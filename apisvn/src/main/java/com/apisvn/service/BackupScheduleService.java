package com.apisvn.service;

import com.apisvn.model.BackupSchedule;
import com.apisvn.model.Repository;
import com.apisvn.repository.BackupScheduleRepository;
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
public class BackupScheduleService {

    private final BackupScheduleRepository backupScheduleRepository;
    private final RepositoryRepository repositoryRepository;

    @Autowired
    public BackupScheduleService(BackupScheduleRepository backupScheduleRepository, RepositoryRepository repositoryRepository) {
        this.backupScheduleRepository = backupScheduleRepository;
        this.repositoryRepository = repositoryRepository;
    }

    public List<BackupSchedule> getAllSchedules() {
        return backupScheduleRepository.findAll();
    }

    public BackupSchedule getScheduleById(Long id) {
        return backupScheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found with id: " + id));
    }

    public Optional<BackupSchedule> getScheduleByScheduleId(String scheduleId) {
        return backupScheduleRepository.findByScheduleId(scheduleId);
    }

    public List<BackupSchedule> getSchedulesByType(String type) {
        return backupScheduleRepository.findByType(type);
    }

    public List<BackupSchedule> getSchedulesByFrequency(String frequency) {
        return backupScheduleRepository.findByFrequency(frequency);
    }

    public List<BackupSchedule> getSchedulesByStatus(String status) {
        return backupScheduleRepository.findByStatus(status);
    }

    public List<BackupSchedule> getSchedulesByRepositoryId(Long repositoryId) {
        return backupScheduleRepository.findByRepositoryId(repositoryId);
    }

    public Optional<BackupSchedule> getNextScheduledBackup() {
        List<BackupSchedule> schedules = backupScheduleRepository.findNextScheduledBackup(PageRequest.of(0, 1));
        return schedules.isEmpty() ? Optional.empty() : Optional.of(schedules.get(0));
    }

    @Transactional
    public BackupSchedule createSchedule(BackupSchedule schedule, List<Long> repositoryIds) {
        // Set creation time
        schedule.setCreatedAt(LocalDateTime.now());

        // Associate repositories if IDs provided
        if (repositoryIds != null && !repositoryIds.isEmpty()) {
            Set<Repository> repositories = repositoryIds.stream()
                    .map(repoId -> repositoryRepository.findById(repoId)
                            .orElseThrow(() -> new EntityNotFoundException("Repository not found with id: " + repoId)))
                    .collect(Collectors.toSet());
            schedule.setRepositories(repositories);
        } else if (schedule.getRepositories() != null && schedule.getRepositories().stream()
                .anyMatch(r -> r.getName().equals("All repositories"))) {
            // If "All repositories" is specified
            List<Repository> allRepositories = repositoryRepository.findAll();
            schedule.setRepositories(new HashSet<>(allRepositories));
        }

        return backupScheduleRepository.save(schedule);
    }

    @Transactional
    public BackupSchedule updateSchedule(Long id, BackupSchedule scheduleDetails, List<Long> repositoryIds) {
        BackupSchedule schedule = getScheduleById(id);

        // Update schedule details
        schedule.setName(scheduleDetails.getName());
        schedule.setType(scheduleDetails.getType());
        schedule.setFrequency(scheduleDetails.getFrequency());
        schedule.setTime(scheduleDetails.getTime());
        schedule.setRetention(scheduleDetails.getRetention());
        schedule.setStatus(scheduleDetails.getStatus());

        // Update repositories if provided
        if (repositoryIds != null) {
            Set<Repository> repositories = repositoryIds.stream()
                    .map(repoId -> repositoryRepository.findById(repoId)
                            .orElseThrow(() -> new EntityNotFoundException("Repository not found with id: " + repoId)))
                    .collect(Collectors.toSet());
            schedule.setRepositories(repositories);
        }

        return backupScheduleRepository.save(schedule);
    }

    @Transactional
    public void deleteSchedule(Long id) {
        if (!backupScheduleRepository.existsById(id)) {
            throw new EntityNotFoundException("Schedule not found with id: " + id);
        }
        backupScheduleRepository.deleteById(id);
    }

    @Transactional
    public BackupSchedule toggleScheduleStatus(Long id) {
        BackupSchedule schedule = getScheduleById(id);

        // Toggle status
        if ("Active".equals(schedule.getStatus())) {
            schedule.setStatus("Inactive");
        } else {
            schedule.setStatus("Active");
        }

        return backupScheduleRepository.save(schedule);
    }
}
