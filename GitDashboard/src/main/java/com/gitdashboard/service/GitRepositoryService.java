
package com.gitdashboard.service;

import com.gitdashboard.dto.GitRepositoryDto;
import com.gitdashboard.model.GitRepository;
import com.gitdashboard.model.GitUser;
import com.gitdashboard.repository.GitRepositoryRepository;
import com.gitdashboard.repository.GitUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GitRepositoryService {
    @Autowired
    private GitRepositoryRepository gitRepositoryRepository;

    @Autowired
    private GitUserRepository gitUserRepository;

    public List<GitRepositoryDto> getAllRepositories() {
        return gitRepositoryRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public GitRepositoryDto getRepositoryById(Long id) {
        return gitRepositoryRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new RuntimeException("Repository not found with id: " + id));
    }

    public List<GitRepositoryDto> searchRepositories(String query) {
        return gitRepositoryRepository.findByProjectNameContainingIgnoreCase(query).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public GitRepositoryDto createRepository(GitRepositoryDto repositoryDto) {
        GitRepository repository = convertToEntity(repositoryDto);
        return convertToDto(gitRepositoryRepository.save(repository));
    }

    public GitRepositoryDto updateRepository(Long id, GitRepositoryDto repositoryDto) {
        if (!gitRepositoryRepository.existsById(id)) {
            throw new RuntimeException("Repository not found with id: " + id);
        }
        GitRepository repository = convertToEntity(repositoryDto);
        repository.setId(id);
        return convertToDto(gitRepositoryRepository.save(repository));
    }

    public void deleteRepository(Long id) {
        if (!gitRepositoryRepository.existsById(id)) {
            throw new RuntimeException("Repository not found with id: " + id);
        }
        gitRepositoryRepository.deleteById(id);
    }

    public Map<String, Long> getRepositoryCountsByDepartment() {
        List<Object[]> results = gitRepositoryRepository.countByDepartment();
        return results.stream()
                .collect(Collectors.toMap(
                        obj -> (String) obj[0],
                        obj -> (Long) obj[1]
                ));
    }

    private GitRepositoryDto convertToDto(GitRepository repository) {
        GitRepositoryDto dto = new GitRepositoryDto();
        dto.setId(repository.getId());
        dto.setProjectName(repository.getProjectName());
        dto.setDepartment(repository.getDepartment());
        dto.setGitUrl(repository.getGitUrl());
        dto.setSshUrl(repository.getSshUrl());
        dto.setCreatedDate(repository.getCreatedDate());
        dto.setCreatedByUsername(repository.getCreatedBy() != null ? repository.getCreatedBy().getUsername() : null);
        dto.setMembers(repository.getMembers());
        return dto;
    }

    private GitRepository convertToEntity(GitRepositoryDto dto) {
        GitRepository repository = new GitRepository();
        repository.setId(dto.getId());
        repository.setProjectName(dto.getProjectName());
        repository.setDepartment(dto.getDepartment());
        repository.setGitUrl(dto.getGitUrl());
        repository.setSshUrl(dto.getSshUrl());
        repository.setCreatedDate(dto.getCreatedDate());

        if (dto.getCreatedByUsername() != null) {
            GitUser createdBy = gitUserRepository.findByUsername(dto.getCreatedByUsername())
                    .orElse(null);
            repository.setCreatedBy(createdBy);
        }

        repository.setMembers(dto.getMembers());
        return repository;
    }
}