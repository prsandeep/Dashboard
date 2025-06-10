// src/main/java/com/gitdashboard/repository/GitBackupRepository.java
package com.gitdashboard.repository;

import com.gitdashboard.model.GitBackup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface GitBackupRepository extends JpaRepository<GitBackup, Long> {
    List<GitBackup> findByBackupStatus(GitBackup.BackupStatus status);
    Optional<GitBackup> findByRepositoryId(Long repositoryId);

    @Query("SELECT b.backupStatus, COUNT(b) FROM GitBackup b GROUP BY b.backupStatus")
    List<Object[]> countByStatus();

    @Query("SELECT COUNT(b) FROM GitBackup b WHERE b.backupStatus = 'COMPLETE'")
    Long countCompleteBackups();
}