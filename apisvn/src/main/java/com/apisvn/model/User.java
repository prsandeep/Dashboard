package com.apisvn.model;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String role;  // Admin, Developer, ReadOnly

    @Column(nullable = false)
    private String status;  // Active, Inactive, Locked

    @Column(name = "`group`", nullable = false)
    private String group;

    @Column(name = "color_code")
    private String colorCode;

    @Column(name = "initials")
    private String initials;

    @Column(name = "last_activity")
    private LocalDateTime lastActivity;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        lastActivity = LocalDateTime.now();
        if (initials == null || initials.isEmpty()) {
            generateInitials();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    private void generateInitials() {
        if (fullName != null && !fullName.isEmpty()) {
            String[] names = fullName.split(" ");
            StringBuilder initialsBuilder = new StringBuilder();

            for (String name : names) {
                if (!name.isEmpty()) {
                    initialsBuilder.append(name.charAt(0));
                }
            }

            this.initials = initialsBuilder.toString().toUpperCase();
        }
    }
}