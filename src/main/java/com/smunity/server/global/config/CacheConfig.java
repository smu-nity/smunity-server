package com.smunity.server.global.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smunity.server.domain.department.dto.DepartmentResponse;
import com.smunity.server.global.common.dto.ListResponse;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.LoggingCacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

// 캐시를 트랜잭션보다 바깥에 두어 캐시 히트 시 트랜잭션(DB 커넥션)을 시작하지 않는다.
@EnableCaching(order = Ordered.HIGHEST_PRECEDENCE)
@Configuration
public class CacheConfig implements CachingConfigurer {

    public static final String DEPARTMENTS = "departments";

    // RefreshToken 등 다른 Redis 키와 섞이지 않도록 캐시 전용 prefix 사용
    private static final String KEY_PREFIX = "smunity:cache:";
    // 학과별 회원 수는 실시간성이 낮아 5분 지연을 허용한다.
    private static final Duration DEPARTMENTS_TTL = Duration.ofMinutes(5);

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        return RedisCacheManager.builder(connectionFactory)
                .withCacheConfiguration(DEPARTMENTS, cacheConfiguration(DEPARTMENTS_TTL,
                        valueSerializer(ListResponse.class, DepartmentResponse.class)))
                .disableCreateOnMissingCache()
                .build();
    }

    /**
     * 캐시 조회/저장 실패 시 예외를 로그로 남기고 삼킨다.
     * 조회 실패는 캐시 미스로 처리되어 원본(DB) 조회로 폴백된다.
     */
    @Override
    public CacheErrorHandler errorHandler() {
        return new LoggingCacheErrorHandler();
    }

    private RedisCacheConfiguration cacheConfiguration(Duration ttl, Jackson2JsonRedisSerializer<Object> serializer) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(ttl)
                .prefixCacheNameWith(KEY_PREFIX)
                .disableCachingNullValues()
                .serializeKeysWith(SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(SerializationPair.fromSerializer(serializer));
    }

    // 제네릭 레코드를 정확한 타입으로 역직렬화하도록 캐시별 타입을 지정한다.
    // 배포 중 이전 버전이 저장한 캐시를 읽을 수 있도록 모르는 필드는 무시한다.
    private Jackson2JsonRedisSerializer<Object> valueSerializer(Class<?> type, Class<?>... parameterTypes) {
        ObjectMapper objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(type, parameterTypes);
        return new Jackson2JsonRedisSerializer<>(objectMapper, javaType);
    }
}
