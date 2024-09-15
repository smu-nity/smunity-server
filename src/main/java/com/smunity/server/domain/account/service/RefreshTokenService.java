package com.smunity.server.domain.account.service;

import com.smunity.server.domain.account.entity.RefreshToken;
import com.smunity.server.domain.account.repository.RefreshTokenRepository;
import com.smunity.server.global.exception.GeneralException;
import com.smunity.server.global.exception.code.ErrorCode;
import com.smunity.server.global.security.config.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;

    public RefreshToken findRefreshToken(String token) {
        return refreshTokenRepository.findById(token)
                .orElseThrow(() -> new GeneralException(ErrorCode.INVALID_REFRESH_TOKEN));
    }

    @Transactional
    public void saveRefreshToken(Long memberId, String token) {
        long expirationTime = jwtProperties.getExpirationTime(true);
        RefreshToken refreshToken = RefreshToken.of(memberId, token, expirationTime);
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void deleteRefreshToken(String token) {
        Optional<RefreshToken> target = refreshTokenRepository.findById(token);
        target.ifPresent(refreshTokenRepository::delete);
    }
}
