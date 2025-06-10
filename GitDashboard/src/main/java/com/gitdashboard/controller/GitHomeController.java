package com.gitdashboard.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/git")
public class GitHomeController {

    @GetMapping("/home")
    public ResponseEntity<Object> home() {
        return ResponseEntity.ok().body(
                Map.of(
                        "message", "Welcome to Git Dashboard API",
                        "status", "UP",
                        "timestamp", System.currentTimeMillis()));
    }
}