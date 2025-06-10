package com.apisvn.config;


//import com.apisvn.model.*;
//import com.apisvn.repository.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//
//import java.time.LocalDateTime;
//import java.util.*;
//
//@Configuration
//public class DataInitializer {
//
//    @Bean
//    @Profile("!test") // Don't run during tests
//    CommandLineRunner initDatabase(
//            UserRepository userRepository,
//            RepositoryRepository repositoryRepository,
//            GitMigrationRepository migrationRepository,
//            BackupRepository backupRepository,
//            BackupScheduleRepository scheduleRepository) {
//
//        return args -> {
//            // Check if data already exists
//            if (userRepository.count() > 0) {
//                System.out.println("Database already contains data. Skipping initialization.");
//                return;
//            }
//
//            System.out.println("Initializing database with sample data...");
//
//            // Initialize users
//            List<User> users = createSampleUsers();
//            users = userRepository.saveAll(users);
//            System.out.println("Created " + users.size() + " sample users");
//
//            // Initialize repositories
//            List<Repository> repositories = createSampleRepositories(users);
//            repositories = repositoryRepository.saveAll(repositories);
//            System.out.println("Created " + repositories.size() + " sample repositories");
//
//            // Initialize Git migrations
//            List<GitMigration> migrations = createSampleGitMigrations(repositories);
//            migrations = migrationRepository.saveAll(migrations);
//            System.out.println("Created " + migrations.size() + " sample migrations");
//
//            // Initialize backups
//            List<Backup> backups = createSampleBackups(repositories);
//            backups = backupRepository.saveAll(backups);
//            System.out.println("Created " + backups.size() + " sample backups");
//
//            // Initialize backup schedules
//            List<BackupSchedule> schedules = createSampleBackupSchedules(repositories);
//            schedules = scheduleRepository.saveAll(schedules);
//            System.out.println("Created " + schedules.size() + " sample backup schedules");
//
//            System.out.println("Sample data initialization complete!");
//        };
//    }
//
//    private List<User> createSampleUsers() {
//        List<User> users = new ArrayList<>();
//
//        // Create sample users
//        User user1 = new User();
//        user1.setUsername("jdoe");
//        user1.setFullName("John Doe");
//        user1.setEmail("john.doe@example.com");
//        user1.setRole("Admin");
//        user1.setStatus("Active");
//        user1.setGroup("Engineering");
//        user1.setInitials("JD");
//        user1.setColorCode("bg-blue-500");
//        user1.setLastActivity(LocalDateTime.now().minusMinutes(12));
//        users.add(user1);
//
//        User user2 = new User();
//        user2.setUsername("tsmith");
//        user2.setFullName("Taylor Smith");
//        user2.setEmail("taylor.smith@example.com");
//        user2.setRole("Developer");
//        user2.setStatus("Active");
//        user2.setGroup("Development");
//        user2.setInitials("TS");
//        user2.setColorCode("bg-purple-500");
//        user2.setLastActivity(LocalDateTime.now().minusHours(1));
//        users.add(user2);
//
//        User user3 = new User();
//        user3.setUsername("ajohnson");
//        user3.setFullName("Alex Johnson");
//        user3.setEmail("alex.johnson@example.com");
//        user3.setRole("ReadOnly");
//        user3.setStatus("Active");
//        user3.setGroup("Operations");
//        user3.setInitials("AJ");
//        user3.setColorCode("bg-orange-500");
//        user3.setLastActivity(LocalDateTime.now().minusDays(1));
//        users.add(user3);
//
//        User user4 = new User();
//        user4.setUsername("mlee");
//        user4.setFullName("Morgan Lee");
//        user4.setEmail("morgan.lee@example.com");
//        user4.setRole("Developer");
//        user4.setStatus("Inactive");
//        user4.setGroup("Development");
//        user4.setInitials("ML");
//        user4.setColorCode("bg-red-500");
//        user4.setLastActivity(LocalDateTime.now().minusDays(3));
//        users.add(user4);
//
//        User user5 = new User();
//        user5.setUsername("cwilson");
//        user5.setFullName("Casey Wilson");
//        user5.setEmail("casey.wilson@example.com");
//        user5.setRole("Admin");
//        user5.setStatus("Locked");
//        user5.setGroup("Management");
//        user5.setInitials("CW");
//        user5.setColorCode("bg-indigo-500");
//        user5.setLastActivity(LocalDateTime.now().minusDays(7));
//        users.add(user5);
//
//        return users;
//    }
//
//    private List<Repository> createSampleRepositories(List<User> users) {
//        List<Repository> repositories = new ArrayList<>();
//
//        // Create sample repositories
//        Repository repo1 = new Repository();
//        repo1.setName("project-alpha");
//        repo1.setDescription("Main project for client Alpha");
//        repo1.setSize("256 MB");
//        repo1.setBackupStatus("Complete");
//        repo1.setMigrationStatus("In Progress");
//        repo1.setMigrationProgress(75);
//        repo1.setColorCode("bg-teal-500");
//        repo1.setCreatedDate(LocalDateTime.now().minusDays(30));
//        repo1.setLastCommit(LocalDateTime.now().minusMinutes(12));
//        repo1.setLastCommitBy("jdoe");
//
//        // Add members to repository
//        Set<User> repo1Members = new HashSet<>();
//        repo1Members.add(users.get(0)); // John Doe
//        repo1Members.add(users.get(1)); // Taylor Smith
//        repo1Members.add(users.get(2)); // Alex Johnson
//        repo1.setMembers(repo1Members);
//
//        repositories.add(repo1);
//
//        Repository repo2 = new Repository();
//        repo2.setName("project-beta");
//        repo2.setDescription("Beta testing environment");
//        repo2.setSize("128 MB");
//        repo2.setBackupStatus("Complete");
//        repo2.setMigrationStatus("Completed");
//        repo2.setMigrationProgress(100);
//        repo2.setColorCode("bg-orange-500");
//        repo2.setCreatedDate(LocalDateTime.now().minusDays(60));
//        repo2.setLastCommit(LocalDateTime.now().minusHours(1));
//        repo2.setLastCommitBy("tsmith");
//
//        // Add members to repository
//        Set<User> repo2Members = new HashSet<>();
//        repo2Members.add(users.get(1)); // Taylor Smith
//        repo2Members.add(users.get(3)); // Morgan Lee
//        repo2.setMembers(repo2Members);
//
//        repositories.add(repo2);
//
//        Repository repo3 = new Repository();
//        repo3.setName("project-gamma");
//        repo3.setDescription("Internal tools repository");
//        repo3.setSize("512 MB");
//        repo3.setBackupStatus("Complete");
//        repo3.setMigrationStatus("Not Started");
//        repo3.setMigrationProgress(0);
//        repo3.setColorCode("bg-indigo-500");
//        repo3.setCreatedDate(LocalDateTime.now().minusDays(45));
//        repo3.setLastCommit(LocalDateTime.now().minusHours(3));
//        repo3.setLastCommitBy("ajohnson");
//
//        // Add members to repository
//        Set<User> repo3Members = new HashSet<>();
//        repo3Members.add(users.get(2)); // Alex Johnson
//        repo3Members.add(users.get(4)); // Casey Wilson
//        repo3Members.add(users.get(0)); // John Doe
//        repo3.setMembers(repo3Members);
//
//        repositories.add(repo3);
//
//        Repository repo4 = new Repository();
//        repo4.setName("project-delta");
//        repo4.setDescription("Customer facing API");
//        repo4.setSize("85 MB");
//        repo4.setBackupStatus("Complete");
//        repo4.setMigrationStatus("Not Started");
//        repo4.setMigrationProgress(0);
//        repo4.setColorCode("bg-purple-500");
//        repo4.setCreatedDate(LocalDateTime.now().minusDays(150));
//        repo4.setLastCommit(LocalDateTime.now().minusDays(7));
//        repo4.setLastCommitBy("mlee");
//
//        // Add members to repository
//        Set<User> repo4Members = new HashSet<>();
//        repo4Members.add(users.get(3)); // Morgan Lee
//        repo4Members.add(users.get(1)); // Taylor Smith
//        repo4.setMembers(repo4Members);
//
//        repositories.add(repo4);
//
//        Repository repo5 = new Repository();
//        repo5.setName("legacy-project");
//        repo5.setDescription("Legacy codebase - migration pending");
//        repo5.setSize("2.5 GB");
//        repo5.setBackupStatus("Complete");
//        repo5.setMigrationStatus("Archived");
//        repo5.setMigrationProgress(0);
//        repo5.setColorCode("bg-pink-500");
//        repo5.setCreatedDate(LocalDateTime.now().minusDays(500));
//        repo5.setLastCommit(LocalDateTime.now().minusDays(60));
//        repo5.setLastCommitBy("cwilson");
//
//        // Add members to repository
//        Set<User> repo5Members = new HashSet<>();
//        repo5Members.add(users.get(4)); // Casey Wilson
//        repo5.setMembers(repo5Members);
//
//        repositories.add(repo5);
//
//        return repositories;
//    }
//
//    private List<GitMigration> createSampleGitMigrations(List<Repository> repositories) {
//        List<GitMigration> migrations = new ArrayList<>();
//
//        // Create sample migrations
//        GitMigration migration1 = new GitMigration();
//        migration1.setName("project-alpha");
//        migration1.setDescription("Main project repository");
//        migration1.setSize("256 MB");
//        migration1.setStatus("In Progress");
//        migration1.setProgress(75);
//        migration1.setStartedDate(LocalDateTime.now().minusHours(2));
//        migration1.setCompletedDate(null);
//        migration1.setEstimatedTime("4 hours");
//        migration1.setAssignedTo("John Doe");
//        migration1.setColorCode("bg-teal-500");
//        migration1.setRepository(repositories.get(0)); // project-alpha
//
//        migrations.add(migration1);
//
//        GitMigration migration2 = new GitMigration();
//        migration2.setName("project-beta");
//        migration2.setDescription("Beta testing environment");
//        migration2.setSize("128 MB");
//        migration2.setStatus("Completed");
//        migration2.setProgress(100);
//        migration2.setStartedDate(LocalDateTime.now().minusDays(5));
//        migration2.setCompletedDate(LocalDateTime.now().minusDays(4));
//        migration2.setEstimatedTime("2 hours");
//        migration2.setAssignedTo("Taylor Smith");
//        migration2.setColorCode("bg-orange-500");
//        migration2.setRepository(repositories.get(1)); // project-beta
//
//        migrations.add(migration2);
//
//        GitMigration migration3 = new GitMigration();
//        migration3.setName("project-gamma");
//        migration3.setDescription("Internal tools repository");
//        migration3.setSize("512 MB");
//        migration3.setStatus("Not Started");
//        migration3.setProgress(0);
//        migration3.setStartedDate(null);
//        migration3.setCompletedDate(null);
//        migration3.setEstimatedTime("8 hours");
//        migration3.setAssignedTo("Alex Johnson");
//        migration3.setColorCode("bg-indigo-500");
//        migration3.setRepository(repositories.get(2)); // project-gamma
//
//        migrations.add(migration3);
//
//        GitMigration migration4 = new GitMigration();
//        migration4.setName("project-delta");
//        migration4.setDescription("Customer facing API");
//        migration4.setSize("85 MB");
//        migration4.setStatus("In Progress");
//        migration4.setProgress(43);
//        migration4.setStartedDate(LocalDateTime.now().minusDays(3));
//        migration4.setCompletedDate(null);
//        migration4.setEstimatedTime("1.5 hours");
//        migration4.setAssignedTo("Morgan Lee");
//        migration4.setColorCode("bg-purple-500");
//        migration4.setRepository(repositories.get(3)); // project-delta
//
//        migrations.add(migration4);
//
//        GitMigration migration5 = new GitMigration();
//        migration5.setName("legacy-project");
//        migration5.setDescription("Legacy codebase migration");
//        migration5.setSize("345 MB");
//        migration5.setStatus("Failed");
//        migration5.setProgress(62);
//        migration5.setStartedDate(LocalDateTime.now().minusDays(10));
//        migration5.setCompletedDate(null);
//        migration5.setEstimatedTime("5 hours");
//        migration5.setAssignedTo("Casey Wilson");
//        migration5.setColorCode("bg-red-500");
//        migration5.setRepository(repositories.get(4)); // legacy-project
//
//        migrations.add(migration5);
//
//        return migrations;
//    }
//
//    private List<Backup> createSampleBackups(List<Repository> repositories) {
//        List<Backup> backups = new ArrayList<>();
//
//        // Create sample backups
//        Backup backup1 = new Backup();
//        backup1.setBackupId("BKP-2042");
//        backup1.setDate(LocalDateTime.now().minusHours(12).minusMinutes(30));
//        backup1.setType("Full");
//        backup1.setStatus("Complete");
//        backup1.setSize("15.8 GB");
//        backup1.setDuration("1h 12m");
//        backup1.setInitiatedBy("System (Automated)");
//        backup1.setNotes("");
//        backup1.setLogs("Backup completed successfully with no errors.");
//        backup1.setRepositories(new HashSet<>(repositories)); // All repositories
//
//        backups.add(backup1);
//
//        Backup backup2 = new Backup();
//        backup2.setBackupId("BKP-2041");
//        backup2.setDate(LocalDateTime.now().minusDays(1).minusHours(12).minusMinutes(30));
//        backup2.setType("Full");
//        backup2.setStatus("Complete");
//        backup2.setSize("15.6 GB");
//        backup2.setDuration("1h 08m");
//        backup2.setInitiatedBy("System (Automated)");
//        backup2.setNotes("");
//        backup2.setLogs("Backup completed successfully with no errors.");
//        backup2.setRepositories(new HashSet<>(repositories)); // All repositories
//
//        backups.add(backup2);
//
//        Backup backup3 = new Backup();
//        backup3.setBackupId("BKP-2040");
//        backup3.setDate(LocalDateTime.now().minusDays(1).minusHours(8).minusMinutes(45));
//        backup3.setType("Delta");
//        backup3.setStatus("Complete");
//        backup3.setSize("1.2 GB");
//        backup3.setDuration("18m");
//        backup3.setInitiatedBy("John Doe");
//        backup3.setNotes("Pre-release backup");
//        backup3.setLogs("Backup completed successfully with no errors.");
//
//        // Selected repositories
//        Set<Repository> backup3Repos = new HashSet<>();
//        backup3Repos.add(repositories.get(0)); // project-alpha
//        backup3Repos.add(repositories.get(1)); // project-beta
//        backup3Repos.add(repositories.get(2)); // project-gamma
//        backup3.setRepositories(backup3Repos);
//
//        backups.add(backup3);
//
//        Backup backup4 = new Backup();
//        backup4.setBackupId("BKP-2039");
//        backup4.setDate(LocalDateTime.now().minusDays(2).minusHours(12).minusMinutes(30));
//        backup4.setType("Full");
//        backup4.setStatus("Failed");
//        backup4.setSize("12.1 GB");
//        backup4.setDuration("42m");
//        backup4.setInitiatedBy("System (Automated)");
//        backup4.setNotes("");
//        backup4.setLogs("Error: Storage quota exceeded. Backup terminated at 78% completion.");
//        backup4.setRepositories(new HashSet<>(repositories)); // All repositories
//
//        backups.add(backup4);
//
//        Backup backup5 = new Backup();
//        backup5.setBackupId("BKP-2038");
//        backup5.setDate(LocalDateTime.now().minusDays(2).minusHours(7).minusMinutes(15));
//        backup5.setType("Delta");
//        backup5.setStatus("Complete");
//        backup5.setSize("0.8 GB");
//        backup5.setDuration("15m");
//        backup5.setInitiatedBy("Sarah Johnson");
//        backup5.setNotes("Hotfix backup");
//        backup5.setLogs("Backup completed successfully with no errors.");
//
//        // Selected repositories
//        Set<Repository> backup5Repos = new HashSet<>();
//        backup5Repos.add(repositories.get(3)); // project-delta
//        backup5Repos.add(repositories.get(2)); // project-gamma
//        backup5.setRepositories(backup5Repos);
//
//        backups.add(backup5);
//
//        Backup backup6 = new Backup();
//        backup6.setBackupId("BKP-2037");
//        backup6.setDate(LocalDateTime.now().minusDays(3).minusHours(12).minusMinutes(30));
//        backup6.setType("Full");
//        backup6.setStatus("Complete");
//        backup6.setSize("15.4 GB");
//        backup6.setDuration("1h 06m");
//        backup6.setInitiatedBy("System (Automated)");
//        backup6.setNotes("");
//        backup6.setLogs("Backup completed successfully with no errors.");
//        backup6.setRepositories(new HashSet<>(repositories)); // All repositories
//
//        backups.add(backup6);
//
//        Backup backup7 = new Backup();
//        backup7.setBackupId("BKP-2036");
//        backup7.setDate(LocalDateTime.now().minusDays(4).minusHours(12).minusMinutes(30));
//        backup7.setType("Full");
//        backup7.setStatus("In Progress");
//        backup7.setSize("10.2 GB");
//        backup7.setDuration("45m");
//        backup7.setInitiatedBy("System (Automated)");
//        backup7.setNotes("");
//        backup7.setLogs("Backup in progress...");
//        backup7.setRepositories(new HashSet<>(repositories)); // All repositories
//
//        backups.add(backup7);
//
//        return backups;
//    }
//
//    private List<BackupSchedule> createSampleBackupSchedules(List<Repository> repositories) {
//        List<BackupSchedule> schedules = new ArrayList<>();
//
//        // Create sample backup schedules
//        BackupSchedule schedule1 = new BackupSchedule();
//        schedule1.setScheduleId("SCH-001");
//        schedule1.setName("Daily Full Backup");
//        schedule1.setType("Full");
//        schedule1.setFrequency("Daily");
//        schedule1.setTime("11:30 PM");
//        schedule1.setRetention("30 days");
//        schedule1.setStatus("Active");
//        schedule1.setRepositories(new HashSet<>(repositories)); // All repositories
//
//        schedules.add(schedule1);
//
//        BackupSchedule schedule2 = new BackupSchedule();
//        schedule2.setScheduleId("SCH-002");
//        schedule2.setName("Weekly Archive");
//        schedule2.setType("Full");
//        schedule2.setFrequency("Weekly");
//        schedule2.setTime("01:00 AM (Sunday)");
//        schedule2.setRetention("6 months");
//        schedule2.setStatus("Active");
//        schedule2.setRepositories(new HashSet<>(repositories)); // All repositories
//
//        schedules.add(schedule2);
//
//        BackupSchedule schedule3 = new BackupSchedule();
//        schedule3.setScheduleId("SCH-003");
//        schedule3.setName("Critical Projects Delta");
//        schedule3.setType("Delta");
//        schedule3.setFrequency("Daily");
//        schedule3.setTime("03:00 PM");
//        schedule3.setRetention("7 days");
//        schedule3.setStatus("Inactive");
//
//        // Selected repositories
//        Set<Repository> schedule3Repos = new HashSet<>();
//        schedule3Repos.add(repositories.get(0)); // project-alpha
//        schedule3Repos.add(repositories.get(1)); // project-beta
//        schedule3Repos.add(repositories.get(2)); // project-gamma
//        schedule3.setRepositories(schedule3Repos);
//
//        schedules.add(schedule3);
//
//        return schedules;
//    }
//}