package com.skillsphere.project.controller;

import com.skillsphere.project.entity.Employee;
import com.skillsphere.project.repository.EmployeeRepository;
import com.skillsphere.project.service.KeycloakEmployeeSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final KeycloakEmployeeSyncService syncService;

    @GetMapping("/me")
    public ResponseEntity<Employee> getCurrentEmployee(
            @AuthenticationPrincipal Jwt jwt) {

        String keycloakId = jwt.getSubject();

        System.out.println("JWT Keycloak ID: " + keycloakId);

        Optional<Employee> employee =
                employeeRepository.findByKeycloakId(keycloakId);

        System.out.println("Employee found: " + employee.isPresent());

        return employee
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new RuntimeException("Employee Not found"));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'TRAINING_MANAGER')")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeRepository.findAll());
    }

    @PostMapping("/sync")
    public ResponseEntity<String> syncEmployees() {

        syncService.syncUsers();

        return ResponseEntity.ok(
                "Keycloak users synchronized successfully"
        );
    }
}
