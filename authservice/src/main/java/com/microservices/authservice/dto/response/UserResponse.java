package com.microservices.authservice.dto.response;

import com.microservices.authservice.model.Role;
import com.microservices.authservice.model.User;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Data Transfer Object for user responses.
 */
@Data
public class UserResponse {
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
     * Create a UserResponse from a User entity.
     *
     * @param user the user entity
     */
    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.roles = mapRolesToStrings(user.getRoles());
    }

    /**
     * Map role entities to role name strings.
     *
     * @param roles set of role entities
     * @return list of role name strings
     */
    private List<String> mapRolesToStrings(Set<Role> roles) {
        return roles.stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());
    }
}