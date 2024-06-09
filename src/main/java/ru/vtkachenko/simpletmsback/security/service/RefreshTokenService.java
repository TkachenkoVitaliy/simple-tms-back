package ru.vtkachenko.simpletmsback.security.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vtkachenko.simpletmsback.security.jwt.JwtUtils;
import ru.vtkachenko.simpletmsback.security.model.RefreshToken;
import ru.vtkachenko.simpletmsback.security.model.User;
import ru.vtkachenko.simpletmsback.security.repository.RefreshTokenRepository;
import ru.vtkachenko.simpletmsback.security.repository.UserRepository;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @Transactional
    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken;
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Optional<RefreshToken> optionalRefresh = refreshTokenRepository.findByUser(user);
        refreshToken = optionalRefresh.orElseGet(RefreshToken::new);

        String token = jwtUtils.generateRefreshToken(user);
        Date expiration = jwtUtils.getExpirationFromJwtToken(token);

        refreshToken.setUser(user);
        refreshToken.setToken(token);
        refreshToken.setExpiryDate(expiration.toInstant());

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh Token not in database"));
        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token was expired");
        }
        return refreshToken;
    }

    public int deleteByUserId(Long userId) {
        return refreshTokenRepository.deleteByUser(
                userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found"))
        );
    }
}
