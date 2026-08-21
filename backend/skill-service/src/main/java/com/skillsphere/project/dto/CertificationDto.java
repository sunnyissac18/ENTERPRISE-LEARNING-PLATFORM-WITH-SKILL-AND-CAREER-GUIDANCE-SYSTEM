package com.skillsphere.project.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CertificationDto {

    private UUID certId;
    private UUID empId;
    private String employeeName;
    private String name;
    private String issuingOrganization;
    private String credentialId;
    private LocalDate issued;
    private LocalDate expiry;
    private String status;

}
