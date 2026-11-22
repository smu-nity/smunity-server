package com.smunity.server.domain.department.reader;

import com.smunity.server.domain.department.config.DepartmentProperties;
import com.smunity.server.domain.department.config.DepartmentProperties.ReadStrategy;
import com.smunity.server.domain.department.dto.DepartmentResponse;
import com.smunity.server.global.common.dto.ListResponse;
import com.smunity.server.global.config.CacheConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.SimpleTransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * CacheConfig + CachedDepartmentReader 동작 검증.
 * Redis 테스트는 실제 Redis(localhost:6379)를 사용하며, 연결할 수 없으면 건너뜀
 */
class CachedDepartmentReaderTest {

    private static final String REDIS_KEY = "smunity:cache:departments::all";
    private static final ListResponse<DepartmentResponse> RESPONSE = ListResponse.from(List.of(
            DepartmentResponse.builder().college("융합공과대학").name("컴퓨터과학전공").code("03005").count(3).build()));

    private final AtomicInteger delegateCalls = new AtomicInteger();
    private final DepartmentReader delegate = () -> {
        delegateCalls.incrementAndGet();
        return RESPONSE;
    };

    private AnnotationConfigApplicationContext context;
    private LettuceConnectionFactory connectionFactory;

    @AfterEach
    void tearDown() {
        if (connectionFactory != null) {
            try {
                new StringRedisTemplate(connectionFactory).delete(REDIS_KEY);
            } catch (Exception ignored) {
            }
        }
        if (context != null) context.close();
        if (connectionFactory != null) connectionFactory.destroy();
    }

    @Test
    void LOCAL_CACHE는_두번째_조회부터_캐시에서_반환한다() {
        DepartmentReader reader = cachedReader(ReadStrategy.LOCAL_CACHE, Duration.ofMinutes(1), null);

        reader.readDepartments();
        ListResponse<DepartmentResponse> second = reader.readDepartments();

        assertThat(second).isEqualTo(RESPONSE);
        assertThat(delegateCalls).hasValue(1);
    }

    @Test
    void LOCAL_CACHE는_TTL이_지나면_다시_조회한다() throws InterruptedException {
        DepartmentReader reader = cachedReader(ReadStrategy.LOCAL_CACHE, Duration.ofMillis(100), null);

        reader.readDepartments();
        Thread.sleep(200);
        reader.readDepartments();

        assertThat(delegateCalls).hasValue(2);
    }

    @Test
    void 캐시_히트_시에는_트랜잭션을_시작하지_않는다() {
        PlatformTransactionManager transactionManager = mock(PlatformTransactionManager.class);
        when(transactionManager.getTransaction(any())).thenReturn(new SimpleTransactionStatus());
        DepartmentReader transactional = new TransactionalDepartmentReader(delegate, new TransactionTemplate(transactionManager));
        DepartmentReader reader = cachedReader(ReadStrategy.LOCAL_CACHE, Duration.ofMinutes(1), null, transactional);

        reader.readDepartments();
        reader.readDepartments();
        reader.readDepartments();

        verify(transactionManager, times(1)).getTransaction(any());
    }

    @Test
    void REDIS_CACHE는_제네릭_응답을_역직렬화해_반환한다() {
        connectionFactory = redisConnectionFactory(6379);
        assumeRedisAvailable(connectionFactory);
        new StringRedisTemplate(connectionFactory).delete(REDIS_KEY);
        DepartmentReader reader = cachedReader(ReadStrategy.REDIS_CACHE, Duration.ofMinutes(1), connectionFactory);

        reader.readDepartments();
        ListResponse<DepartmentResponse> second = reader.readDepartments();

        assertThat(second).isEqualTo(RESPONSE);
        assertThat(second.content().get(0)).isInstanceOf(DepartmentResponse.class);
        assertThat(delegateCalls).hasValue(1);
        assertThat(new StringRedisTemplate(connectionFactory).getExpire(REDIS_KEY)).isPositive();
    }

    @Test
    void REDIS_CACHE는_Redis_장애_시_원본_조회로_폴백한다() {
        // 열려 있지 않은 포트로 연결 → 캐시 조회/저장 실패
        connectionFactory = redisConnectionFactory(1);
        DepartmentReader reader = cachedReader(ReadStrategy.REDIS_CACHE, Duration.ofMinutes(1), connectionFactory);

        ListResponse<DepartmentResponse> first = reader.readDepartments();
        ListResponse<DepartmentResponse> second = reader.readDepartments();

        assertThat(first).isEqualTo(RESPONSE);
        assertThat(second).isEqualTo(RESPONSE);
        assertThat(delegateCalls).hasValue(2);
    }

    private DepartmentReader cachedReader(ReadStrategy strategy, Duration ttl, RedisConnectionFactory redis) {
        return cachedReader(strategy, ttl, redis, delegate);
    }

    private DepartmentReader cachedReader(ReadStrategy strategy, Duration ttl, RedisConnectionFactory redis,
                                          DepartmentReader target) {
        context = new AnnotationConfigApplicationContext();
        context.registerBean(DepartmentProperties.class, () -> new DepartmentProperties(
                strategy, new DepartmentProperties.Cache(ttl), new DepartmentProperties.Scheduler(false)));
        if (redis != null) {
            context.registerBean(RedisConnectionFactory.class, () -> redis);
        }
        context.register(CacheConfig.class);
        context.registerBean(DepartmentReader.class, () -> new CachedDepartmentReader(target));
        context.refresh();
        return context.getBean(DepartmentReader.class);
    }

    private static LettuceConnectionFactory redisConnectionFactory(int port) {
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
}
