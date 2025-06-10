package com.apisvn.service;


import com.apisvn.model.GitMigration;
import com.apisvn.model.Repository;
import com.apisvn.repository.GitMigrationRepository;
import com.apisvn.repository.RepositoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class GitMigrationService {

    private final GitMigrationRepository gitMigrationRepository;
    private final RepositoryRepository repositoryRepository;
    private final Random random = new Random();

    @Autowired
    public GitMigrationService(GitMigrationRepository gitMigrationRepository, RepositoryRepository repositoryRepository) {
        this.gitMigrationRepository = gitMigrationRepository;
        this.repositoryRepository = repositoryRepository;
    }

    public List<GitMigration> getAllMigrations() {
        return gitMigrationRepository.findAll();
    }

    public GitMigration getMigrationById(Long id) {
        return gitMigrationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Migration not found with id: " + id));
    }

    public List<GitMigration> getMigrationsByStatus(String status) {
        return gitMigrationRepository.findByStatus(status);
    }

    public List<GitMigration> getMigrationsByAssignedTo(String assignedTo) {
        return gitMigrationRepository.findByAssignedTo(assignedTo);
    }

    public List<GitMigration> getMigrationsByRepositoryId(Long repositoryId) {
        return gitMigrationRepository.findByRepositoryId(repositoryId);
    }

    public List<GitMigration> searchMigrations(String term) {
        return gitMigrationRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(term, term);
    }

    @Transactional
    public GitMigration createMigration(GitMigration migration, Long repositoryId) {
        // Set color code if not provided
        if (migration.getColorCode() == null || migration.getColorCode().isEmpty()) {
            migration.setColorCode(getRandomColorCode());
        }

        // Set creation time
        migration.setCreatedAt(LocalDateTime.now());

        // Set progress based on status if not provided
        if (migration.getProgress() == null) {
            switch (migration.getStatus()) {
                case "Completed":
                    migration.setProgress(100);
                    break;
                case "Not Started":
                    migration.setProgress(0);
                    break;
                case "In Progress":
                    migration.setProgress(1);
                    break;
                default:
                    migration.setProgress(0);
            }
        }

        // Set dates based on status
        if ("In Progress".equals(migration.getStatus()) && migration.getStartedDate() == null) {
            migration.setStartedDate(LocalDateTime.now());
        }

        if ("Completed".equals(migration.getStatus()) && migration.getCompletedDate() == null) {
            migration.setCompletedDate(LocalDateTime.now());
        }

        // Associate with repository if provided
        if (repositoryId != null) {
            Repository repository = repositoryRepository.findById(repositoryId)
                    .orElseThrow(() -> new EntityNotFoundException("Repository not found with id: " + repositoryId));
            migration.setRepository(repository);

            // Update repository migration status
            repository.setMigrationStatus(migration.getStatus());
            repository.setMigrationProgress(migration.getProgress());
            repositoryRepository.save(repository);
        }

        return gitMigrationRepository.save(migration);
    }

    @Transactional
    public GitMigration updateMigration(Long id, GitMigration migrationDetails) {
        GitMigration migration = getMigrationById(id);

        // Update migration details
        migration.setName(migrationDetails.getName());
        migration.setDescription(migrationDetails.getDescription());
        migration.setSize(migrationDetails.getSize());
        migration.setEstimatedTime(migrationDetails.getEstimatedTime());
        migration.setAssignedTo(migrationDetails.getAssignedTo());

        // Update status and related fields
        boolean statusChanged = !migration.getStatus().equals(migrationDetails.getStatus());
        migration.setStatus(migrationDetails.getStatus());

        if (migrationDetails.getProgress() != null) {
            migration.setProgress(migrationDetails.getProgress());
        } else if (statusChanged) {
            // Set default progress based on new status
            switch (migrationDetails.getStatus()) {
                case "Completed":
                    migration.setProgress(100);
                    break;
                case "Not Started":
                    migration.setProgress(0);
                    break;
                case "In Progress":
                    if (migration.getProgress() == 0) {
                        migration.setProgress(1);
                    }
                    break;
            }
        }

        // Update dates based on status changes
        if (statusChanged) {
            if ("In Progress".equals(migrationDetails.getStatus()) && migration.getStartedDate() == null) {
                migration.setStartedDate(LocalDateTime.now());
            }

            if ("Completed".equals(migrationDetails.getStatus()) && migration.getCompletedDate() == null) {
                migration.setCompletedDate(LocalDateTime.now());
            }

            // If the status changes to Failed, keep the startedDate but clear completedDate
            if ("Failed".equals(migrationDetails.getStatus())) {
                migration.setCompletedDate(null);
            }
        }

        // Update repository status if associated
        if (migration.getRepository() != null) {
            Repository repository = migration.getRepository();
            repository.setMigrationStatus(migration.getStatus());
            repository.setMigrationProgress(migration.getProgress());
            repositoryRepository.save(repository);
        }

        return gitMigrationRepository.save(migration);
    }

    @Transactional
    public void deleteMigration(Long id) {
        if (!gitMigrationRepository.existsById(id)) {
            throw new EntityNotFoundException("Migration not found with id: " + id);
        }
        gitMigrationRepository.deleteById(id);
    }

    @Transactional
    public GitMigration startMigration(Long id) {
        GitMigration migration = getMigrationById(id);
        migration.setStatus("In Progress");
        migration.setStartedDate(LocalDateTime.now());

        if (migration.getProgress() == 0) {
            migration.setProgress(1);
        }

        // Update repository status if associated
        if (migration.getRepository() != null) {
            Repository repository = migration.getRepository();
            repository.setMigrationStatus("In Progress");
            repository.setMigrationProgress(migration.getProgress());
            repositoryRepository.save(repository);
        }

        return gitMigrationRepository.save(migration);
    }

    @Transactional
    public GitMigration pauseMigration(Long id) {
        GitMigration migration = getMigrationById(id);
        migration.setStatus("Not Started");

        // Update repository status if associated
        if (migration.getRepository() != null) {
            Repository repository = migration.getRepository();
            repository.setMigrationStatus("Not Started");
            repositoryRepository.save(repository);
        }

        return gitMigrationRepository.save(migration);
    }

    @Transactional
    public GitMigration completeMigration(Long id) {
        GitMigration migration = getMigrationById(id);
        migration.setStatus("Completed");
        migration.setProgress(100);
        migration.setCompletedDate(LocalDateTime.now());

        // Update repository status if associated
        if (migration.getRepository() != null) {
            Repository repository = migration.getRepository();
            repository.setMigrationStatus("Completed");
            repository.setMigrationProgress(100);
            repositoryRepository.save(repository);
        }

        return gitMigrationRepository.save(migration);
    }

    @Transactional
    public GitMigration retryMigration(Long id) {
        GitMigration migration = getMigrationById(id);
        migration.setStatus("In Progress");
        migration.setStartedDate(LocalDateTime.now());
        migration.setCompletedDate(null);

        // Reduce progress by 10% for retry
        int newProgress = Math.max(1, migration.getProgress() - 10);
        migration.setProgress(newProgress);

        // Update repository status if associated
        if (migration.getRepository() != null) {
            Repository repository = migration.getRepository();
            repository.setMigrationStatus("In Progress");
            repository.setMigrationProgress(newProgress);
            repositoryRepository.save(repository);
        }

        return gitMigrationRepository.save(migration);
    }

    // Helper method to generate random color code
    private String getRandomColorCode() {
        String[] colors = {
                "bg-teal-500", "bg-orange-500", "bg-indigo-500", "bg-purple-500",
                "bg-blue-500", "bg-green-500", "bg-pink-500", "bg-red-500"
        };
        return colors[random.nextInt(colors.length)];
    }
}
