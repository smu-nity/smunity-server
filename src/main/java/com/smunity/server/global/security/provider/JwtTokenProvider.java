package com.smunity.server.global.security.provider;

import com.smunity.server.global.common.entity.enums.MemberRole;
import com.smunity.server.global.exception.code.ErrorCode;
import com.smunity.server.global.security.config.JwtProperties;
import com.smunity.server.global.security.exception.JwtAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * JWT 토큰을 생성하고 검증하는 클래스
 * JWT 토큰을 생성, 검증하며, 토큰에서 인증 정보를 추출하여 인증 처리에 사용
 */
@Component
@RequiredArgsConstructor
public class JwtTokenProvider implements AuthProvider {

    private static final String CLAIM_MEMBER_ROLE = "memberRole";

    private final JwtProperties jwtProperties;

    /**
     * HTTP 요청에서 JWT 토큰을 추출하고, 유효성 검증 후 인증 정보를 담은 Authentication 객체를 반환
     */
    public Authentication getAuthentication(HttpServletRequest request) {
        String jwt = resolveToken(request);
        return validateToken(jwt, false) ? getAuthentication(jwt) : null;
    }

    /**
     * JWT access 토큰 생성
     */
    public String createAccessToken(Long memberId, MemberRole memberRole, boolean isRefresh) {
        return createToken(String.valueOf(memberId), memberRole, isRefresh);
    }

    /**
     * JWT auth 토큰 생성 (재학생 인증)
     */
    public String createAuthToken(String username) {
        return createToken(username, MemberRole.ROLE_VERIFIED, false);
    }

    /**
     * JWT 토큰 유효성 검증
     *
     * @param isRefresh JWT refresh 토큰인지 여부
     */
    public boolean validateToken(String token, boolean isRefresh) {
        if (!StringUtils.hasText(token)) return false;
        try {
            getClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            ErrorCode errorCode = isRefresh ? ErrorCode.RELOGIN_EXCEPTION : ErrorCode.EXPIRED_JWT_EXCEPTION;
            throw new JwtAuthenticationException(errorCode);
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException(ErrorCode.INVALID_TOKEN_EXCEPTION);
        }
    }

    // HTTP 요청에서 JWT 토큰을 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ") ?
                bearerToken.substring(7) : null;
    }

    // JWT 토큰을 기반으로 Authentication 객체 생성
    private Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        String memberRole = claims.get(CLAIM_MEMBER_ROLE, String.class);
        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(memberRole));
        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    // JWT 토큰 생성
    private String createToken(String subject, MemberRole memberRole, boolean isRefresh) {
        return Jwts.builder()
                .subject(subject)
                .claim(CLAIM_MEMBER_ROLE, memberRole.name())
                .signWith(jwtProperties.getSecretKey())
                .expiration(expirationDate(isRefresh))
                .compact();
    }

    // 액세스 토큰의 만료 시간 계산
    private Date expirationDate(boolean isRefresh) {
        Date now = new Date();
        long expirationTime = jwtProperties.getExpirationTime(isRefresh) * 1_000;
        return new Date(now.getTime() + expirationTime);
    }

    // JWT 토큰에서 클레임 정보를 추출
    private Claims getClaims(String token) {
        return Jwts.parser().verifyWith(jwtProperties.getSecretKey()).build().parseSignedClaims(token).getPayload();
    }
}
