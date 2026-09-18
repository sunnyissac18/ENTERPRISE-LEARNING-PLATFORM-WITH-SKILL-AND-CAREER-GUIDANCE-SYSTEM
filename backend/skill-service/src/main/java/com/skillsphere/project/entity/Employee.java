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
        TRAINING_MANAGER,
        SOFTWARE_ENGINEER,
        SENIOR_SOFTWARE_ENGINEER,
        QA_ENGINEER,
        SYSTEM_ADMINISTRATOR,
        DATA_SCIENTIST,
        DATA_ANALYST,
        PRODUCT_MANAGER,
        PROJECT_MANAGER,
        BUSINESS_ANALYST,
        UX_DESIGNER,
        UI_DESIGNER,
        SALES_EXECUTIVE,
        MARKETING_SPECIALIST,
        CUSTOMER_SUPPORT,
        FINANCE_ANALYST,
        ACCOUNTANT,
        DIRECTOR,
        VICE_PRESIDENT,
        CEO,
        CTO,
        CFO,
        COO
    }
}
