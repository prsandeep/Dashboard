package com.gitdashboard.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "git_users")
public class GitUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String employeeId;

    @Column(unique = true)
    private String username;

    private String groupName;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public enum UserRole {
        DEVELOPER, REVIEWER, TESTER, ADMIN
    }
}