package com.microservices.authservice.repository;

import com.microservices.authservice.model.ERole;
import com.microservices.authservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Role entity, providing methods to interact with the roles table.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    /**
     * Find a role by its name.
     * @param name the enum value of the role
     * @return an Optional containing the role if found, empty otherwise
     */
    Optional<Role> findByName(ERole name);
}