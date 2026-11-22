package com.smunity.server.domain.account.service;

import com.smunity.server.global.exception.GeneralException;
import com.smunity.server.global.exception.code.ErrorCode;
import com.smunity.server.global.security.config.JwtProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class RefreshTokenServiceTest {

    private static final String KEY = "refresh_token:token-1";

    private StringRedisTemplate redisTemplate;
    private ValueOperations<String, String> valueOperations;
    private SetOperations<String, String> setOperations;
    private RefreshTokenService service;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        redisTemplate = mock(StringRedisTemplate.class);
        valueOperations = mock(ValueOperations.class);
        setOperations = mock(SetOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(redisTemplate.opsForSet()).thenReturn(setOperations);

        JwtProperties.TokenProperties token = new JwtProperties.TokenProperties();
        token.setAccessExpirationTime(1800);
        token.setRefreshExpirationTime(1209600);
        JwtProperties properties = new JwtProperties();
        properties.setToken(token);
        service = new RefreshTokenService(redisTemplate, properties);
    }

    @Test
    void 토큰을_회원_ID와_만료시간과_함께_저장한다() {
        service.saveRefreshToken(7L, "token-1");
        verify(valueOperations).set(KEY, "7", Duration.ofSeconds(1209600));
        verify(setOperations).add("refresh_token_member:7", "token-1");
        verify(redisTemplate).expire("refresh_token_member:7", Duration.ofSeconds(1209600));
    }

    @Test
    void consume은_GETDEL로_조회와_삭제를_한번에_처리한다() {
        when(valueOperations.getAndDelete(KEY)).thenReturn("7");
        assertThat(service.consumeRefreshToken("token-1")).isEqualTo(7L);
        verify(valueOperations).getAndDelete(KEY);
        verify(setOperations).remove("refresh_token_member:7", "token-1");
        verify(redisTemplate, never()).delete(anyString());
    }

    @Test
    void 회원의_모든_토큰을_폐기한다() {
        when(setOperations.members("refresh_token_member:7")).thenReturn(Set.of("token-1", "token-2"));

        service.deleteAllByMemberId(7L);

        verify(redisTemplate).delete(argThat((Collection<String> keys) ->
                keys.size() == 2 && keys.containsAll(List.of("refresh_token:token-1", "refresh_token:token-2"))));
        verify(redisTemplate).delete("refresh_token_member:7");
    }

    @Test
    void 토큰이_없는_회원도_예외없이_폐기를_처리한다() {
        when(setOperations.members("refresh_token_member:7")).thenReturn(Set.of());

        service.deleteAllByMemberId(7L);

        verify(redisTemplate).delete("refresh_token_member:7");
        verify(redisTemplate, never()).delete(anyCollection());
    }

    @Test
    void 존재하지_않거나_이미_사용된_토큰은_거부한다() {
        when(valueOperations.getAndDelete(KEY)).thenReturn(null);
        assertThatThrownBy(() -> service.consumeRefreshToken("token-1"))
                .isInstanceOfSatisfying(GeneralException.class,
                        e -> assertThat(e.getErrorCode()).isEqualTo(ErrorCode.INVALID_REFRESH_TOKEN));
    }

    @Test
    void findMemberId는_토큰을_삭제하지_않는다() {
        when(valueOperations.get(KEY)).thenReturn("7");
        assertThat(service.findMemberId("token-1")).isEqualTo(7L);
        verify(valueOperations, never()).getAndDelete(anyString());
    }

    @Test
    void 동시에_소비해도_저장소가_한번만_반환하면_하나만_성공한다() throws Exception {
        // Redis GETDEL은 원자적이므로 값을 한 번만 반환한다는 계약을 모사
        AtomicInteger calls = new AtomicInteger();
        when(valueOperations.getAndDelete(KEY)).thenAnswer(i -> calls.getAndIncrement() == 0 ? "7" : null);

        int threads = 20;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch start = new CountDownLatch(1);
        AtomicInteger success = new AtomicInteger();
        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {
                start.await();
                try {
                    service.consumeRefreshToken("token-1");
                    success.incrementAndGet();
                } catch (GeneralException ignored) {
                }
                return null;
            });
        }
        start.countDown();
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        assertThat(success.get()).isEqualTo(1);
    }
}
