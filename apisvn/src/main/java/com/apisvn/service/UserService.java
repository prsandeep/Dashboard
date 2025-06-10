package com.apisvn.service;


import com.apisvn.model.User;
import com.apisvn.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final Random random = new Random();

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getUsersByRole(String role) {
        return userRepository.findByRole(role);
    }

    public List<User> getUsersByStatus(String status) {
        return userRepository.findByStatus(status);
    }

    public List<User> getUsersByGroup(String group) {
        return userRepository.findByGroup(group);
    }

    public List<User> searchUsers(String term) {
        return userRepository.findByUsernameContainingIgnoreCaseOrFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                term, term, term);
    }

    @Transactional
    public User createUser(User user) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + user.getUsername());
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }

        // Generate initials if not provided
        if (user.getInitials() == null || user.getInitials().isEmpty()) {
            String[] names = user.getFullName().split(" ");
            StringBuilder initialsBuilder = new StringBuilder();
            for (String name : names) {
                if (!name.isEmpty()) {
                    initialsBuilder.append(name.charAt(0));
                }
            }
            user.setInitials(initialsBuilder.toString().toUpperCase());
        }

        // Set color code if not provided
        if (user.getColorCode() == null || user.getColorCode().isEmpty()) {
            user.setColorCode(getRandomColorCode());
        }

        // Set created and last activity time
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setLastActivity(now);

        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);

        // Check if username is being changed and already exists
        if (!user.getUsername().equals(userDetails.getUsername()) &&
                userRepository.existsByUsername(userDetails.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + userDetails.getUsername());
        }

        // Check if email is being changed and already exists
        if (!user.getEmail().equals(userDetails.getEmail()) &&
                userRepository.existsByEmail(userDetails.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + userDetails.getEmail());
        }

        // Update user details
        user.setUsername(userDetails.getUsername());
        user.setFullName(userDetails.getFullName());
        user.setEmail(userDetails.getEmail());
        user.setRole(userDetails.getRole());
        user.setStatus(userDetails.getStatus());
        user.setGroup(userDetails.getGroup());
        user.setLastActivity(LocalDateTime.now());

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public void updateUserStatus(Long id, String status) {
        User user = getUserById(id);
        user.setStatus(status);
        user.setLastActivity(LocalDateTime.now());
        userRepository.save(user);
    }

    // Helper method to generate random color code for user avatar
    private String getRandomColorCode() {
        String[] colors = {
                "bg-blue-500", "bg-purple-500", "bg-orange-500", "bg-red-500",
                "bg-indigo-500", "bg-green-500", "bg-pink-500", "bg-teal-500"
        };
        return colors[random.nextInt(colors.length)];
    }
}