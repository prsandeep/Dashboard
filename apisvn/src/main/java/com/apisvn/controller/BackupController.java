package com.apisvn.controller;


import com.apisvn.model.Backup;
import com.apisvn.service.BackupService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/svn/backups")
public class BackupController {

    private final BackupService backupService;

    @Autowired
    public BackupController(BackupService backupService) {
        this.backupService = backupService;
    }

    @GetMapping
    public ResponseEntity<List<Backup>> getAllBackups(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(value = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(value = "repositoryId", required = false) Long repositoryId) {

        List<Backup> backups;

        if (type != null && !type.equals("All")) {
            backups = backupService.getBackupsByType(type);
        } else if (status != null && !status.equals("All")) {
            backups = backupService.getBackupsByStatus(status);
        } else if (startDate != null && endDate != null) {
            backups = backupService.getBackupsByDateRange(startDate, endDate);
        } else if (repositoryId != null) {
            backups = backupService.getBackupsByRepositoryId(repositoryId);
        } else {
            backups = backupService.getAllBackups();
        }

        return ResponseEntity.ok(backups);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Backup> getBackupById(@PathVariable Long id) {
        Backup backup = backupService.getBackupById(id);
        return ResponseEntity.ok(backup);
    }

    @GetMapping("/backup-id/{backupId}")
    public ResponseEntity<Backup> getBackupByBackupId(@PathVariable String backupId) {
        Optional<Backup> backup = backupService.getBackupByBackupId(backupId);
        return backup.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/last-full")
    public ResponseEntity<Backup> getLastFullBackup() {
        Optional<Backup> backup = backupService.getLastFullBackup();
        return backup.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Backup> createBackup(
            @Valid @RequestBody Backup backup,
            @RequestParam(value = "repositoryIds", required = false) List<Long> repositoryIds) {

        Backup createdBackup = backupService.createBackup(backup, repositoryIds);
        return new ResponseEntity<>(createdBackup, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteBackup(@PathVariable Long id) {
        backupService.deleteBackup(id);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/retry")
    public ResponseEntity<Backup> retryBackup(@PathVariable Long id) {
        Backup updatedBackup = backupService.retryBackup(id);
        return ResponseEntity.ok(updatedBackup);
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getBackupStatistics() {
        List<Backup> allBackups = backupService.getAllBackups();

        int totalBackups = allBackups.size();
        long completed = allBackups.stream().filter(b -> "Complete".equals(b.getStatus())).count();
        long inProgress = allBackups.stream().filter(b -> "In Progress".equals(b.getStatus())).count();
        long failed = allBackups.stream().filter(b -> "Failed".equals(b.getStatus())).count();

        // Calculate total storage
        double totalStorage = allBackups.stream()
                .filter(b -> "Complete".equals(b.getStatus()))
                .filter(b -> b.getSize() != null && !b.getSize().isEmpty())
                .mapToDouble(b -> {
                    try {
                        String sizeStr = b.getSize().replaceAll("[^\\d.]", "");
                        return Double.parseDouble(sizeStr);
                    } catch (NumberFormatException e) {
                        return 0.0;
                    }
                })
                .sum();

        // Get last full backup
        Optional<Backup> lastFullBackup = backupService.getLastFullBackup();
        String lastFullBackupDate = lastFullBackup.map(b -> b.getDate().toString()).orElse("None");

        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalBackups", totalBackups);
        statistics.put("completedBackups", completed);
        statistics.put("inProgressBackups", inProgress);
        statistics.put("failedBackups", failed);
        statistics.put("totalStorageGB", Math.round(totalStorage * 10) / 10.0);
        statistics.put("lastFullBackupDate", lastFullBackupDate);

        return ResponseEntity.ok(statistics);
    }
}
