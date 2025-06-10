
package com.gitdashboard.controller;

import com.gitdashboard.dto.GitRepositoryDto;
import com.gitdashboard.service.GitRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/git/repositories")
public class GitRepositoryController {
    @Autowired
    private GitRepositoryService gitRepositoryService;

    @GetMapping
    public ResponseEntity<List<GitRepositoryDto>> getAllRepositories() {
        return ResponseEntity.ok(gitRepositoryService.getAllRepositories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GitRepositoryDto> getRepositoryById(@PathVariable Long id) {
        return ResponseEntity.ok(gitRepositoryService.getRepositoryById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<GitRepositoryDto>> searchRepositories(@RequestParam String query) {
        return ResponseEntity.ok(gitRepositoryService.searchRepositories(query));
    }

    @GetMapping("/departments")
    public ResponseEntity<Map<String, Long>> getRepositoryCountsByDepartment() {
        return ResponseEntity.ok(gitRepositoryService.getRepositoryCountsByDepartment());
    }

    @PostMapping
    public ResponseEntity<GitRepositoryDto> createRepository(@RequestBody GitRepositoryDto repositoryDto) {
        return new ResponseEntity<>(gitRepositoryService.createRepository(repositoryDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GitRepositoryDto> updateRepository(@PathVariable Long id,
            @RequestBody GitRepositoryDto repositoryDto) {
        return ResponseEntity.ok(gitRepositoryService.updateRepository(id, repositoryDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRepository(@PathVariable Long id) {
        gitRepositoryService.deleteRepository(id);
        return ResponseEntity.noContent().build();
    }
}