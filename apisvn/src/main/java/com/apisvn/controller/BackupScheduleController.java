package com.apisvn.controller;


import com.apisvn.model.BackupSchedule;
import com.apisvn.service.BackupScheduleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/svn/backup-schedules")
public class BackupScheduleController {

    private final BackupScheduleService scheduleService;

    @Autowired
    public BackupScheduleController(BackupScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public ResponseEntity<List<BackupSchedule>> getAllSchedules(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "frequency", required = false) String frequency,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "repositoryId", required = false) Long repositoryId) {

        List<BackupSchedule> schedules;

        if (type != null && !type.equals("All")) {
            schedules = scheduleService.getSchedulesByType(type);
        } else if (frequency != null && !frequency.equals("All")) {
            schedules = scheduleService.getSchedulesByFrequency(frequency);
        } else if (status != null && !status.equals("All")) {
            schedules = scheduleService.getSchedulesByStatus(status);
        } else if (repositoryId != null) {
            schedules = scheduleService.getSchedulesByRepositoryId(repositoryId);
        } else {
            schedules = scheduleService.getAllSchedules();
        }

        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BackupSchedule> getScheduleById(@PathVariable Long id) {
        BackupSchedule schedule = scheduleService.getScheduleById(id);
        return ResponseEntity.ok(schedule);
    }

    @GetMapping("/schedule-id/{scheduleId}")
    public ResponseEntity<BackupSchedule> getScheduleByScheduleId(@PathVariable String scheduleId) {
        Optional<BackupSchedule> schedule = scheduleService.getScheduleByScheduleId(scheduleId);
        return schedule.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/next")
    public ResponseEntity<BackupSchedule> getNextScheduledBackup() {
        Optional<BackupSchedule> schedule = scheduleService.getNextScheduledBackup();
        return schedule.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BackupSchedule> createSchedule(
            @Valid @RequestBody BackupSchedule schedule,
            @RequestParam(value = "repositoryIds", required = false) List<Long> repositoryIds) {

        BackupSchedule createdSchedule = scheduleService.createSchedule(schedule, repositoryIds);
        return new ResponseEntity<>(createdSchedule, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BackupSchedule> updateSchedule(
            @PathVariable Long id,
            @Valid @RequestBody BackupSchedule scheduleDetails,
            @RequestParam(value = "repositoryIds", required = false) List<Long> repositoryIds) {

        BackupSchedule updatedSchedule = scheduleService.updateSchedule(id, scheduleDetails, repositoryIds);
        return ResponseEntity.ok(updatedSchedule);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/toggle-status")
    public ResponseEntity<BackupSchedule> toggleScheduleStatus(@PathVariable Long id) {
        BackupSchedule updatedSchedule = scheduleService.toggleScheduleStatus(id);
        return ResponseEntity.ok(updatedSchedule);
    }
}