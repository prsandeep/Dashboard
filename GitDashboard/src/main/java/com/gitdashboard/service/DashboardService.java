
package com.gitdashboard.service;

import com.gitdashboard.dto.DashboardSummaryDto;
import com.gitdashboard.repository.GitUserRepository;
import com.gitdashboard.repository.GitRepositoryRepository;
import com.gitdashboard.repository.GitBackupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    @Autowired
    private GitUserService gitUserService;

    @Autowired
    private GitRepositoryService gitRepositoryService;

    @Autowired
    private GitBackupService gitBackupService;

    @Autowired
    private GitUserRepository gitUserRepository;

    @Autowired
    private GitRepositoryRepository gitRepositoryRepository;

    public DashboardSummaryDto getDashboardSummary() {
        DashboardSummaryDto summary = new DashboardSummaryDto();

        // Count totals
        long totalUsers = gitUserRepository.count();
        long totalRepositories = gitRepositoryRepository.count();
        long totalBackupsCompleted = gitBackupService.getCompletedBackupsCount();

        summary.setTotalUsers(totalUsers);
        summary.setTotalRepositories(totalRepositories);
        summary.setTotalBackupsCompleted(totalBackupsCompleted);

        // Calculate backup completion rate
        double backupCompletionRate = totalRepositories > 0
                ? ((double) totalBackupsCompleted / totalRepositories) * 100
                : 0;
        summary.setBackupCompletionRate(Math.round(backupCompletionRate * 100.0) / 100.0);

        // Get counts by category
        summary.setUsersByRole(gitUserService.getUserCountsByRole());
        summary.setReposByDepartment(gitRepositoryService.getRepositoryCountsByDepartment());
        summary.setBackupsByStatus(gitBackupService.getBackupCountsByStatus());

        return summary;
    }
}