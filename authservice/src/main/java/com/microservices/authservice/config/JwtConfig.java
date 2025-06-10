package com.microservices.authservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for JWT settings.
 * Maps application.yml properties under the 'app.jwt' prefix to this class.
 */
@Configuration
@ConfigurationProperties(prefix = "app.jwt")
@Data
public class JwtConfig {
    /**
     * Secret key used for signing JWT tokens.
     */
    private String secret;

    /**
     * Expiration time for access tokens in milliseconds.
     */
    private int expirationMs;

    /**
     * Expiration time for refresh tokens in milliseconds.
     */
    private int refreshExpirationMs;
}