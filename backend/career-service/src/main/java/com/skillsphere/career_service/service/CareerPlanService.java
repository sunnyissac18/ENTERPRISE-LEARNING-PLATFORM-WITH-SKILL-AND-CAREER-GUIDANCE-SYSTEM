package com.skillsphere.career_service.service;

import com.skillsphere.career_service.dto.CareerPlanDto;
import com.skillsphere.career_service.entity.CareerPlan;
import com.skillsphere.career_service.repository.CareerPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CareerPlanService {

    private final CareerPlanRepository repository;

    private CareerPlanDto toDTO(CareerPlan p) {
        return CareerPlanDto.builder()
                .planId(p.getPlanId()).empId(p.getEmpId())
                .employeeName(p.getEmployeeName())
                .currentRole(p.getCurrentRole()).targetRole(p.getTargetRole())
                .progress(p.getProgress()).mentor(p.getMentor())
                .skillGaps(p.getSkillGaps()).trainingPlan(p.getTrainingPlan())
                .promotionScore(p.getPromotionScore())
                .promotionEligible(p.getPromotionEligible())
                .status(p.getStatus().name()).build();
    }

    private int calculatePromotionScore(CareerPlanDto dto) {
        int score = 0;
        if (dto.getProgress() != null)
            score += (int)(dto.getProgress() * 0.6);
        if (dto.getSkillGaps() == null || dto.getSkillGaps().isBlank())
            score += 20;
        if (dto.getTrainingPlan() != null && !dto.getTrainingPlan().isBlank())
            score += 20;
        return Math.min(score, 100);
    }


    public List<CareerPlanDto> getAll(){

        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public CareerPlanDto getById(UUID id){

        CareerPlan plan = repository.findById(id)
                .orElseThrow(()-> new RuntimeException("Career plan Not found"));

        return toDTO(plan);

    }

    public List<CareerPlanDto> getByEmployee(UUID empId){

        return repository.findByEmpId(empId)
                .stream()
                .map(this::toDTO)
                .toList();
    }



    public CareerPlanDto update(UUID id, CareerPlanDto dto){

        CareerPlan plan = repository.findById(id)
                .orElseThrow(()-> new RuntimeException("Career plan not Found"));

        plan.setCurrentRole(dto.getCurrentRole());
        plan.setTargetRole(dto.getTargetRole());
        plan.setProgress(dto.getProgress());
        plan.setMentor(dto.getMentor());
        plan.setSkillGaps(dto.getSkillGaps());
        plan.setTrainingPlan(dto.getTrainingPlan());

        int score= calculatePromotionScore(dto);

        plan.setPromotionScore(score);
        plan.setPromotionEligible(score >= 80);

        CareerPlan saved = repository.save(plan);
        return toDTO(saved);
    }

    public CareerPlanDto create(CareerPlanDto dto){

        int score = calculatePromotionScore(dto);

        CareerPlan plan = CareerPlan.builder()
                .empId(dto.getEmpId())
                .employeeName(dto.getEmployeeName())
                .currentRole(dto.getCurrentRole())
                .targetRole(dto.getTargetRole())
                .progress(dto.getProgress())
                .mentor(dto.getMentor())
                .skillGaps(dto.getSkillGaps())
                .trainingPlan(dto.getTrainingPlan())
                .promotionScore(score)
                .promotionEligible(score>=80)
                .status(CareerPlan.PlanStatus.ACTIVE)
                .build();

        CareerPlan saved= repository.save(plan);
        return toDTO(saved);

    }

    public void delete(UUID id){

        if(!repository.existsById(id)){
            throw new RuntimeException("Career plan not found");
        }
        repository.deleteById(id);
    }
}
