package com.apisvn.util;

import com.apisvn.dto.*;
import com.apisvn.model.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EntityDTOMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a");

    // User mappers
    public UserDTO toUserDTO(User user) {
        if (user == null) return null;

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
        dto.setGroup(user.getGroup());
        dto.setColorCode(user.getColorCode());
        dto.setInitials(user.getInitials());

        // Format last activity
        if (user.getLastActivity() != null) {
            dto.setLastActivity(formatTimeAgo(user.getLastActivity()));
        }

        return dto;
    }

    public User toUserEntity(UserDTO dto) {
        if (dto == null) return null;

        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setStatus(dto.getStatus());
        user.setGroup(dto.getGroup());
        user.setColorCode(dto.getColorCode());
        user.setInitials(dto.getInitials());

        return user;
    }

    // Repository mappers
    public RepositoryDTO toRepositoryDTO(Repository repository) {
        if (repository == null) return null;

        RepositoryDTO dto = new RepositoryDTO();
        dto.setId(repository.getId());
        dto.setName(repository.getName());
        dto.setDescription(repository.getDescription());
        dto.setSize(repository.getSize());
        dto.setBackupStatus(repository.getBackupStatus());
        dto.setMigrationStatus(repository.getMigrationStatus());
        dto.setMigrationProgress(repository.getMigrationProgress());
        dto.setColorCode(repository.getColorCode());

        // Format dates
        if (repository.getCreatedDate() != null) {
            dto.setCreatedDate(repository.getCreatedDate().format(DATE_FORMATTER));
        }

        if (repository.getLastCommit() != null) {
            dto.setLastCommit(formatTimeAgo(repository.getLastCommit()));
        }

        dto.setLastCommitBy(repository.getLastCommitBy());

        // Map members
        if (repository.getMembers() != null && !repository.getMembers().isEmpty()) {
            dto.setMembers(repository.getMembers().stream()
                    .map(this::toUserDTO)
                    .collect(Collectors.toList()));

            dto.setMemberIds(repository.getMembers().stream()
                    .map(User::getId)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public Repository toRepositoryEntity(RepositoryDTO dto) {
        if (dto == null) return null;

        Repository repository = new Repository();
        repository.setId(dto.getId());
        repository.setName(dto.getName());
        repository.setDescription(dto.getDescription());
        repository.setSize(dto.getSize());
        repository.setBackupStatus(dto.getBackupStatus());
        repository.setMigrationStatus(dto.getMigrationStatus());
        repository.setMigrationProgress(dto.getMigrationProgress());
        repository.setColorCode(dto.getColorCode());
        repository.setLastCommitBy(dto.getLastCommitBy());

        return repository;
    }

    // Git Migration mappers
    public GitMigrationDTO toGitMigrationDTO(GitMigration migration) {
        if (migration == null) return null;

        GitMigrationDTO dto = new GitMigrationDTO();
        dto.setId(migration.getId());
        dto.setName(migration.getName());
        dto.setDescription(migration.getDescription());
        dto.setSize(migration.getSize());
        dto.setStatus(migration.getStatus());
        dto.setProgress(migration.getProgress());
        dto.setEstimatedTime(migration.getEstimatedTime());
        dto.setAssignedTo(migration.getAssignedTo());
        dto.setColorCode(migration.getColorCode());

        // Format dates
        if (migration.getStartedDate() != null) {
            dto.setStartedDate(migration.getStartedDate().format(DATE_FORMATTER));
        }

        if (migration.getCompletedDate() != null) {
            dto.setCompletedDate(migration.getCompletedDate().format(DATE_FORMATTER));
        }

        // Set repository ID if available
        if (migration.getRepository() != null) {
            dto.setRepositoryId(migration.getRepository().getId());
        }

        return dto;
    }

    public GitMigration toGitMigrationEntity(GitMigrationDTO dto) {
        if (dto == null) return null;

        GitMigration migration = new GitMigration();
        migration.setId(dto.getId());
        migration.setName(dto.getName());
        migration.setDescription(dto.getDescription());
        migration.setSize(dto.getSize());
        migration.setStatus(dto.getStatus());
        migration.setProgress(dto.getProgress());
        migration.setEstimatedTime(dto.getEstimatedTime());
        migration.setAssignedTo(dto.getAssignedTo());
        migration.setColorCode(dto.getColorCode());

        return migration;
    }

    // Backup mappers
    public BackupDTO toBackupDTO(Backup backup) {
        if (backup == null) return null;

        BackupDTO dto = new BackupDTO();
        dto.setId(backup.getId());
        dto.setBackupId(backup.getBackupId());
        dto.setType(backup.getType());
        dto.setStatus(backup.getStatus());
        dto.setSize(backup.getSize());
        dto.setDuration(backup.getDuration());
        dto.setInitiatedBy(backup.getInitiatedBy());
        dto.setNotes(backup.getNotes());
        dto.setLogs(backup.getLogs());

        // Format date
        if (backup.getDate() != null) {
            dto.setDate(backup.getDate().format(DATE_FORMATTER));
        }

        // Map repositories
        if (backup.getRepositories() != null && !backup.getRepositories().isEmpty()) {
            dto.setRepositoryIds(backup.getRepositories().stream()
                    .map(Repository::getId)
                    .collect(Collectors.toList()));

            // Create a readable string of repository names
            String repoNames = backup.getRepositories().stream()
                    .map(Repository::getName)
                    .collect(Collectors.joining(", "));

            dto.setRepos(repoNames.isEmpty() ? "All repositories" : repoNames);
        } else {
            dto.setRepos("All repositories");
        }

        return dto;
    }

    public Backup toBackupEntity(BackupDTO dto) {
        if (dto == null) return null;

        Backup backup = new Backup();
        backup.setId(dto.getId());
        backup.setBackupId(dto.getBackupId());
        backup.setType(dto.getType());
        backup.setStatus(dto.getStatus());
        backup.setSize(dto.getSize());
        backup.setDuration(dto.getDuration());
        backup.setInitiatedBy(dto.getInitiatedBy());
        backup.setNotes(dto.getNotes());
        backup.setLogs(dto.getLogs());

        return backup;
    }

    // Backup Schedule mappers
    public BackupScheduleDTO toBackupScheduleDTO(BackupSchedule schedule) {
        if (schedule == null) return null;

        BackupScheduleDTO dto = new BackupScheduleDTO();
        dto.setId(schedule.getId());
        dto.setScheduleId(schedule.getScheduleId());
        dto.setName(schedule.getName());
        dto.setType(schedule.getType());
        dto.setFrequency(schedule.getFrequency());
        dto.setTime(schedule.getTime());
        dto.setRetention(schedule.getRetention());
        dto.setStatus(schedule.getStatus());

        // Map repositories
        if (schedule.getRepositories() != null && !schedule.getRepositories().isEmpty()) {
            dto.setRepositoryIds(schedule.getRepositories().stream()
                    .map(Repository::getId)
                    .collect(Collectors.toList()));

            // Create a readable string of repository names
            String repoNames = schedule.getRepositories().stream()
                    .map(Repository::getName)
                    .collect(Collectors.joining(", "));

            dto.setRepos(repoNames.isEmpty() ? "All repositories" : repoNames);
        } else {
            dto.setRepos("All repositories");
        }

        return dto;
    }

    public BackupSchedule toBackupScheduleEntity(BackupScheduleDTO dto) {
        if (dto == null) return null;

        BackupSchedule schedule = new BackupSchedule();
        schedule.setId(dto.getId());
        schedule.setScheduleId(dto.getScheduleId());
        schedule.setName(dto.getName());
        schedule.setType(dto.getType());
        schedule.setFrequency(dto.getFrequency());
        schedule.setTime(dto.getTime());
        schedule.setRetention(dto.getRetention());
        schedule.setStatus(dto.getStatus());

        return schedule;
    }

    // Helper method to format time ago
    private String formatTimeAgo(LocalDateTime dateTime) {
        if (dateTime == null) return "";

        LocalDateTime now = LocalDateTime.now();
        long minutesAgo = java.time.Duration.between(dateTime, now).toMinutes();

        if (minutesAgo < 1) {
            return "Just now";
        } else if (minutesAgo < 60) {
            return minutesAgo + " min ago";
        } else if (minutesAgo < 1440) {
            long hoursAgo = minutesAgo / 60;
            return hoursAgo + (hoursAgo == 1 ? " hour ago" : " hours ago");
        } else if (minutesAgo < 10080) {
            long daysAgo = minutesAgo / 1440;
            return daysAgo + (daysAgo == 1 ? " day ago" : " days ago");
        } else {
            return dateTime.format(DATE_FORMATTER);
        }
    }

    // Helper method to convert DTO list to entity list
    public <T, U> List<U> mapList(List<T> source, java.util.function.Function<T, U> mapper) {
        if (source == null) return null;
        return source.stream().map(mapper).collect(Collectors.toList());
    }
}