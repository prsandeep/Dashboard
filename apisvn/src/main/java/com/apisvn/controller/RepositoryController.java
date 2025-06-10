package com.apisvn.controller;


import com.apisvn.model.Repository;
import com.apisvn.service.RepositoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/svn/repositories")
public class RepositoryController {

    private final RepositoryService repositoryService;

    @Autowired
    public RepositoryController(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @GetMapping
    public ResponseEntity<List<Repository>> getAllRepositories(
            @RequestParam(value = "migrationStatus", required = false) String migrationStatus,
            @RequestParam(value = "backupStatus", required = false) String backupStatus,
            @RequestParam(value = "member", required = false) String memberUsername,
            @RequestParam(value = "search", required = false) String search) {

        List<Repository> repositories;

        if (search != null && !search.isEmpty()) {
            repositories = repositoryService.searchRepositories(search);
        } else if (migrationStatus != null && !migrationStatus.equals("All")) {
            repositories = repositoryService.getRepositoriesByMigrationStatus(migrationStatus);
        } else if (backupStatus != null && !backupStatus.equals("All")) {
            repositories = repositoryService.getRepositoriesByBackupStatus(backupStatus);
        } else if (memberUsername != null && !memberUsername.equals("All")) {
            repositories = repositoryService.getRepositoriesByMemberUsername(memberUsername);
        } else {
            repositories = repositoryService.getAllRepositories();
        }

        return ResponseEntity.ok(repositories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Repository> getRepositoryById(@PathVariable Long id) {
        Repository repository = repositoryService.getRepositoryById(id);
        return ResponseEntity.ok(repository);
    }

    @PostMapping
    public ResponseEntity<Repository> createRepository(
            @Valid @RequestBody Repository repository,
            @RequestParam(value = "memberIds", required = false) List<Long> memberIds) {

        Repository createdRepository = repositoryService.createRepository(repository, memberIds);
        return new ResponseEntity<>(createdRepository, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Repository> updateRepository(
            @PathVariable Long id,
            @Valid @RequestBody Repository repositoryDetails,
            @RequestParam(value = "memberIds", required = false) List<Long> memberIds) {

        Repository updatedRepository = repositoryService.updateRepository(id, repositoryDetails, memberIds);
        return ResponseEntity.ok(updatedRepository);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteRepository(@PathVariable Long id) {
        repositoryService.deleteRepository(id);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/members")
    public ResponseEntity<Repository> updateRepositoryMembers(
            @PathVariable Long id,
            @RequestBody List<Long> memberIds) {

        Repository updatedRepository = repositoryService.updateRepositoryMembers(id, memberIds);
        return ResponseEntity.ok(updatedRepository);
    }

    @PatchMapping("/{id}/migration-status")
    public ResponseEntity<Repository> updateMigrationStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) Integer progress) {

        Repository updatedRepository = repositoryService.updateMigrationStatus(id, status, progress);
        return ResponseEntity.ok(updatedRepository);
    }
}