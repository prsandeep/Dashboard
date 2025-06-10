package com.apisvn.controller;


import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/svn")
public class SvnHomeController {

    @GetMapping("/home")
    public ResponseEntity<Object> home() {
        return ResponseEntity.ok().body(
                Map.of(
                        "message", "Welcome to SVN API",
                        "status", "UP",
                        "timestamp", System.currentTimeMillis()
                )
        );
    }
}
