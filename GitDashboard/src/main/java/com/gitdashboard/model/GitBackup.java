// src/main/java/com/gitdashboard/model/GitBackup.java
package com.gitdashboard.model;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "git_backups")
public class GitBackup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "repository_id")
    private GitRepository repository;

    @Enumerated(EnumType.STRING)
    private BackupStatus backupStatus;

    private LocalDateTime lastBackupTime;

    public enum BackupStatus {
        COMPLETE, PENDING
    }
}