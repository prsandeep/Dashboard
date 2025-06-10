// src/main/java/com/gitdashboard/controller/GitBackupController.java
package com.gitdashboard.controller;

import com.gitdashboard.dto.GitBackupDto;
import com.gitdashboard.model.GitBackup;
import com.gitdashboard.service.GitBackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/git/backups")
public class GitBackupController {
    @Autowired
    private GitBackupService gitBackupService;

    @GetMapping
    public ResponseEntity<List<GitBackupDto>> getAllBackups() {
        return ResponseEntity.ok(gitBackupService.getAllBackups());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GitBackupDto> getBackupById(@PathVariable Long id) {
        return ResponseEntity.ok(gitBackupService.getBackupById(id));
    }

    @GetMapping("/repository/{repositoryId}")
    public ResponseEntity<GitBackupDto> getBackupByRepositoryId(@PathVariable Long repositoryId) {
        return ResponseEntity.ok(gitBackupService.getBackupByRepositoryId(repositoryId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<GitBackupDto>> getBackupsByStatus(@PathVariable String status) {
        return ResponseEntity
                .ok(gitBackupService.getBackupsByStatus(GitBackup.BackupStatus.valueOf(status.toUpperCase())));
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getBackupCountsByStatus() {
        return ResponseEntity.ok(gitBackupService.getBackupCountsByStatus());
    }

    @PostMapping
    public ResponseEntity<GitBackupDto> createBackup(@RequestBody GitBackupDto backupDto) {
        return new ResponseEntity<>(gitBackupService.createBackup(backupDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GitBackupDto> updateBackup(@PathVariable Long id, @RequestBody GitBackupDto backupDto) {
        return ResponseEntity.ok(gitBackupService.updateBackup(id, backupDto));
    }

    @PostMapping("/run/{repositoryId}")
    public ResponseEntity<GitBackupDto> runBackup(@PathVariable Long repositoryId) {
        return ResponseEntity.ok(gitBackupService.runBackup(repositoryId));
    }
}