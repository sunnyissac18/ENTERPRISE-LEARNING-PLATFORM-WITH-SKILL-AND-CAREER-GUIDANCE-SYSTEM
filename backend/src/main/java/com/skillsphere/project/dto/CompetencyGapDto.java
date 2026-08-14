package com.skillsphere.project.dto;

import lombok.*;

@Data
@Builder
public class CompetencyGapDto {

    private String skillName;
    private Integer currentProficiency;
    private Integer requiredProficiency;
    private Integer gap;
}