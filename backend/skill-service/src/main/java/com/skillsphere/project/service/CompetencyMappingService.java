package com.skillsphere.project.service;

import com.skillsphere.project.dto.CompetencyGapDto;
import com.skillsphere.project.entity.CompetencyFramework;
import com.skillsphere.project.entity.EmployeeSkill;
import com.skillsphere.project.repository.CompetencyFrameworkRepository;
import com.skillsphere.project.repository.EmployeeSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompetencyMappingService {

    private final EmployeeSkillRepository employeeSkillRepository;
    private final CompetencyFrameworkRepository competencyFrameworkRepository;

    public List<CompetencyGapDto> getGapsForRole(
            UUID empId,
            String targetRoleTitle) {

        List<EmployeeSkill> currentSkills =
                employeeSkillRepository.findByEmployeeEmpId(empId);

        List<CompetencyFramework> requirements =
                competencyFrameworkRepository.findByRoleTitle(targetRoleTitle);

        return requirements.stream()
                .map(req -> {

                    int current = currentSkills.stream()
                            .filter(es -> es.getSkill()
                                    .getSkillId()
                                    .equals(req.getSkill().getSkillId()))
                            .findFirst()
                            .map(EmployeeSkill::getProficiency)
                            .orElse(0);

                    return CompetencyGapDto.builder()
                            .skillName(req.getSkill().getName())
                            .currentProficiency(current)
                            .requiredProficiency(
                                    req.getRequiredProficiency())
                            .gap(req.getRequiredProficiency() - current)
                            .build();
                })
                .collect(Collectors.toList());
    }
}