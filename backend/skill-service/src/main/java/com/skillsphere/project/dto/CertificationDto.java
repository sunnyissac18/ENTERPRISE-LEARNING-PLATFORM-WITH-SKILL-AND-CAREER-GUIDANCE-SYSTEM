package com.skillsphere.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CertificationDto {

    private UUID certId;
    private UUID empId;
    private String certificationName;
    private UUID skillId;
    private LocalDate issueDate;
    private LocalDate expiryDate;

}
