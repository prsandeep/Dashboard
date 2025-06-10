//// src/main/java/com/gitdashboard/config/DataInitializer.java
//package com.gitdashboard.config;
//
//import com.gitdashboard.model.GitBackup;
//import com.gitdashboard.model.GitRepository;
//import com.gitdashboard.model.GitUser;
//import com.gitdashboard.repository.GitBackupRepository;
//import com.gitdashboard.repository.GitRepositoryRepository;
//import com.gitdashboard.repository.GitUserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//
//@Configuration
//public class DataInitializer {
//
//    @Autowired
//    private GitUserRepository gitUserRepository;
//
//    @Autowired
//    private GitRepositoryRepository gitRepositoryRepository;
//
//    @Autowired
//    private GitBackupRepository gitBackupRepository;
//
//    @Bean
//    public CommandLineRunner initData() {
//        return args -> {
//            // Create users
//            GitUser johnDev = new GitUser();
//            johnDev.setEmployeeId("EMP001");
//            johnDev.setUsername("john_dev");
//            johnDev.setGroupName("Frontend");
//            johnDev.setRole(GitUser.UserRole.DEVELOPER);
//            gitUserRepository.save(johnDev);
//
//            GitUser aliceAdmin = new GitUser();
//            aliceAdmin.setEmployeeId("EMP002");
//            aliceAdmin.setUsername("alice_admin");
//            aliceAdmin.setGroupName("DevOps");
//            aliceAdmin.setRole(GitUser.UserRole.ADMIN);
//            gitUserRepository.save(aliceAdmin);
//
//            GitUser bobReviewer = new GitUser();
//            bobReviewer.setEmployeeId("EMP003");
//            bobReviewer.setUsername("bob_rev");
//            bobReviewer.setGroupName("Backend");
//            bobReviewer.setRole(GitUser.UserRole.REVIEWER);
//            gitUserRepository.save(bobReviewer);
//
//            GitUser sarahTester = new GitUser();
//            sarahTester.setEmployeeId("EMP004");
//            sarahTester.setUsername("sarah_test");
//            sarahTester.setGroupName("QA");
//            sarahTester.setRole(GitUser.UserRole.TESTER);
//            gitUserRepository.save(sarahTester);
//
//            GitUser mikeDev = new GitUser();
//            mikeDev.setEmployeeId("EMP005");
//            mikeDev.setUsername("mike_dev");
//            mikeDev.setGroupName("Frontend");
//            mikeDev.setRole(GitUser.UserRole.DEVELOPER);
//            gitUserRepository.save(mikeDev);
//
//            // Create repositories
//            GitRepository frontendApp = new GitRepository();
//            frontendApp.setProjectName("Frontend App");
//            frontendApp.setDepartment("Frontend");
//            frontendApp.setGitUrl("https://github.com/company/frontend-app.git");
//            frontendApp.setSshUrl("git@github.com:company/frontend-app.git");
//            frontendApp.setCreatedDate(LocalDateTime.now().minusDays(30));
//            frontendApp.setCreatedBy(johnDev);
//            frontendApp.setMembers(Arrays.asList("EMP001", "EMP005"));
//            gitRepositoryRepository.save(frontendApp);
//
//            GitRepository backendApi = new GitRepository();
//            backendApi.setProjectName("Backend API");
//            backendApi.setDepartment("Backend");
//            backendApi.setGitUrl("https://github.com/company/backend-api.git");
//            backendApi.setSshUrl("git@github.com:company/backend-api.git");
//            backendApi.setCreatedDate(LocalDateTime.now().minusDays(25));
//            backendApi.setCreatedBy(bobReviewer);
//            backendApi.setMembers(Arrays.asList("EMP003"));
//            gitRepositoryRepository.save(backendApi);
//
//            GitRepository devopsTools = new GitRepository();
//            devopsTools.setProjectName("DevOps Tools");
//            devopsTools.setDepartment("DevOps");
//            devopsTools.setGitUrl("https://github.com/company/devops-tools.git");
//            devopsTools.setSshUrl("git@github.com:company/devops-tools.git");
//            devopsTools.setCreatedDate(LocalDateTime.now().minusDays(15));
//            devopsTools.setCreatedBy(aliceAdmin);
//            devopsTools.setMembers(Arrays.asList("EMP002"));
//            gitRepositoryRepository.save(devopsTools);
//
//            GitRepository testFramework = new GitRepository();
//            testFramework.setProjectName("Test Framework");
//            testFramework.setDepartment("QA");
//            testFramework.setGitUrl("https://github.com/company/test-framework.git");
//            testFramework.setSshUrl("git@github.com:company/test-framework.git");
//            testFramework.setCreatedDate(LocalDateTime.now().minusDays(10));
//            testFramework.setCreatedBy(sarahTester);
//            testFramework.setMembers(Arrays.asList("EMP004"));
//            gitRepositoryRepository.save(testFramework);
//
//            GitRepository mobileApp = new GitRepository();
//            mobileApp.setProjectName("Mobile App");
//            mobileApp.setDepartment("Frontend");
//            mobileApp.setGitUrl("https://github.com/company/mobile-app.git");
//            mobileApp.setSshUrl("git@github.com:company/mobile-app.git");
//            mobileApp.setCreatedDate(LocalDateTime.now().minusDays(5));
//            mobileApp.setCreatedBy(johnDev);
//            mobileApp.setMembers(Arrays.asList("EMP001", "EMP005"));
//            gitRepositoryRepository.save(mobileApp);
//
//            // Create backups
//            GitBackup frontendBackup = new GitBackup();
//            frontendBackup.setRepository(frontendApp);
//            frontendBackup.setBackupStatus(GitBackup.BackupStatus.COMPLETE);
//            frontendBackup.setLastBackupTime(LocalDateTime.now().minusHours(2));
//            gitBackupRepository.save(frontendBackup);
//
//            GitBackup backendBackup = new GitBackup();
//            backendBackup.setRepository(backendApi);
//            backendBackup.setBackupStatus(GitBackup.BackupStatus.COMPLETE);
//            backendBackup.setLastBackupTime(LocalDateTime.now().minusHours(3));
//            gitBackupRepository.save(backendBackup);
//
//            GitBackup devopsBackup = new GitBackup();
//            devopsBackup.setRepository(devopsTools);
//            devopsBackup.setBackupStatus(GitBackup.BackupStatus.PENDING);
//            devopsBackup.setLastBackupTime(LocalDateTime.now().minusDays(1));
//            gitBackupRepository.save(devopsBackup);
//
//            GitBackup testBackup = new GitBackup();
//            testBackup.setRepository(testFramework);
//            testBackup.setBackupStatus(GitBackup.BackupStatus.COMPLETE);
//            testBackup.setLastBackupTime(LocalDateTime.now().minusHours(1));
//            gitBackupRepository.save(testBackup);
//
//            GitBackup mobileBackup = new GitBackup();
//            mobileBackup.setRepository(mobileApp);
//            mobileBackup.setBackupStatus(GitBackup.BackupStatus.PENDING);
//            mobileBackup.setLastBackupTime(LocalDateTime.now().minusDays(1));
//            gitBackupRepository.save(mobileBackup);
//        };
//    }
//}