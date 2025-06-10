package com.apisvn.model;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "repositories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Repository {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String size;

    @Column(name = "last_commit")
    private LocalDateTime lastCommit;

    @Column(name = "last_commit_by")
    private String lastCommitBy;

    @Column(name = "backup_status")
    private String backupStatus; // Complete, In Progress, Failed

    @Column(name = "migration_status")
    private String migrationStatus; // Completed, In Progress, Not Started, Archived

    @Column(name = "migration_progress")
    private Integer migrationProgress;

    @Column(name = "color_code")
    private String colorCode;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany
    @JoinTable(
            name = "repository_members",
            joinColumns = @JoinColumn(name = "repository_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> members = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (createdDate == null) {
            createdDate = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
