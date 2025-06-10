package com.apisvn.controller;


import com.apisvn.model.GitMigration;
import com.apisvn.service.GitMigrationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/svn/migrations")
public class GitMigrationController {

    private final GitMigrationService migrationService;

    @Autowired
    public GitMigrationController(GitMigrationService migrationService) {
        this.migrationService = migrationService;
    }

    @GetMapping
    public ResponseEntity<List<GitMigration>> getAllMigrations(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "assignedTo", required = false) String assignedTo,
            @RequestParam(value = "repositoryId", required = false) Long repositoryId,
            @RequestParam(value = "search", required = false) String search) {

        List<GitMigration> migrations;

        if (search != null && !search.isEmpty()) {
            migrations = migrationService.searchMigrations(search);
        } else if (status != null && !status.equals("All")) {
            migrations = migrationService.getMigrationsByStatus(status);
        } else if (assignedTo != null && !assignedTo.equals("All")) {
            migrations = migrationService.getMigrationsByAssignedTo(assignedTo);
        } else if (repositoryId != null) {
            migrations = migrationService.getMigrationsByRepositoryId(repositoryId);
        } else {
            migrations = migrationService.getAllMigrations();
        }

        return ResponseEntity.ok(migrations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GitMigration> getMigrationById(@PathVariable Long id) {
        GitMigration migration = migrationService.getMigrationById(id);
        return ResponseEntity.ok(migration);
    }

    @PostMapping
    public ResponseEntity<GitMigration> createMigration(
            @Valid @RequestBody GitMigration migration,
            @RequestParam(value = "repositoryId", required = false) Long repositoryId) {

        GitMigration createdMigration = migrationService.createMigration(migration, repositoryId);
        return new ResponseEntity<>(createdMigration, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GitMigration> updateMigration(
            @PathVariable Long id,
            @Valid @RequestBody GitMigration migrationDetails) {

        GitMigration updatedMigration = migrationService.updateMigration(id, migrationDetails);
        return ResponseEntity.ok(updatedMigration);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteMigration(@PathVariable Long id) {
        migrationService.deleteMigration(id);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<GitMigration> startMigration(@PathVariable Long id) {
        GitMigration updatedMigration = migrationService.startMigration(id);
        return ResponseEntity.ok(updatedMigration);
    }

    @PostMapping("/{id}/pause")
    public ResponseEntity<GitMigration> pauseMigration(@PathVariable Long id) {
        GitMigration updatedMigration = migrationService.pauseMigration(id);
        return ResponseEntity.ok(updatedMigration);
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<GitMigration> completeMigration(@PathVariable Long id) {
        GitMigration updatedMigration = migrationService.completeMigration(id);
        return ResponseEntity.ok(updatedMigration);
    }

    @PostMapping("/{id}/retry")
    public ResponseEntity<GitMigration> retryMigration(@PathVariable Long id) {
        GitMigration updatedMigration = migrationService.retryMigration(id);
        return ResponseEntity.ok(updatedMigration);
    }
}
