// src/main/java/com/gitdashboard/dto/GitRepositoryDto.java
package com.gitdashboard.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class GitRepositoryDto {
    private Long id;
    private String projectName;
    private String department;
    private String gitUrl;
    private String sshUrl;
    private LocalDateTime createdDate;
    private String createdByUsername;
    private List<String> members;
}