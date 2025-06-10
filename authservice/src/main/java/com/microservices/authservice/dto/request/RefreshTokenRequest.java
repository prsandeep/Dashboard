package com.microservices.authservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Data Transfer Object for token refresh requests.
 */
@Data
public class RefreshTokenRequest {
    /**
     * Refresh token to be used for generating a new access token.
     */
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;
}