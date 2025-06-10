// src/main/java/com/gitdashboard/model/GitRepository.java
package com.gitdashboard.model;


import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "git_repositories")
public class GitRepository {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String projectName;
    private String department;
    private String gitUrl;
    private String sshUrl;
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private GitUser createdBy;

    @ElementCollection
    @CollectionTable(name = "repository_members", joinColumns = @JoinColumn(name = "repository_id"))
    @Column(name = "employee_id")
    private List<String> members;
}