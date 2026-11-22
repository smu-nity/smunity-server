package com.smunity.server.domain.department.service;

import com.smunity.server.domain.department.dto.DepartmentResponse;
import com.smunity.server.domain.department.mapper.DepartmentMapper;
import com.smunity.server.domain.department.repository.DepartmentQueryRepository;
import com.smunity.server.global.common.dto.ListResponse;
import com.smunity.server.global.common.repository.DepartmentRepository;
import com.smunity.server.global.config.CacheConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.SimpleTransactionStatus;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 학과 목록 Redis 캐시 검증.
 * 캐시 히트 테스트는 실제 Redis(localhost:6379)를 사용하며, 연결할 수 없으면 건너뜀
 */
class DepartmentServiceCacheTest {

    private static final String KEY = "smunity:cache:departments::all";
    private static final List<DepartmentResponse> DEPARTMENTS = List.of(
            DepartmentResponse.builder().college("융합공과대학").name("컴퓨터과학전공").code("03005").count(3).build());

    private final DepartmentQueryRepository departmentQueryRepository = mock(DepartmentQueryRepository.class);
    private AnnotationConfigApplicationContext context;
    private LettuceConnectionFactory connectionFactory;

    @AfterEach
    void tearDown() {
        if (connectionFactory != null) {
            try {
                new StringRedisTemplate(connectionFactory).delete(KEY);
            } catch (Exception ignored) {
            }
        }
        if (context != null) context.close();
        if (connectionFactory != null) connectionFactory.destroy();
    }

    @Test
    void 두번째_조회부터는_DB를_조회하지_않고_캐시에서_반환한다() {
        connectionFactory = connectionFactory(6379);
        assumeRedisAvailable(connectionFactory);
        new StringRedisTemplate(connectionFactory).delete(KEY);
        when(departmentQueryRepository.findAllWithMemberCount()).thenReturn(DEPARTMENTS);
        DepartmentService service = departmentService(connectionFactory);

        service.readDepartments();
        ListResponse<DepartmentResponse> cached = service.readDepartments();

        assertThat(cached.content()).isEqualTo(DEPARTMENTS);
        assertThat(cached.content().get(0)).isInstanceOf(DepartmentResponse.class);
        verify(departmentQueryRepository, times(1)).findAllWithMemberCount();
    }

    @Test
    void 캐시_히트_시에는_트랜잭션을_시작하지_않는다() {
        connectionFactory = connectionFactory(6379);
        assumeRedisAvailable(connectionFactory);
        new StringRedisTemplate(connectionFactory).delete(KEY);
        when(departmentQueryRepository.findAllWithMemberCount()).thenReturn(DEPARTMENTS);
        PlatformTransactionManager transactionManager = mock(PlatformTransactionManager.class);
        when(transactionManager.getTransaction(any())).thenReturn(new SimpleTransactionStatus());
        DepartmentService service = departmentService(connectionFactory, transactionManager);

        service.readDepartments();
        service.readDepartments();
        service.readDepartments();

        verify(transactionManager, times(1)).getTransaction(any());
    }

    @Test
    void 이전_버전이_저장한_캐시에_모르는_필드가_있어도_캐시에서_읽는다() {
        connectionFactory = connectionFactory(6379);
        assumeRedisAvailable(connectionFactory);
        // 이전 버전 DTO에만 있던 필드(memberCount, version)가 섞인 캐시
        new StringRedisTemplate(connectionFactory).opsForValue().set(KEY, """
                {"count":1,"version":1,"content":[
                  {"college":"융합공과대학","name":"컴퓨터과학전공","code":"03005","count":3,"memberCount":3}
                ]}
                """, Duration.ofMinutes(1));
        DepartmentService service = departmentService(connectionFactory);

        ListResponse<DepartmentResponse> cached = service.readDepartments();

        assertThat(cached.content()).isEqualTo(DEPARTMENTS);
        verify(departmentQueryRepository, never()).findAllWithMemberCount();
    }

    @Test
    void 캐시는_전용_prefix_키에_5분_TTL로_저장된다() {
        connectionFactory = connectionFactory(6379);
        assumeRedisAvailable(connectionFactory);
        when(departmentQueryRepository.findAllWithMemberCount()).thenReturn(DEPARTMENTS);
        DepartmentService service = departmentService(connectionFactory);

        service.readDepartments();

        Long ttl = new StringRedisTemplate(connectionFactory).getExpire(KEY);
        assertThat(ttl).isBetween(Duration.ofMinutes(4).toSeconds(), Duration.ofMinutes(5).toSeconds());
    }

    @Test
    void Redis_장애_시_예외_없이_DB에서_조회한다() {
        // 열려 있지 않은 포트로 연결 → 캐시 조회/저장 실패
        connectionFactory = connectionFactory(1);
        when(departmentQueryRepository.findAllWithMemberCount()).thenReturn(DEPARTMENTS);
        DepartmentService service = departmentService(connectionFactory);

        ListResponse<DepartmentResponse> first = service.readDepartments();
        ListResponse<DepartmentResponse> second = service.readDepartments();

        assertThat(first.content()).isEqualTo(DEPARTMENTS);
        assertThat(second.content()).isEqualTo(DEPARTMENTS);
        verify(departmentQueryRepository, times(2)).findAllWithMemberCount();
    }

    private DepartmentService departmentService(RedisConnectionFactory redis) {
        return departmentService(redis, null);
    }

    private DepartmentService departmentService(RedisConnectionFactory redis, PlatformTransactionManager transactionManager) {
        context = new AnnotationConfigApplicationContext();
        // 트랜잭션 설정을 먼저 등록한다. 순서값이 같으면 등록 순서를 따르므로,
        // CacheConfig의 order 지정 없이는 트랜잭션이 바깥에 놓이는 조건에서 검증한다.
        if (transactionManager != null) {
            context.registerBean(PlatformTransactionManager.class, () -> transactionManager);
            context.register(TransactionConfig.class);
        }
        context.registerBean(RedisConnectionFactory.class, () -> redis);
        context.register(CacheConfig.class);
        context.registerBean(DepartmentService.class, () -> new DepartmentService(
                mock(DepartmentRepository.class), departmentQueryRepository, mock(DepartmentMapper.class)));
        context.refresh();
        return context.getBean(DepartmentService.class);
    }

    private static LettuceConnectionFactory connectionFactory(int port) {
        LettuceConnectionFactory factory = new LettuceConnectionFactory(
                new RedisStandaloneConfiguration("localhost", port),
                LettuceClientConfiguration.builder().commandTimeout(Duration.ofMillis(500)).build());
        factory.afterPropertiesSet();
        return factory;
    }

    private static void assumeRedisAvailable(RedisConnectionFactory factory) {
        try {
            factory.getConnection().ping();
        } catch (Exception e) {
            assumeTrue(false, "Redis에 연결할 수 없어 테스트를 건너뜁니다.");
        }
    }

    @Configuration
    @EnableTransactionManagement
    static class TransactionConfig {
    }
}
