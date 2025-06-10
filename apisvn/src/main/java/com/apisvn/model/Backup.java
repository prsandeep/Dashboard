package com.apisvn.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "backups")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Backup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "backup_id", unique = true, nullable = false)
    private String backupId;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private String type; // Full, Delta

    @Column(nullable = false)
    private String status; // Complete, In Progress, Failed

    @Column(nullable = false)
    private String size;

    @Column(nullable = false)
    private String duration;

    @Column(name = "initiated_by")
    private String initiatedBy;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(columnDefinition = "TEXT")
    private String logs;

    @ManyToMany
    @JoinTable(
            name = "backup_repositories",
            joinColumns = @JoinColumn(name = "backup_id"),
            inverseJoinColumns = @JoinColumn(name = "repository_id")
    )
    private Set<Repository> repositories = new HashSet<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (date == null) {
            date = LocalDateTime.now();
        }
        if (backupId == null) {
            generateBackupId();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    private void generateBackupId() {
        // Generate a backup ID in the format BKP-XXXX
        this.backupId = "BKP-" + (2000 + (int)(Math.random() * 1000));
    }


}