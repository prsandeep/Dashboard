package com.apisvn.model;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "git_migrations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GitMigration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String size;

    @Column(nullable = false)
    private String status; // In Progress, Completed, Not Started, Failed

    @Column(nullable = false)
    private Integer progress;

    @Column(name = "started_date")
    private LocalDateTime startedDate;

    @Column(name = "completed_date")
    private LocalDateTime completedDate;

    @Column(name = "estimated_time")
    private String estimatedTime;

    @Column(name = "assigned_to")
    private String assignedTo;

    @Column(name = "color_code")
    private String colorCode;

    @ManyToOne
    @JoinColumn(name = "repository_id")
    private Repository repository;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}