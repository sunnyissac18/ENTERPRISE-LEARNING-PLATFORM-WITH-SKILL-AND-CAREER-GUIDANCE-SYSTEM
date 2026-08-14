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
import lombok.RequiredArgsConstructor;
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

@Cacheable(value = "skillProfiles", key = "#empId")
public Map<String,Object> getSkillProfile(UUID empId) {

    Employee employee = employeeRepository.findById(empId)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + empId));

    List<EmployeeSkill> skills = employeeSkillRepository.findByEmployeeEmpId(empId);
    List<Certification> certs = certificationRepository.findByEmployeeEmpId(empId);
    List<Assessment> assessments = assessmentRepository.findByEmployeeEmpId(empId);

    return java.util.Map.of(
            "employee", employee,
            "skills", skills,
            "certifications", certs,
            "assessments", assessments
    );

    }
}