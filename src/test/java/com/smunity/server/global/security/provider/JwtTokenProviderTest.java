package com.smunity.server.global.security.provider;

import com.smunity.server.global.common.entity.enums.MemberRole;
import com.smunity.server.global.security.config.JwtProperties;
import com.smunity.server.global.security.exception.JwtAuthenticationException;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtTokenProviderTest {

    private static final String SECRET = "test-secret-key-for-unit-test-must-be-32-bytes-or-more";

    private JwtProperties properties;
    private JwtTokenProvider provider;

    @BeforeEach
    void setUp() {
        JwtProperties.TokenProperties token = new JwtProperties.TokenProperties();
        token.setAccessExpirationTime(1800);
        token.setRefreshExpirationTime(1209600);
        properties = new JwtProperties();
        properties.setSecret(SECRET);
        properties.setToken(token);
        provider = new JwtTokenProvider(properties);
    }

    private MockHttpServletRequest bearer(String token) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        return request;
    }

    @Test
    void accessToken은_API_인증에_사용할_수_있다() {
        String token = provider.createAccessToken(1L, MemberRole.ROLE_USER);
        assertThat(provider.getAuthentication(bearer(token))).isNotNull();
    }

    @Test
    void authToken은_인증_필터를_통과하고_VERIFIED_권한만_가진다() {
        String token = provider.createAuthToken("202012345");
        assertThat(provider.getAuthentication(bearer(token)).getAuthorities())
                .extracting(Object::toString).containsExactly(MemberRole.ROLE_VERIFIED.name());
    }

    @Test
    void refreshToken은_API_인증에_사용할_수_없다() {
        String token = provider.createRefreshToken(1L, MemberRole.ROLE_USER);
        assertThatThrownBy(() -> provider.getAuthentication(bearer(token)))
                .isInstanceOf(JwtAuthenticationException.class);
    }

    @Test
    void accessToken은_refresh_검증에_사용할_수_없다() {
        String token = provider.createAccessToken(1L, MemberRole.ROLE_USER);
        assertThatThrownBy(() -> provider.validateToken(token, TokenType.REFRESH))
                .isInstanceOf(JwtAuthenticationException.class);
    }

    @Test
    void authToken은_refresh_검증에_사용할_수_없다() {
        String token = provider.createAuthToken("202012345");
        assertThatThrownBy(() -> provider.validateToken(token, TokenType.REFRESH))
                .isInstanceOf(JwtAuthenticationException.class);
    }

    @Test
    void refreshToken은_refresh_검증을_통과한다() {
        String token = provider.createRefreshToken(1L, MemberRole.ROLE_USER);
        assertThat(provider.validateToken(token, TokenType.REFRESH)).isTrue();
    }

    @Test
    void type_claim이_없는_토큰은_거부한다() {
        String token = Jwts.builder()
                .subject("1")
                .claim("memberRole", MemberRole.ROLE_USER.name())
                .signWith(properties.getSecretKey())
                .expiration(new Date(System.currentTimeMillis() + 60_000))
                .compact();
        assertThatThrownBy(() -> provider.getAuthentication(bearer(token)))
                .isInstanceOf(JwtAuthenticationException.class);
    }

    @Test
    void 같은_시각에_발급해도_토큰이_서로_다르다() {
        assertThat(provider.createRefreshToken(1L, MemberRole.ROLE_USER))
                .isNotEqualTo(provider.createRefreshToken(1L, MemberRole.ROLE_USER));
    }
}
