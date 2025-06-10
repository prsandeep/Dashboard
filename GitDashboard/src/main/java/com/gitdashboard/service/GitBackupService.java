
package com.gitdashboard.service;

import com.gitdashboard.dto.GitBackupDto;
import com.gitdashboard.model.GitBackup;
import com.gitdashboard.model.GitRepository;
import com.gitdashboard.repository.GitBackupRepository;
import com.gitdashboard.repository.GitRepositoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GitBackupService {
    @Autowired
    private GitBackupRepository gitBackupRepository;

    @Autowired
    private GitRepositoryRepository gitRepositoryRepository;

    public List<GitBackupDto> getAllBackups() {
        return gitBackupRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public GitBackupDto getBackupById(Long id) {
        return gitBackupRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new RuntimeException("Backup not found with id: " + id));
    }

    public GitBackupDto getBackupByRepositoryId(Long repositoryId) {
        return gitBackupRepository.findByRepositoryId(repositoryId)
                .map(this::convertToDto)
                .orElseThrow(() -> new RuntimeException("Backup not found for repository with id: " + repositoryId));
    }

    public List<GitBackupDto> getBackupsByStatus(GitBackup.BackupStatus status) {
        return gitBackupRepository.findByBackupStatus(status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public GitBackupDto createBackup(GitBackupDto backupDto) {
        GitBackup backup = convertToEntity(backupDto);
        return convertToDto(gitBackupRepository.save(backup));
    }

    public GitBackupDto updateBackup(Long id, GitBackupDto backupDto) {
        if (!gitBackupRepository.existsById(id)) {
            throw new RuntimeException("Backup not found with id: " + id);
        }
        GitBackup backup = convertToEntity(backupDto);
        backup.setId(id);
        return convertToDto(gitBackupRepository.save(backup));
    }

    public GitBackupDto runBackup(Long repositoryId) {
        GitRepository repository = gitRepositoryRepository.findById(repositoryId)
                .orElseThrow(() -> new RuntimeException("Repository not found with id: " + repositoryId));

        GitBackup backup = gitBackupRepository.findByRepositoryId(repositoryId)
                .orElse(new GitBackup());

        backup.setRepository(repository);
        backup.setBackupStatus(GitBackup.BackupStatus.COMPLETE);
        backup.setLastBackupTime(LocalDateTime.now());

        return convertToDto(gitBackupRepository.save(backup));
    }

    public Map<String, Long> getBackupCountsByStatus() {
        List<Object[]> results = gitBackupRepository.countByStatus();
        Map<String, Long> counts = results.stream()
                .collect(Collectors.toMap(
                        obj -> ((GitBackup.BackupStatus) obj[0]).name(),
                        obj -> (Long) obj[1]
                ));

        // Ensure all statuses are represented
        for (GitBackup.BackupStatus status : GitBackup.BackupStatus.values()) {
            counts.putIfAbsent(status.name(), 0L);
        }

        return counts;
    }

    public long getCompletedBackupsCount() {
        return gitBackupRepository.countCompleteBackups();
    }

    private GitBackupDto convertToDto(GitBackup backup) {
        GitBackupDto dto = new GitBackupDto();
        dto.setId(backup.getId());

        if (backup.getRepository() != null) {
            dto.setRepositoryId(backup.getRepository().getId());
            dto.setRepositoryName(backup.getRepository().getProjectName());
            dto.setDepartment(backup.getRepository().getDepartment());
        }

        dto.setBackupStatus(backup.getBackupStatus());
        dto.setLastBackupTime(backup.getLastBackupTime());
        return dto;
    }

    private GitBackup convertToEntity(GitBackupDto dto) {
        GitBackup backup = new GitBackup();
        backup.setId(dto.getId());

        if (dto.getRepositoryId() != null) {
            GitRepository repository = gitRepositoryRepository.findById(dto.getRepositoryId())
                    .orElseThrow(() -> new RuntimeException("Repository not found with id: " + dto.getRepositoryId()));
            backup.setRepository(repository);
        }

        backup.setBackupStatus(dto.getBackupStatus());
        backup.setLastBackupTime(dto.getLastBackupTime());
        return backup;
    }
}