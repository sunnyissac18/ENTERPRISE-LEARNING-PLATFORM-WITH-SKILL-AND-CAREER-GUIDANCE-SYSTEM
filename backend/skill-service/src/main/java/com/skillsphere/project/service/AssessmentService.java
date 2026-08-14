package com.skillsphere.project.service;

import com.skillsphere.project.dto.AssessmentDto;
import com.skillsphere.project.entity.Assessment;
import com.skillsphere.project.entity.Employee;
import com.skillsphere.project.entity.Skill;
import com.skillsphere.project.repository.AssessmentRepository;
import com.skillsphere.project.repository.EmployeeRepository;
import com.skillsphere.project.repository.SkillRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final EmployeeRepository employeeRepository;
    private final SkillRepository skillRepository;

    private static final float PASS_THRESHOLD = 70.0f;

    public AssessmentDto createAssessment(AssessmentDto dto) {

        Employee employee = employeeRepository.findById(dto.getEmpId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Skill skill = skillRepository.findById(dto.getSkillId())
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        boolean passed = dto.getScore() >= PASS_THRESHOLD;

        Assessment assessment = Assessment.builder()
                .employee(employee)
                .skill(skill)
                .score(dto.getScore())
                .passed(passed)
                .verified(false)
                .build();

        Assessment saved = assessmentRepository.save(assessment);

        return AssessmentDto.builder()
                .assessId(saved.getAssessId())
                .empId(employee.getEmpId())
                .skillId(skill.getSkillId())
                .score(saved.getScore())
                .passed(saved.getPassed())
                .verified(saved.getVerified())
                .build();
    }

    public AssessmentDto verifyAssessment(java.util.UUID assessId) {

        Assessment assessment = assessmentRepository.findById(assessId)
                .orElseThrow(() -> new RuntimeException("Assessment not found"));

        assessment.setVerified(true);
        assessmentRepository.save(assessment);

        return AssessmentDto.builder()
                .assessId(assessment.getAssessId())
                .score(assessment.getScore())
                .passed(assessment.getPassed())
                .verified(true)
                .build();
    }
}