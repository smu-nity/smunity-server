package com.smunity.server.domain.account.service;

import com.smunity.server.global.exception.GeneralException;
import com.smunity.server.global.security.config.JwtProperties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * 실제 Redis(localhost:6379)를 사용하는 테스트. Redis에 연결할 수 없으면 건너뜀
 */
class RefreshTokenServiceRedisTest {

    private LettuceConnectionFactory connectionFactory;
    private StringRedisTemplate redisTemplate;
    private RefreshTokenService service;
    private String token;

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
        if (connectionFactory != null) connectionFactory.destroy();
    }

    @Test
    void 저장한_토큰은_TTL이_설정된다() {
        service.saveRefreshToken(7L, token);
        Long ttl = redisTemplate.getExpire("refresh_token:" + token);
        assertThat(ttl).isBetween(1L, 60L);
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
