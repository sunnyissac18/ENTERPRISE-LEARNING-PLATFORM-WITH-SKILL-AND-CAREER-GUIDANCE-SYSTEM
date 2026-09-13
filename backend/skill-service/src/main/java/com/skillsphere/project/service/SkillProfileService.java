package com.skillsphere.project.service;
import com.skillsphere.project.entity.Assessment;
import com.skillsphere.project.entity.Certification;
import com.skillsphere.project.entity.Employee;
import com.skillsphere.project.entity.EmployeeSkill;
import com.skillsphere.project.exception.ResourceNotFoundException;
import com.skillsphere.project.repository.AssessmentRepository;
import com.skillsphere.project.repository.CertificationRepository;
import com.skillsphere.project.repository.EmployeeRepository;
import com.skillsphere.project.repository.EmployeeSkillRepository;
import com.skillsphere.project.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class SkillProfileService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeSkillRepository employeeSkillRepository;
    private final AssessmentRepository assessmentRepository;
    private final CertificationRepository certificationRepository;
    private final SkillRepository skillRepository;

    private Employee resolveEmployee(UUID id) {
        return employeeRepository.findById(id)
                .orElseGet(() -> employeeRepository.findByKeycloakId(id.toString())
                        .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + id)));
    }

    @Cacheable(value = "skillProfiles", key = "#empId")
    public Map<String,Object> getSkillProfile(UUID empId) {

        Employee employee = resolveEmployee(empId);
        UUID internalId = employee.getEmpId();

        List<EmployeeSkill> skills = employeeSkillRepository.findByEmployeeEmpId(internalId);
        List<Certification> certs = certificationRepository.findByEmployeeEmpId(internalId);
        List<Assessment> assessments = assessmentRepository.findByEmployeeEmpId(internalId);

        return java.util.Map.of(
                "employee", employee,
                "skills", skills,
                "certifications", certs,
                "assessments", assessments
        );

    }

    @CacheEvict(value = "skillProfiles", key = "#empId")
    public EmployeeSkill addSkillToProfile(UUID empId, UUID skillId) {
        Employee employee = resolveEmployee(empId);
        UUID internalId = employee.getEmpId();

        com.skillsphere.project.entity.Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found: " + skillId));

        // Check if employee already has this skill
        List<EmployeeSkill> existingSkills = employeeSkillRepository.findByEmployeeEmpId(internalId);
        for (EmployeeSkill es : existingSkills) {
            if (es.getSkill().getSkillId().equals(skillId)) {
                return es; // Already added
            }
        }

        EmployeeSkill employeeSkill = EmployeeSkill.builder()
                .employee(employee)
                .skill(skill)
                .proficiency(1) // Default to 1 (Beginner)
                .build();

        return employeeSkillRepository.save(employeeSkill);
    }

    @CacheEvict(value = "skillProfiles", key = "#empId")
    public EmployeeSkill updateSkillProficiency(UUID empId, UUID skillId, int proficiency) {
        if (proficiency < 1 || proficiency > 100) {
            throw new IllegalArgumentException("Proficiency must be between 1 and 100");
        }
        
        Employee employee = resolveEmployee(empId);
        UUID internalId = employee.getEmpId();

        List<EmployeeSkill> existingSkills = employeeSkillRepository.findByEmployeeEmpId(internalId);
        for (EmployeeSkill es : existingSkills) {
            if (es.getSkill().getSkillId().equals(skillId)) {
                es.setProficiency(proficiency);
                return employeeSkillRepository.save(es);
            }
        }
        throw new ResourceNotFoundException("Skill " + skillId + " not found in profile for employee " + empId);
    }
}

