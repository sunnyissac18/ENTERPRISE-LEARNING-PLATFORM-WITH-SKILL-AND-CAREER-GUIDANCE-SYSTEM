package com.skillsphere.career_service.dto;

import com.skillsphere.career_service.entity.CareerPlan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CareerPlanDto {

    private UUID planId;
    private UUID empId;
    private String employeeName;
    private String currentRole;
    private String targetRole;
    private Integer progress;
    private String mentor;
    private String skillGaps;
    private String trainingPlan;
    private Integer promotionScore;
    private Boolean promotionEligible;
    private String status;
}
