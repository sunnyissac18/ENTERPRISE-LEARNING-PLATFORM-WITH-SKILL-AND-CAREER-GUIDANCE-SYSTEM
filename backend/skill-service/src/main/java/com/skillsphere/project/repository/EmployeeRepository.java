package com.skillsphere.project.repository;

import com.skillsphere.project.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    Optional<Employee> findByKeycloakId(String keycloakId);

    boolean existsByKeycloakId(String keycloakId);
}