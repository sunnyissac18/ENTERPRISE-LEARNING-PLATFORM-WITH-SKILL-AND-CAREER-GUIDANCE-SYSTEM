package com.skillsphere.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplianceDTO {
    private String employeeName;
    private long totalCertifications;
    private long validCertifications;
    private long expiredCertifications;
    private boolean compliant;
}