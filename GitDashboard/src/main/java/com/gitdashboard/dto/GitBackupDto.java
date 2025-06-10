// src/main/java/com/gitdashboard/dto/GitBackupDto.java
package com.gitdashboard.dto;

import com.gitdashboard.model.GitBackup;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class GitBackupDto {
    private Long id;
    private Long repositoryId;
    private String repositoryName;
    private String department;
    private GitBackup.BackupStatus backupStatus;
    private LocalDateTime lastBackupTime;
}