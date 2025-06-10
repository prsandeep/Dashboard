// DTOs
// src/main/java/com/gitdashboard/dto/GitUserDto.java
package com.gitdashboard.dto;

import com.gitdashboard.model.GitUser;
import lombok.Data;

@Data
public class GitUserDto {
    private Long id;
    private String employeeId;
    private String username;
    private String groupName;
    private GitUser.UserRole role;
}