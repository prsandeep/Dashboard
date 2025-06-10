
package com.gitdashboard.service;

import com.gitdashboard.dto.GitUserDto;
import com.gitdashboard.model.GitUser;
import com.gitdashboard.repository.GitUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class GitUserService {
    @Autowired
    private GitUserRepository gitUserRepository;

    public List<GitUserDto> getAllUsers() {
        return gitUserRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public GitUserDto getUserById(Long id) {
        return gitUserRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public GitUserDto createUser(GitUserDto userDto) {
        validateUserDto(userDto);
        GitUser user = convertToEntity(userDto);
        return convertToDto(gitUserRepository.save(user));
    }

    public GitUserDto updateUser(Long id, GitUserDto userDto) {
        validateUserDto(userDto);
        if (!gitUserRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        GitUser user = convertToEntity(userDto);
        user.setId(id);
        return convertToDto(gitUserRepository.save(user));
    }

    public void deleteUser(Long id) {
        if (!gitUserRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        gitUserRepository.deleteById(id);
    }

    public Map<String, Long> getUserCountsByRole() {
        List<Object[]> results = gitUserRepository.countByRole();
        Map<String, Long> counts = results.stream()
                .collect(Collectors.toMap(
                        obj -> ((GitUser.UserRole) obj[0]).name(),
                        obj -> (Long) obj[1]
                ));

        // Ensure all roles are represented
        for (GitUser.UserRole role : GitUser.UserRole.values()) {
            counts.putIfAbsent(role.name(), 0L);
        }

        return counts;
    }

    private void validateUserDto(GitUserDto userDto) {
        // Validate employee ID uniqueness
        Optional<GitUser> existingByEmployeeId = gitUserRepository.findByEmployeeId(userDto.getEmployeeId());
        if (existingByEmployeeId.isPresent() && !existingByEmployeeId.get().getId().equals(userDto.getId())) {
            throw new RuntimeException("Employee ID already exists: " + userDto.getEmployeeId());
        }

        // Validate username uniqueness
        Optional<GitUser> existingByUsername = gitUserRepository.findByUsername(userDto.getUsername());
        if (existingByUsername.isPresent() && !existingByUsername.get().getId().equals(userDto.getId())) {
            throw new RuntimeException("Username already exists: " + userDto.getUsername());
        }
    }

    private GitUserDto convertToDto(GitUser user) {
        GitUserDto dto = new GitUserDto();
        dto.setId(user.getId());
        dto.setEmployeeId(user.getEmployeeId());
        dto.setUsername(user.getUsername());
        dto.setGroupName(user.getGroupName());
        dto.setRole(user.getRole());
        return dto;
    }

    private GitUser convertToEntity(GitUserDto dto) {
        GitUser user = new GitUser();
        user.setId(dto.getId());
        user.setEmployeeId(dto.getEmployeeId());
        user.setUsername(dto.getUsername());
        user.setGroupName(dto.getGroupName());
        user.setRole(dto.getRole());
        return user;
    }
}