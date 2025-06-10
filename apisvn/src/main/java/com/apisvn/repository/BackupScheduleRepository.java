package com.apisvn.repository;


import com.apisvn.model.BackupSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BackupScheduleRepository extends JpaRepository<BackupSchedule, Long> {

    Optional<BackupSchedule> findByScheduleId(String scheduleId);

    List<BackupSchedule> findByType(String type);

    List<BackupSchedule> findByFrequency(String frequency);

    List<BackupSchedule> findByStatus(String status);

    @Query("SELECT bs FROM BackupSchedule bs JOIN bs.repositories r WHERE r.id = :repositoryId")
    List<BackupSchedule> findByRepositoryId(@Param("repositoryId") Long repositoryId);

    @Query("SELECT bs FROM BackupSchedule bs ORDER BY bs.time ASC")
    List<BackupSchedule> findNextScheduledBackup(org.springframework.data.domain.Pageable pageable);
}