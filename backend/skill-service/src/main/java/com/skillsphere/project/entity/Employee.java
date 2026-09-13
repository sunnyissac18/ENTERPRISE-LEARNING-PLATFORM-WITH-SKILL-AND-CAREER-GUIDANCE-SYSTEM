package com.skillsphere.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "employees",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_employee_keycloak_id", columnNames = "keycloak_id")
        }
)
public class Employee {

    @Id
    @GeneratedValue
    @Column(name = "emp_id")
    private UUID empId;

    @Column(name = "keycloak_id", nullable = false, unique = true)
    private String keycloakId;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Column(name = "department")
    private String dept;

    public enum Role {
        DEVELOPER,
        MANAGER,
        TECH_LEAD,
        HR,
        ADMIN,
        TRAINING_MANAGER
    }
}