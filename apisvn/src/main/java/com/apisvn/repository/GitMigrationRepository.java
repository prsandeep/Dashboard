package com.apisvn.repository;


import com.apisvn.model.GitMigration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GitMigrationRepository extends JpaRepository<GitMigration, Long> {

    List<GitMigration> findByStatus(String status);

    List<GitMigration> findByAssignedTo(String assignedTo);

    List<GitMigration> findByRepositoryId(Long repositoryId);

    List<GitMigration> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);
}