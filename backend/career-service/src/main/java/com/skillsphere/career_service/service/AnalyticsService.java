package com.skillsphere.career_service.service;

import com.skillsphere.career_service.dto.AnalyticsDto;
import com.skillsphere.career_service.entity.CareerPlan;
import com.skillsphere.career_service.repository.CareerPlanRepository;
import com.skillsphere.career_service.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final CareerPlanRepository planRepository;
    private final JobRepository jobRepository;

    public AnalyticsDto getAnalytics(){

        var plans = planRepository.findAll();

        double averageProgress = plans.stream()
                .filter(p -> p.getProgress() != null)
                .mapToInt(CareerPlan::getProgress)
                .average().orElse(0);
        return AnalyticsDto.builder()
                .totalCareerPlans(plans.size())
                .activeCareerPlans(planRepository.countByStatus(
                        CareerPlan.PlanStatus.ACTIVE))
                .completedPlans(planRepository.countByStatus(
                        CareerPlan.PlanStatus.COMPLETED))
                .promotionEligible(planRepository.countByPromotionEligibleTrue())
                .averageProgress(averageProgress)
                .skillCoverage(calculateSkillCoverage(plans))
                .activeJobs(jobRepository.findByActiveTrue().size())
                .skillGaps(calculateTopSkillGaps(plans))
                .build();
    }
    private double calculateSkillCoverage(
            java.util.List<CareerPlan> plans) {
        if (plans.isEmpty()) return 0;
        long withoutGap = plans.stream()
                .filter(p -> p.getSkillGaps() == null ||
                        p.getSkillGaps().isBlank())
                .count();
        return (withoutGap * 100.0) / plans.size();
    }

    private List<Map<String, Object>> calculateTopSkillGaps(List<CareerPlan> plans) {
        Map<String, Integer> gapCounts = new HashMap<>();
        
        for (CareerPlan plan : plans) {
            String gaps = plan.getSkillGaps();
            if (gaps != null && !gaps.isBlank()) {
                String[] skills = gaps.split(",");
                for (String skill : skills) {
                    String trimmedSkill = skill.trim();
                    if (!trimmedSkill.isEmpty()) {
                        gapCounts.put(trimmedSkill, gapCounts.getOrDefault(trimmedSkill, 0) + 1);
                    }
                }
            }
        }
        
        return gapCounts.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", entry.getKey());
                    map.put("gap", entry.getValue());
                    return map;
                })
                .sorted((a, b) -> Integer.compare((Integer) b.get("gap"), (Integer) a.get("gap")))
                .limit(5)
                .collect(Collectors.toList());
    }
}
