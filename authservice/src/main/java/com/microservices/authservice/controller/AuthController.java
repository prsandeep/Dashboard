package com.microservices.authservice.controller;

import com.microservices.authservice.dto.request.LoginRequest;
import com.microservices.authservice.dto.request.RefreshTokenRequest;
import com.microservices.authservice.dto.request.SignupRequest;
import com.microservices.authservice.dto.response.JwtResponse;
import com.microservices.authservice.dto.response.MessageResponse;
import com.microservices.authservice.security.services.UserDetailsImpl;
import com.microservices.authservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller for authentication operations.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Authenticate a user and generate JWT tokens.
     *
     * @param loginRequest login credentials
     * @return JWT response with tokens and user info
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.authenticateUser(loginRequest));
    }

    /**
     * Register a new user account.
     *
     * @param signupRequest signup information
     * @return response message
     */
    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        return ResponseEntity.ok(authService.registerUser(signupRequest));
    }

    /**
     * Refresh an access token using a refresh token.
     *
     * @param request refresh token request
     * @return JWT response with new access token
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<JwtResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request.getRefreshToken()));
    }

    /**
     * Log out the current user.
     *
     * @return response message
     */
    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logoutUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(authService.logoutUser(userDetails.getId()));
    }


    @PostMapping("/validate")
    public ResponseEntity<Map<String, Boolean>> validateToken(@RequestBody Map<String, String> tokenMap) {
        String token = tokenMap.get("token");
        boolean isValid = authService.validateToken(token);
        return ResponseEntity.ok(Map.of("valid", isValid));
    }

    /**
     * Get information about the current user.
     *
     * @param authentication the current authentication
     * @return user details
     */
    @GetMapping("/me")
    public ResponseEntity<UserDetailsImpl> getCurrentUser(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(userDetails);
    }
}