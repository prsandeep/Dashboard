package com.apisvn.model;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "backup_schedules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BackupSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "schedule_id", unique = true, nullable = false)
    private String scheduleId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type; // Full, Delta

    @Column(nullable = false)
    private String frequency; // Daily, Weekly, Monthly

    @Column(nullable = false)
    private String time;

    @Column(nullable = false)
    private String retention;

    @Column(nullable = false)
    private String status; // Active, Inactive

    @ManyToMany
    @JoinTable(
            name = "schedule_repositories",
            joinColumns = @JoinColumn(name = "schedule_id"),
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
        if (scheduleId == null) {
            generateScheduleId();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    private void generateScheduleId() {
        // Generate a schedule ID in the format SCH-XXX
        this.scheduleId = "SCH-" + String.format("%03d", (int)(Math.random() * 1000));
    }
}