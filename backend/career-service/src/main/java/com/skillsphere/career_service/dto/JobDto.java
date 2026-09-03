package com.skillsphere.career_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobDto {

    private UUID jobId;
    private String title;
    private String department;
    private String requiredSkills;
    private Integer minimumExperience;
    private Boolean active;
}
