package com.apisvn.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BackupScheduleDTO {

    private Long id;
    private String scheduleId;

    @NotBlank(message = "Schedule name is required")
    private String name;

    @NotBlank(message = "Backup type is required")
    private String type;

    @NotBlank(message = "Frequency is required")
    private String frequency;

    @NotBlank(message = "Time is required")
    private String time;

    @NotBlank(message = "Retention period is required")
    private String retention;

    @NotBlank(message = "Status is required")
    private String status;

    private List<Long> repositoryIds;
    private String repos; // For display purposes, "All repositories" or a list
}