package com.microservices.authservice.dto.response;

import lombok.Data;

import java.util.List;

/**
 * Data Transfer Object for JWT authentication responses.
 */
@Data
public class JwtResponse {
    /**
     * Type of token, typically "Bearer".
     */
    private String tokenType = "Bearer";

    /**
     * JWT access token.
     */
    private String accessToken;

    /**
     * JWT refresh token.
     */
    private String refreshToken;

    /**
     * User ID.
     */
    private Long id;

    /**
     * Username.
     */
    private String username;

    /**
     * User email.
     */
    private String email;

    /**
     * User roles.
     */
    private List<String> roles;

    /**
     * Constructor for creating a JWT response with all fields.
     *
     * @param accessToken the JWT access token
     * @param refreshToken the JWT refresh token
     * @param id the user ID
     * @param username the username
     * @param email the user email
     * @param roles the user roles
     */
    public JwtResponse(String accessToken, String refreshToken, Long id, String username, String email, List<String> roles) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}