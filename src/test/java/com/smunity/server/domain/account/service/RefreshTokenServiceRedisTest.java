package com.smunity.server.domain.account.service;

import com.smunity.server.global.exception.GeneralException;
import com.smunity.server.global.security.config.JwtProperties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * 실제 Redis(localhost:6379)를 사용하는 테스트. Redis에 연결할 수 없으면 건너뜀
 */
class RefreshTokenServiceRedisTest {

    private LettuceConnectionFactory connectionFactory;
    private StringRedisTemplate redisTemplate;
    private RefreshTokenService service;
    private String token;
    private final List<Long> memberIds = new ArrayList<>();

    @BeforeEach
    void setUp() {
        connectionFactory = new LettuceConnectionFactory(new RedisStandaloneConfiguration("localhost", 6379));
        connectionFactory.afterPropertiesSet();
        redisTemplate = new StringRedisTemplate(connectionFactory);
        redisTemplate.afterPropertiesSet();
        try {
            redisTemplate.getConnectionFactory().getConnection().ping();
        } catch (Exception e) {
            assumeTrue(false, "Redis에 연결할 수 없어 테스트를 건너뜁니다.");
        }

        JwtProperties.TokenProperties tokenProperties = new JwtProperties.TokenProperties();
        tokenProperties.setRefreshExpirationTime(60);
        JwtProperties properties = new JwtProperties();
        properties.setToken(tokenProperties);
        service = new RefreshTokenService(redisTemplate, properties);
        token = "test-" + UUID.randomUUID();
    }

    @AfterEach
    void tearDown() {
        if (redisTemplate != null && token != null) service.deleteRefreshToken(token);
        if (redisTemplate != null) memberIds.forEach(id -> service.deleteAllByMemberId(id));
        if (connectionFactory != null) connectionFactory.destroy();
    }

    @Test
    void 저장한_토큰은_TTL이_설정된다() {
        service.saveRefreshToken(7L, token);
        Long ttl = redisTemplate.getExpire("refresh_token:" + token);
        assertThat(ttl).isBetween(1L, 60L);
    }

    private long newMemberId() {
        long id = 900_000_000L + ThreadLocalRandom.current().nextLong(100_000_000L);
        memberIds.add(id);
        return id;
    }

    @Test
    void 회원의_모든_토큰을_폐기하고_다른_회원의_토큰은_유지한다() {
        long memberId = newMemberId();
        long otherId = newMemberId();
        String t1 = token + "-1", t2 = token + "-2", other = token + "-other";
        service.saveRefreshToken(memberId, t1);
        service.saveRefreshToken(memberId, t2);
        service.saveRefreshToken(otherId, other);

        service.deleteAllByMemberId(memberId);

        assertThat(redisTemplate.hasKey("refresh_token:" + t1)).isFalse();
        assertThat(redisTemplate.hasKey("refresh_token:" + t2)).isFalse();
        assertThat(redisTemplate.hasKey("refresh_token_member:" + memberId)).isFalse();
        assertThat(service.findMemberId(other)).isEqualTo(otherId);
        assertThatThrownBy(() -> service.consumeRefreshToken(t1)).isInstanceOf(GeneralException.class);
    }

    @Test
    void 소비하거나_삭제한_토큰은_회원_목록에서도_제거된다() {
        long memberId = newMemberId();
        String t1 = token + "-1", t2 = token + "-2";
        service.saveRefreshToken(memberId, t1);
        service.saveRefreshToken(memberId, t2);

        service.consumeRefreshToken(t1);
        service.deleteRefreshToken(t2);

        assertThat(redisTemplate.opsForSet().members("refresh_token_member:" + memberId)).isEmpty();
    }

    @Test
    void 회원_토큰_목록에도_TTL이_설정된다() {
        long memberId = newMemberId();
        service.saveRefreshToken(memberId, token);
        assertThat(redisTemplate.getExpire("refresh_token_member:" + memberId)).isBetween(1L, 60L);
    }

    @Test
    void 같은_토큰으로_동시에_소비하면_하나만_성공한다() throws Exception {
        service.saveRefreshToken(7L, token);

        int threads = 50;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch ready = new CountDownLatch(threads);
        CountDownLatch start = new CountDownLatch(1);
        AtomicInteger success = new AtomicInteger();
        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {
                ready.countDown();
                start.await();
                try {
                    assertThat(service.consumeRefreshToken(token)).isEqualTo(7L);
                    success.incrementAndGet();
                } catch (GeneralException ignored) {
                }
                return null;
            });
        }
        ready.await();
        start.countDown();
        executor.shutdown();
        assertThat(executor.awaitTermination(10, TimeUnit.SECONDS)).isTrue();

        assertThat(success.get()).isEqualTo(1);
    }
}
