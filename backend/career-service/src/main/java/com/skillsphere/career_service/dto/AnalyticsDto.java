package com.skillsphere.career_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

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
    private List<Map<String, Object>> skillGaps;
}
