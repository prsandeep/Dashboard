package com.apisvn.service;


import com.apisvn.model.Repository;
import com.apisvn.model.User;
import com.apisvn.repository.RepositoryRepository;
import com.apisvn.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RepositoryService {

    private final RepositoryRepository repositoryRepository;
    private final UserRepository userRepository;
    private final Random random = new Random();

    @Autowired
    public RepositoryService(RepositoryRepository repositoryRepository, UserRepository userRepository) {
        this.repositoryRepository = repositoryRepository;
        this.userRepository = userRepository;
    }

    public List<Repository> getAllRepositories() {
        return repositoryRepository.findAll();
    }

    public Repository getRepositoryById(Long id) {
        return repositoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Repository not found with id: " + id));
    }

    public Optional<Repository> getRepositoryByName(String name) {
        return repositoryRepository.findByName(name);
    }

    public List<Repository> getRepositoriesByMigrationStatus(String migrationStatus) {
        return repositoryRepository.findByMigrationStatus(migrationStatus);
    }

    public List<Repository> getRepositoriesByBackupStatus(String backupStatus) {
        return repositoryRepository.findByBackupStatus(backupStatus);
    }

    public List<Repository> searchRepositories(String term) {
        return repositoryRepository.searchRepositories(term);
    }

    public List<Repository> getRepositoriesByMemberId(Long userId) {
        return repositoryRepository.findByMemberId(userId);
    }

    public List<Repository> getRepositoriesByMemberUsername(String username) {
        return repositoryRepository.findByMemberUsername(username);
    }

    @Transactional
    public Repository createRepository(Repository repository, List<Long> memberIds) {
        // Check if repository name already exists
        if (repositoryRepository.existsByName(repository.getName())) {
            throw new IllegalArgumentException("Repository name already exists: " + repository.getName());
        }

        // Set color code if not provided
        if (repository.getColorCode() == null || repository.getColorCode().isEmpty()) {
            repository.setColorCode(getRandomColorCode());
        }

        // Set created time and dates
        LocalDateTime now = LocalDateTime.now();
        repository.setCreatedAt(now);
        repository.setCreatedDate(now);
        repository.setLastCommit(now);

        // Initialize migration progress if needed
        if (repository.getMigrationStatus() == null) {
            repository.setMigrationStatus("Not Started");
            repository.setMigrationProgress(0);
        }

        // Add members if provided
        if (memberIds != null && !memberIds.isEmpty()) {
            Set<User> members = memberIds.stream()
                    .map(userId -> userRepository.findById(userId)
                            .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId)))
                    .collect(Collectors.toSet());
            repository.setMembers(members);
        }

        return repositoryRepository.save(repository);
    }

    @Transactional
    public Repository updateRepository(Long id, Repository repositoryDetails, List<Long> memberIds) {
        Repository repository = getRepositoryById(id);

        // Check if repository name is being changed and already exists
        if (!repository.getName().equals(repositoryDetails.getName()) &&
                repositoryRepository.existsByName(repositoryDetails.getName())) {
            throw new IllegalArgumentException("Repository name already exists: " + repositoryDetails.getName());
        }

        // Update repository details
        repository.setName(repositoryDetails.getName());
        repository.setDescription(repositoryDetails.getDescription());
        repository.setSize(repositoryDetails.getSize());
        repository.setBackupStatus(repositoryDetails.getBackupStatus());
        repository.setMigrationStatus(repositoryDetails.getMigrationStatus());
        repository.setMigrationProgress(repositoryDetails.getMigrationProgress());
        repository.setLastCommitBy("you (updated)");
        repository.setLastCommit(LocalDateTime.now());

        // Update members if provided
        if (memberIds != null) {
            Set<User> members = memberIds.stream()
                    .map(userId -> userRepository.findById(userId)
                            .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId)))
                    .collect(Collectors.toSet());
            repository.setMembers(members);
        }

        return repositoryRepository.save(repository);
    }

    @Transactional
    public void deleteRepository(Long id) {
        if (!repositoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Repository not found with id: " + id);
        }
        repositoryRepository.deleteById(id);
    }

    @Transactional
    public Repository updateRepositoryMembers(Long id, List<Long> memberIds) {
        Repository repository = getRepositoryById(id);

        // Update members
        Set<User> members = memberIds.stream()
                .map(userId -> userRepository.findById(userId)
                        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId)))
                .collect(Collectors.toSet());
        repository.setMembers(members);

        return repositoryRepository.save(repository);
    }

    @Transactional
    public Repository updateMigrationStatus(Long id, String status, Integer progress) {
        Repository repository = getRepositoryById(id);
        repository.setMigrationStatus(status);

        if (progress != null) {
            repository.setMigrationProgress(progress);
        } else {
            // Set default progress based on status
            switch (status) {
                case "Completed":
                    repository.setMigrationProgress(100);
                    break;
                case "Not Started":
                    repository.setMigrationProgress(0);
                    break;
                case "In Progress":
                    if (repository.getMigrationProgress() == 0) {
                        repository.setMigrationProgress(1);
                    }
                    break;
            }
        }

        return repositoryRepository.save(repository);
    }

    // Helper method to generate random color code for repository icon
    private String getRandomColorCode() {
        String[] colors = {
                "bg-teal-500", "bg-orange-500", "bg-indigo-500", "bg-purple-500",
                "bg-blue-500", "bg-green-500", "bg-pink-500", "bg-red-500"
        };
        return colors[random.nextInt(colors.length)];
    }
}