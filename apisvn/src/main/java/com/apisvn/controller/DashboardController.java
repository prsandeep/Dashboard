package com.apisvn.controller;


import com.apisvn.model.*;
import com.apisvn.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/svn/dashboard")
public class DashboardController {

    private final UserService userService;
    private final RepositoryService repositoryService;
    private final GitMigrationService migrationService;
    private final BackupService backupService;
    private final BackupScheduleService scheduleService;

    @Autowired
    public DashboardController(
            UserService userService,
            RepositoryService repositoryService,
            GitMigrationService migrationService,
            BackupService backupService,
            BackupScheduleService scheduleService) {
        this.userService = userService;
        this.repositoryService = repositoryService;
        this.migrationService = migrationService;
        this.backupService = backupService;
        this.scheduleService = scheduleService;
    }

    @GetMapping("/metrics")
    public ResponseEntity<Map<String, Object>> getDashboardMetrics() {
        Map<String, Object> metrics = new HashMap<>();

        // User metrics
        List<User> users = userService.getAllUsers();
        metrics.put("totalUsers", users.size());
        metrics.put("activeUsers", users.stream().filter(u -> "Active".equals(u.getStatus())).count());

        // Repository metrics
        List<Repository> repositories = repositoryService.getAllRepositories();
        metrics.put("totalRepositories", repositories.size());
        metrics.put("activeRepositories", repositories.stream()
                .filter(r -> !"Archived".equals(r.getMigrationStatus())).count());

        // Backup metrics
        List<Backup> backups = backupService.getAllBackups();
        long successfulBackups = backups.stream().filter(b -> "Complete".equals(b.getStatus())).count();
        long totalBackups = backups.size();
        metrics.put("backupSuccessRate", totalBackups > 0 ? (double) successfulBackups / totalBackups * 100 : 0);

        Optional<Backup> lastFullBackup = backupService.getLastFullBackup();
        metrics.put("lastFullBackup", lastFullBackup.map(b -> b.getDate().format(DateTimeFormatter.ISO_DATE_TIME)).orElse("None"));

        // Migration metrics
        List<GitMigration> migrations = migrationService.getAllMigrations();
        long completedMigrations = migrations.stream().filter(m -> "Completed".equals(m.getStatus())).count();
        long totalMigrations = migrations.size();
        metrics.put("gitMigrationProgress", totalMigrations > 0 ? (double) completedMigrations / totalMigrations * 100 : 0);

        // Get recent activity
        List<Map<String, Object>> recentActivity = getRecentActivity().getBody();
        metrics.put("recentActivity", recentActivity);

        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/recent-activity")
    public ResponseEntity<List<Map<String, Object>>> getRecentActivity() {
        // Collect recent activity from different sources
        List<Map<String, Object>> activities = collectRecentActivities();
        return ResponseEntity.ok(activities);
    }

    @GetMapping("/migration-progress")
    public ResponseEntity<Map<String, Object>> getMigrationProgress() {
        List<Repository> repositories = repositoryService.getAllRepositories();

        long total = repositories.size();
        long completed = repositories.stream().filter(r -> "Completed".equals(r.getMigrationStatus())).count();
        long inProgress = repositories.stream().filter(r -> "In Progress".equals(r.getMigrationStatus())).count();
        long notStarted = repositories.stream().filter(r -> "Not Started".equals(r.getMigrationStatus())).count();
        long archived = repositories.stream().filter(r -> "Archived".equals(r.getMigrationStatus())).count();

        double overallProgress = total > 0 ? (double) (completed + inProgress * 0.5) / total * 100 : 0;

        Map<String, Object> progressData = new HashMap<>();
        progressData.put("totalRepositories", total);
        progressData.put("completedRepositories", completed);
        progressData.put("inProgressRepositories", inProgress);
        progressData.put("notStartedRepositories", notStarted);
        progressData.put("archivedRepositories", archived);
        progressData.put("overallProgress", Math.round(overallProgress));

        return ResponseEntity.ok(progressData);
    }

    @GetMapping("/backup-summary")
    public ResponseEntity<Map<String, Object>> getBackupSummary() {
        List<Backup> backups = backupService.getAllBackups();

        int totalBackups = backups.size();
        long completed = backups.stream().filter(b -> "Complete".equals(b.getStatus())).count();
        long inProgress = backups.stream().filter(b -> "In Progress".equals(b.getStatus())).count();
        long failed = backups.stream().filter(b -> "Failed".equals(b.getStatus())).count();

        // Calculate total storage
        double totalStorage = backups.stream()
                .filter(b -> "Complete".equals(b.getStatus()))
                .filter(b -> b.getSize() != null && !b.getSize().isEmpty())
                .mapToDouble(b -> {
                    try {
                        String sizeStr = b.getSize().replaceAll("[^\\d.]", "");
                        return Double.parseDouble(sizeStr);
                    } catch (NumberFormatException e) {
                        return 0.0;
                    }
                })
                .sum();

        // Get next scheduled backup
        Optional<BackupSchedule> nextSchedule = scheduleService.getNextScheduledBackup();

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalBackups", totalBackups);
        summary.put("completedBackups", completed);
        summary.put("inProgressBackups", inProgress);
        summary.put("failedBackups", failed);
        summary.put("totalStorageGB", Math.round(totalStorage * 10) / 10.0);
        summary.put("nextScheduledBackup", nextSchedule.map(BackupSchedule::getTime).orElse("None"));

        return ResponseEntity.ok(summary);
    }

    // Helper method to collect recent activities
    private List<Map<String, Object>> collectRecentActivities() {
        // This would be more sophisticated in a real application,
        // possibly using an activity log table or collecting recent changes

        // For this example, we'll just simulate some recent activities
        LocalDateTime now = LocalDateTime.now();

        // Create sample activity entries
        Map<String, Object> activity1 = new HashMap<>();
        activity1.put("id", 1);
        activity1.put("user", "user1");
        activity1.put("action", "committed to project-alpha");
        activity1.put("time", "12 min ago");
        activity1.put("color", "bg-blue-500");

        Map<String, Object> activity2 = new HashMap<>();
        activity2.put("id", 2);
        activity2.put("user", "user2");
        activity2.put("action", "created branch in project-beta");
        activity2.put("time", "1 hour ago");
        activity2.put("color", "bg-purple-500");

        Map<String, Object> activity3 = new HashMap<>();
        activity3.put("id", 3);
        activity3.put("user", "admin");
        activity3.put("action", "started Git migration for project-gamma");
        activity3.put("time", "3 hours ago");
        activity3.put("color", "bg-pink-500");

        return List.of(activity1, activity2, activity3);
    }
}
