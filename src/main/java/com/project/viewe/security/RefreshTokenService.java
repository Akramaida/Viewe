package com.project.viewe.security;

import com.project.viewe.dto.RefreshTokenRequest;
import com.project.viewe.exception.ProjectException;
import com.project.viewe.model.RefreshToken;
import com.project.viewe.repo.RefreshTokenRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {
    private final RefreshTokenRepo refreshTokenRepo;

    public RefreshToken generateRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());
        return  refreshTokenRepo.save(refreshToken);
    }

    public void validateRefreshToken(String refreshToken) {
        refreshTokenRepo.findByToken(refreshToken)
                .orElseThrow(() -> new ProjectException("Invalid refresh token"));
    }

    public void deletedRefreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenRepo.deleteByToken(refreshTokenRequest.getRefreshToken());
    }
}
