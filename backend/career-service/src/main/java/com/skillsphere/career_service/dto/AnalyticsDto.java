package com.skillsphere.career_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsDto {

    private long totalCareerPlans;
    private long activeCareerPlans;
    private long completedPlans;
    private long promotionEligible;
    private double averageProgress;
    private double skillCoverage;
    private long activeJobs;

}
