package com.smunity.server.global.config;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.smunity.server.domain.department.config.DepartmentProperties;
import com.smunity.server.domain.department.dto.DepartmentResponse;
import com.smunity.server.global.common.dto.ListResponse;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.LoggingCacheErrorHandler;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.List;
import java.util.Set;

@EnableCaching
@Configuration
public class CacheConfig implements CachingConfigurer {

    public static final String DEPARTMENTS = "departments";

    // RefreshToken 등 다른 Redis 키와 섞이지 않도록 캐시 전용 prefix 사용
    private static final String REDIS_KEY_PREFIX = "smunity:cache:";
    private static final long LOCAL_CACHE_MAXIMUM_SIZE = 100;

    @Bean
    public CacheManager cacheManager(DepartmentProperties properties,
                                     ObjectProvider<RedisConnectionFactory> redisConnectionFactory) {
        Duration ttl = properties.cache().ttl();
        return switch (properties.readStrategy()) {
            case REDIS_CACHE -> redisCacheManager(redisConnectionFactory.getObject(), ttl);
            case LOCAL_CACHE -> localCacheManager(ttl);
            default -> new NoOpCacheManager();
        };
    }

    /**
     * 캐시 조회/저장 실패 시 예외를 로그로 남기고 삼킨다.
     * 조회 실패는 캐시 미스로 처리되어 원본(DB) 조회로 폴백된다.
     */
    @Override
    public CacheErrorHandler errorHandler() {
        return new LoggingCacheErrorHandler();
    }

    private RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory, Duration ttl) {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType departmentsType = objectMapper.getTypeFactory()
                .constructParametricType(ListResponse.class, DepartmentResponse.class);
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(ttl)
                .prefixCacheNameWith(REDIS_KEY_PREFIX)
                .disableCachingNullValues()
                .serializeKeysWith(SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(SerializationPair.fromSerializer(
                        new Jackson2JsonRedisSerializer<>(objectMapper, departmentsType)));
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(configuration)
                .initialCacheNames(Set.of(DEPARTMENTS))
                .disableCreateOnMissingCache()
                .enableStatistics()
                .build();
    }

    private CaffeineCacheManager localCacheManager(Duration ttl) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(ttl)
                .maximumSize(LOCAL_CACHE_MAXIMUM_SIZE)
                .recordStats());
        cacheManager.setAllowNullValues(false);
        cacheManager.setCacheNames(List.of(DEPARTMENTS));
        return cacheManager;
    }
}
