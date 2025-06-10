package com.microservices.authservice.service;

import com.microservices.authservice.dto.request.UserUpdateRequest;
import com.microservices.authservice.dto.response.MessageResponse;
import com.microservices.authservice.dto.response.UserResponse;
import com.microservices.authservice.exception.ResourceNotFoundException;
import com.microservices.authservice.exception.UserOperationException;
import com.microservices.authservice.model.ERole;
import com.microservices.authservice.model.Role;
import com.microservices.authservice.model.User;
import com.microservices.authservice.repository.RoleRepository;
import com.microservices.authservice.repository.UserRepository;
import com.microservices.authservice.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service for user management operations.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RefreshTokenService refreshTokenService;

    /**
     * Get all users.
     *
     * @return list of all users
     */
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Get a user by ID.
     *
     * @param id the user ID
     * @return the user response
     */
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return new UserResponse(user);
    }

    /**
     * Update a user.
     *
     * @param id the user ID
     * @param updateRequest the update information
     * @return the updated user response
     */
    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest updateRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Update username if provided
        if (StringUtils.hasText(updateRequest.getUsername())
                && !user.getUsername().equals(updateRequest.getUsername())) {
            // Check if new username is already taken
            if (userRepository.existsByUsername(updateRequest.getUsername())) {
                throw new UserOperationException("Username is already taken");
            }
            user.setUsername(updateRequest.getUsername());
        }

        // Update email if provided
        if (StringUtils.hasText(updateRequest.getEmail())
                && !user.getEmail().equals(updateRequest.getEmail())) {
            // Check if new email is already in use
            if (userRepository.existsByEmail(updateRequest.getEmail())) {
                throw new UserOperationException("Email is already in use");
            }
            user.setEmail(updateRequest.getEmail());
        }

        // Update password if provided
        if (StringUtils.hasText(updateRequest.getPassword())) {
            user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        }

        // Update roles if provided
        if (updateRequest.getRoles() != null && !updateRequest.getRoles().isEmpty()) {
            Set<Role> roles = new HashSet<>();

            updateRequest.getRoles().forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role ADMIN is not found."));
                        roles.add(adminRole);
                        break;
                    case "user":
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role USER is not found."));
                        roles.add(userRole);
                        break;
                    default:
                        throw new UserOperationException("Invalid role: " + role);
                }
            });

            user.setRoles(roles);
        }

        userRepository.save(user);
        return new UserResponse(user);
    }

    /**
     * Delete a user.
     *
     * @param id the user ID
     * @return response message
     */
    @Transactional
    public MessageResponse deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Check if user is trying to delete their own account and is an admin
        UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentUser.getId().equals(id) && user.hasAdminRole()) {
            throw new UserOperationException("Administrators cannot delete their own accounts");
        }

        // Delete refresh tokens first
        refreshTokenService.deleteByUserId(id);

        // Then delete the user
        userRepository.delete(user);

        return new MessageResponse("User deleted successfully");
    }
}