// src/main/java/com/gitdashboard/repository/GitRepositoryRepository.java
package com.gitdashboard.repository;

import com.gitdashboard.model.GitRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GitRepositoryRepository extends JpaRepository<GitRepository, Long> {
    List<GitRepository> findByDepartment(String department);
    List<GitRepository> findByProjectNameContainingIgnoreCase(String projectName);

    @Query("SELECT r.department, COUNT(r) FROM GitRepository r GROUP BY r.department")
    List<Object[]> countByDepartment();
}