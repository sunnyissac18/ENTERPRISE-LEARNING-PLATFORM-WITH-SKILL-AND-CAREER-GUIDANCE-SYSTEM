package com.skillsphere.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KeycloakEmployeeSyncScheduler {

    private final KeycloakEmployeeSyncService syncService;

    @Scheduled(fixedDelay = 30000)
    public void syncKeycloakUsers() {

        syncService.syncUsers();
    }
}