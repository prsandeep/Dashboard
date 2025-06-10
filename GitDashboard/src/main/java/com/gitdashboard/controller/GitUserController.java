package com.gitdashboard.controller;

import com.gitdashboard.dto.GitUserDto;
import com.gitdashboard.service.GitUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/git/users")
public class GitUserController {
    @Autowired
    private GitUserService gitUserService;

    @GetMapping
    public ResponseEntity<List<GitUserDto>> getAllUsers() {
        return ResponseEntity.ok(gitUserService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GitUserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(gitUserService.getUserById(id));
    }

    @GetMapping("/roles")
    public ResponseEntity<Map<String, Long>> getUserCountsByRole() {
        return ResponseEntity.ok(gitUserService.getUserCountsByRole());
    }

    @PostMapping
    public ResponseEntity<GitUserDto> createUser(@RequestBody GitUserDto userDto) {
        return new ResponseEntity<>(gitUserService.createUser(userDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GitUserDto> updateUser(@PathVariable Long id, @RequestBody GitUserDto userDto) {
        return ResponseEntity.ok(gitUserService.updateUser(id, userDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        gitUserService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}