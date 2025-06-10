package com.apisvn.repository;



import com.apisvn.model.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
public interface RepositoryRepository extends JpaRepository<Repository, Long> {

    Optional<Repository> findByName(String name);

    List<Repository> findByMigrationStatus(String migrationStatus);

    List<Repository> findByBackupStatus(String backupStatus);

    @Query("SELECT r FROM Repository r WHERE r.name LIKE %:searchTerm% OR r.description LIKE %:searchTerm%")
    List<Repository> searchRepositories(@Param("searchTerm") String searchTerm);

    @Query("SELECT r FROM Repository r JOIN r.members m WHERE m.id = :userId")
    List<Repository> findByMemberId(@Param("userId") Long userId);

    @Query("SELECT r FROM Repository r JOIN r.members m WHERE m.username = :username")
    List<Repository> findByMemberUsername(@Param("username") String username);

    boolean existsByName(String name);
}