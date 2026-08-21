package com.skillsphere.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RenewalDTO {

    private UUID renewalId;
    private UUID certificationId;
    private LocalDate oldExpiry;
    private LocalDate newExpiry;
    private String status;
    private String requestedBy;
    private String approvedBy;

}