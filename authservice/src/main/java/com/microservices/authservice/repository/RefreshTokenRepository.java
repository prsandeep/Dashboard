package com.microservices.authservice.repository;

import com.microservices.authservice.model.RefreshToken;
import com.microservices.authservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for RefreshToken entity, providing methods to interact with the refresh_tokens table.
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    /**
     * Find a refresh token by its token value.
     * @param token the token string to search for
     * @return an Optional containing the refresh token if found, empty otherwise
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Find a refresh token by its associated user.
     * @param user the user to search by
     * @return an Optional containing the refresh token if found, empty otherwise
     */
    Optional<RefreshToken> findByUser(User user);

    /**
     * Delete refresh tokens for a specific user.
     * @param user the user whose tokens should be deleted
     * @return the number of tokens deleted
     */
    @Modifying
    int deleteByUser(User user);
}