package com.apisvn.repository;


import com.apisvn.model.Backup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BackupRepository extends JpaRepository<Backup, Long> {

    Optional<Backup> findByBackupId(String backupId);

    List<Backup> findByType(String type);

    List<Backup> findByStatus(String status);

    List<Backup> findByInitiatedBy(String initiatedBy);

    List<Backup> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT b FROM Backup b JOIN b.repositories r WHERE r.id = :repositoryId")
    List<Backup> findByRepositoryId(@Param("repositoryId") Long repositoryId);

    @Query("SELECT b FROM Backup b WHERE b.type = 'Full' AND b.status = 'Complete' ORDER BY b.date DESC")
    List<Backup> findLastFullBackup(org.springframework.data.domain.Pageable pageable);
}