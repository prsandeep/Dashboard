package com.apisvn.service;




import com.apisvn.model.Backup;
import com.apisvn.model.Repository;
import com.apisvn.repository.BackupRepository;
import com.apisvn.repository.RepositoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BackupService {

    private final BackupRepository backupRepository;
    private final RepositoryRepository repositoryRepository;

    @Autowired
    public BackupService(BackupRepository backupRepository, RepositoryRepository repositoryRepository) {
        this.backupRepository = backupRepository;
        this.repositoryRepository = repositoryRepository;
    }

    public List<Backup> getAllBackups() {
        return backupRepository.findAll();
    }

    public Backup getBackupById(Long id) {
        return backupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Backup not found with id: " + id));
    }

    public Optional<Backup> getBackupByBackupId(String backupId) {
        return backupRepository.findByBackupId(backupId);
    }

    public List<Backup> getBackupsByType(String type) {
        return backupRepository.findByType(type);
    }

    public List<Backup> getBackupsByStatus(String status) {
        return backupRepository.findByStatus(status);
    }

    public List<Backup> getBackupsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return backupRepository.findByDateBetween(startDate, endDate);
    }

    public List<Backup> getBackupsByRepositoryId(Long repositoryId) {
        return backupRepository.findByRepositoryId(repositoryId);
    }

    public Optional<Backup> getLastFullBackup() {
        List<Backup> backups = backupRepository.findLastFullBackup(PageRequest.of(0, 1));
        return backups.isEmpty() ? Optional.empty() : Optional.of(backups.get(0));
    }

    @Transactional
    public Backup createBackup(Backup backup, List<Long> repositoryIds) {
        // Set creation time
        LocalDateTime now = LocalDateTime.now();
        backup.setCreatedAt(now);
        backup.setDate(now);

        // Set initial status to In Progress
        backup.setStatus("In Progress");
        backup.setDuration("0m");
        backup.setLogs("Backup initiated...");

        // Associate repositories if IDs provided
        if (repositoryIds != null && !repositoryIds.isEmpty()) {
            Set<Repository> repositories = repositoryIds.stream()
                    .map(repoId -> repositoryRepository.findById(repoId)
                            .orElseThrow(() -> new EntityNotFoundException("Repository not found with id: " + repoId)))
                    .collect(Collectors.toSet());
            backup.setRepositories(repositories);

            // Update backup status for repositories
            for (Repository repository : repositories) {
                repository.setBackupStatus("In Progress");
                repositoryRepository.save(repository);
            }
        } else if ("All repositories".equals(backup.getLogs())) {
            // If "All repositories" is specified
            List<Repository> allRepositories = repositoryRepository.findAll();
            backup.setRepositories(new HashSet<>(allRepositories));

            // Update backup status for all repositories
            for (Repository repository : allRepositories) {
                repository.setBackupStatus("In Progress");
                repositoryRepository.save(repository);
            }
        }

        // Save the backup
        Backup savedBackup = backupRepository.save(backup);

        // Simulate backup completion in a real application (in this case just update status)
        // In a real application, this would be handled by a separate thread or job scheduler
        completeBackupAsync(savedBackup.getId());

        return savedBackup;
    }

    @Transactional
    public void deleteBackup(Long id) {
        if (!backupRepository.existsById(id)) {
            throw new EntityNotFoundException("Backup not found with id: " + id);
        }
        backupRepository.deleteById(id);
    }

    @Transactional
    public Backup retryBackup(Long id) {
        Backup backup = getBackupById(id);

        if (!"Failed".equals(backup.getStatus())) {
            throw new IllegalStateException("Only failed backups can be retried");
        }

        // Update backup for retry
        backup.setStatus("In Progress");
        backup.setDate(LocalDateTime.now());
        backup.setLogs("Retry initiated...");

        // Update backup status for all related repositories
        for (Repository repository : backup.getRepositories()) {
            repository.setBackupStatus("In Progress");
            repositoryRepository.save(repository);
        }

        Backup savedBackup = backupRepository.save(backup);

        // Simulate backup completion in a real application
        completeBackupAsync(savedBackup.getId());

        return savedBackup;
    }

    // Simulates asynchronous backup completion
    // In a real application, this would be a separate job or task
    private void completeBackupAsync(Long backupId) {
        // In a real implementation, this would be run in a separate thread or scheduler
        // For this demo, we'll just do it directly for simplicity

        try {
            // Simulate processing delay
            Thread.sleep(5000); // 5 seconds delay

            // Complete the backup
            Backup backup = getBackupById(backupId);
            backup.setStatus("Complete");

            // Set appropriate duration based on type
            if ("Full".equals(backup.getType())) {
                backup.setDuration("1h 05m");
                backup.setSize("15.8 GB");
            } else {
                backup.setDuration("15m");
                backup.setSize("1.2 GB");
            }

            backup.setLogs("Backup completed successfully with no errors.");

            // Update backup status for all related repositories
            for (Repository repository : backup.getRepositories()) {
                repository.setBackupStatus("Complete");
                repositoryRepository.save(repository);
            }

            backupRepository.save(backup);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            // Handle errors - in a real application, we'd log this and possibly retry
            try {
                Backup backup = getBackupById(backupId);
                backup.setStatus("Failed");
                backup.setLogs("Backup failed: " + e.getMessage());

                // Update backup status for all related repositories
                for (Repository repository : backup.getRepositories()) {
                    repository.setBackupStatus("Failed");
                    repositoryRepository.save(repository);
                }

                backupRepository.save(backup);
            } catch (Exception ex) {
                // Log error
                System.err.println("Error updating failed backup: " + ex.getMessage());
            }
        }
    }
}