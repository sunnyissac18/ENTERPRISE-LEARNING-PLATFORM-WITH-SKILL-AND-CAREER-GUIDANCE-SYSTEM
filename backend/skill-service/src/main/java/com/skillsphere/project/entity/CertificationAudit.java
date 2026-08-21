package com.skillsphere.project.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "certification_audits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificationAudit {

    @Id
    @GeneratedValue
    private UUID auditId;
    private UUID certificationId;
    private UUID employeeId;
    private String action;
    private String performedBy;
    private LocalDateTime performedAt;
}
