package com.smunity.server.domain.account.service;

import com.smunity.server.global.exception.GeneralException;
import com.smunity.server.global.exception.code.ErrorCode;
import com.smunity.server.global.security.config.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;

/**
 * Refresh 토큰을 Redis에 문자열 키(refresh_token:{token}) → 회원 ID 형태로 저장
 * 조회와 삭제를 원자적으로 처리하기 위해 GETDEL 사용
 * 회원별 전체 폐기를 위해 회원별 토큰 목록(Set, refresh_token_member:{memberId})을 함께 관리
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private static final String KEY_PREFIX = "refresh_token:";
    private static final String MEMBER_KEY_PREFIX = "refresh_token_member:";

    private final StringRedisTemplate redisTemplate;
    private final JwtProperties jwtProperties;

    /**
     * 토큰을 조회하고 즉시 삭제 (동시 요청 시 하나만 성공)
     */
    public Long consumeRefreshToken(String token) {
        String memberId = redisTemplate.opsForValue().getAndDelete(key(token));
        Long id = toMemberId(memberId);
        redisTemplate.opsForSet().remove(memberKey(id), token);
        return id;
    }

    public Long findMemberId(String token) {
        String memberId = redisTemplate.opsForValue().get(key(token));
        return toMemberId(memberId);
    }

    public void saveRefreshToken(Long memberId, String token) {
        Duration ttl = Duration.ofSeconds(jwtProperties.getExpirationTime(true));
        redisTemplate.opsForValue().set(key(token), String.valueOf(memberId), ttl);
        redisTemplate.opsForSet().add(memberKey(memberId), token);
        redisTemplate.expire(memberKey(memberId), ttl);
    }

    public void deleteRefreshToken(String token) {
        String memberId = redisTemplate.opsForValue().getAndDelete(key(token));
        if (memberId != null) {
            redisTemplate.opsForSet().remove(memberKey(Long.valueOf(memberId)), token);
        }
    }

    /**
     * 회원의 모든 refresh 토큰 폐기 (비밀번호 변경, 계정 탈퇴 시)
     */
    public void deleteAllByMemberId(Long memberId) {
        Set<String> tokens = redisTemplate.opsForSet().members(memberKey(memberId));
        if (tokens != null && !tokens.isEmpty()) {
            redisTemplate.delete(tokens.stream().map(this::key).toList());
        }
        redisTemplate.delete(memberKey(memberId));
    }

    private Long toMemberId(String memberId) {
        if (memberId == null) {
            throw new GeneralException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        return Long.valueOf(memberId);
    }

    private String key(String token) {
        return KEY_PREFIX + token;
    }

    private String memberKey(Long memberId) {
        return MEMBER_KEY_PREFIX + memberId;
    }
}
