package com.gitdashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GitDashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(GitDashboardApplication.class, args);
    }

}
