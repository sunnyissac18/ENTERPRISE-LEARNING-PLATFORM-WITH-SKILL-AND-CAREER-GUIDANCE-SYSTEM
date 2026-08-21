package com.skillsphere.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificationReportDto {

    private long total;
    private long active;
    private long expired;
    private long pendingRenewal;
    private long expiringWithin30Days;
    private double renewalRate;
}
