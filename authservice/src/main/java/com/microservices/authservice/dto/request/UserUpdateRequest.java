package com.microservices.authservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

/**
 * Data Transfer Object for user update requests.
 */
@Data
public class UserUpdateRequest {
    /**
     * New username (optional).
     */
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    /**
     * New email (optional).
     */
    @Size(max = 50, message = "Email must not exceed 50 characters")
    @Email(message = "Email should be valid")
    private String email;

    /**
     * New password (optional).
     */
    @Size(min = 6, max = 40, message = "Password must be between 6 and 40 characters")
    private String password;

    /**
     * New set of roles (optional).
     */
    private Set<String> roles;
}