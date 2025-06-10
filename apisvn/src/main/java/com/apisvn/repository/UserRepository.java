package com.apisvn.repository;



import com.apisvn.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    List<User> findByRole(String role);

    List<User> findByStatus(String status);

    List<User> findByGroup(String group);

    List<User> findByFullNameContainingIgnoreCase(String fullName);

    List<User> findByUsernameContainingIgnoreCaseOrFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String username, String fullName, String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}