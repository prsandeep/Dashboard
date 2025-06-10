package com.microservices.authservice.service;

import com.microservices.authservice.exception.TokenRefreshException;
import com.microservices.authservice.model.RefreshToken;
import com.microservices.authservice.model.User;
import com.microservices.authservice.repository.RefreshTokenRepository;
import com.microservices.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for handling refresh token operations.
 */
@Service
public class RefreshTokenService {

    @Value("${app.jwt.refresh-expiration-ms}")
    private long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Find a refresh token by its token value.
     *
     * @param token the token string
     * @return an Optional containing the refresh token if found
     */
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    /**
     * Create a new refresh token for a user.
     *
     * @param userId the ID of the user
     * @return the created refresh token
     */
    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId)));
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        // Check if user already has a refresh token
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUser(refreshToken.getUser());
        if (existingToken.isPresent()) {
            // Update existing token instead of creating a new one
            RefreshToken token = existingToken.get();
            token.setExpiryDate(refreshToken.getExpiryDate());
            token.setToken(refreshToken.getToken());
            return refreshTokenRepository.save(token);
        }

        return refreshTokenRepository.save(refreshToken);
    }

    /**
     * Verify if a refresh token is still valid (not expired).
     *
     * @param token the refresh token to verify
     * @return the verified refresh token
     */
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

    /**
     * Delete all refresh tokens for a user.
     *
     * @param userId the ID of the user
     * @return the number of tokens deleted
     */
    @Transactional
    public int deleteByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return refreshTokenRepository.deleteByUser(user);
    }
}