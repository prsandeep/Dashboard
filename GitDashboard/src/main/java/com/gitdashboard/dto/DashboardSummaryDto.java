// src/main/java/com/gitdashboard/dto/DashboardSummaryDto.java
package com.gitdashboard.dto;

import lombok.Data;
import java.util.Map;

@Data
public class DashboardSummaryDto {
    private long totalUsers;
    private long totalRepositories;
    private long totalBackupsCompleted;
    private double backupCompletionRate;
    private Map<String, Long> usersByRole;
    private Map<String, Long> reposByDepartment;
    private Map<String, Long> backupsByStatus;
}