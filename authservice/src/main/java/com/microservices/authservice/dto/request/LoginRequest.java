package com.microservices.authservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Data Transfer Object for login requests.
 */
@Data
public class LoginRequest {
    /**
     * Username for login.
     */
    @NotBlank(message = "Username is required")
    private String username;

    /**
     * Password for login.
     */
    @NotBlank(message = "Password is required")
    private String password;
}