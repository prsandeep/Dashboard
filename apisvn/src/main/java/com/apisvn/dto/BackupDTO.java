package com.apisvn.dto;



import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BackupDTO {

    private Long id;
    private String backupId;

    @NotBlank(message = "Backup type is required")
    private String type;

    private String status;
    private String size;
    private String duration;
    private String date;

    @NotBlank(message = "Initiated by is required")
    private String initiatedBy;

    private String notes;
    private String logs;
    private List<Long> repositoryIds;
    private String repos; // For display purposes, "All repositories" or a list
}
