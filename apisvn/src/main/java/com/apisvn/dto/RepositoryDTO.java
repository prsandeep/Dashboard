package com.apisvn.dto;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepositoryDTO {

    private Long id;

    @NotBlank(message = "Repository name is required")
    @Size(min = 3, max = 100, message = "Repository name must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;

    private String size;
    private String lastCommit;
    private String lastCommitBy;
    private String backupStatus;
    private String migrationStatus;
    private Integer migrationProgress;
    private String colorCode;
    private String createdDate;
    private List<UserDTO> members;
    private List<Long> memberIds;
}