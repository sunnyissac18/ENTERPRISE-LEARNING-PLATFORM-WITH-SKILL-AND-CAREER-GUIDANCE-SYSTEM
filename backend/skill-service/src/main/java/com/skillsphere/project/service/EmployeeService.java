package com.skillsphere.project.service;

import com.skillsphere.project.entity.Employee;
import com.skillsphere.project.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public Employee getOrCreateEmployee(Jwt jwt) {

        String keycloakId = jwt.getSubject();

        return employeeRepository
                .findByKeycloakId(keycloakId)
                .orElseGet(() -> {

                    String fullName = buildFullName(jwt);

                    Employee employee = Employee.builder()
                            .keycloakId(keycloakId)
                            .fullName(fullName)
                            .build();

                    return employeeRepository.save(employee);
                });
    }

    private String buildFullName(Jwt jwt) {

        String firstName = jwt.getClaimAsString("given_name");
        String lastName = jwt.getClaimAsString("family_name");

        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        }

        if (firstName != null) {
            return firstName;
        }

        String username = jwt.getClaimAsString("preferred_username");

        if (username != null) {
            return username;
        }

        return "User";
    }
}