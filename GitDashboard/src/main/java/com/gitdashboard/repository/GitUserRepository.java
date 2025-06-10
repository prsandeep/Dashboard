// Repository interfaces
// src/main/java/com/gitdashboard/repository/GitUserRepository.java
package com.gitdashboard.repository;

import com.gitdashboard.model.GitUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface GitUserRepository extends JpaRepository<GitUser, Long> {
    Optional<GitUser> findByUsername(String username);
    Optional<GitUser> findByEmployeeId(String employeeId);
    List<GitUser> findByRole(GitUser.UserRole role);

    @Query("SELECT u.role, COUNT(u) FROM GitUser u GROUP BY u.role")
    List<Object[]> countByRole();
}