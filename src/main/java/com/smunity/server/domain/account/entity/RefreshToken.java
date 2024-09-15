package com.smunity.server.domain.account.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    private String token;

    private Long memberId;

    @TimeToLive
    private long expirationTime;

    public static RefreshToken of(Long memberId, String token, long expirationTime) {
        return RefreshToken.builder()
                .memberId(memberId)
                .token(token)
                .expirationTime(expirationTime)
                .build();
    }
}
