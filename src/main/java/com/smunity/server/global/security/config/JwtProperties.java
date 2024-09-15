package com.smunity.server.global.security.config;

import io.jsonwebtoken.Jwts;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Component
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {

    private String secret;
    private TokenProperties token;

    // 비밀 키 생성
    public SecretKey getSecretKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    // JWT 토큰의 만료 시간을 반환
    public long getExpirationTime(boolean isRefresh) {
        return isRefresh ? token.refreshExpirationTime : token.accessExpirationTime;
    }

    @Data
    public static class TokenProperties {
        private long accessExpirationTime;
        private long refreshExpirationTime;
    }
}
