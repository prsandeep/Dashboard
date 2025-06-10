package com.apisvn.dto;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GitMigrationDTO {

    private Long id;

    @NotBlank(message = "Migration name is required")
    @Size(min = 3, max = 100, message = "Migration name must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;

    @NotBlank(message = "Size is required")
    private String size;

    @NotBlank(message = "Status is required")
    private String status;

    @NotNull(message = "Progress is required")
    private Integer progress;

    private String startedDate;
    private String completedDate;
    private String estimatedTime;

    @NotBlank(message = "Assigned to is required")
    private String assignedTo;

    private String colorCode;
    private Long repositoryId;
}