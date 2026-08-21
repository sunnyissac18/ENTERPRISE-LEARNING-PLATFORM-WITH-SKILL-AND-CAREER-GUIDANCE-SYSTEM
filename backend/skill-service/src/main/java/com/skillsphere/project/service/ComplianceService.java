package com.skillsphere.project.service;

import com.skillsphere.project.dto.ComplianceDTO;
import com.skillsphere.project.entity.Certification;
import com.skillsphere.project.entity.Employee;
import com.skillsphere.project.repository.CertificationRepository;
import com.skillsphere.project.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ComplianceService {

    private final EmployeeRepository employeeRepository;
    private final CertificationRepository certificationRepository;

    public ComplianceDTO getCompliance(UUID empId){

        Employee employee =employeeRepository.findById(empId)
                .orElseThrow(()->new RuntimeException("Employee Not Found"));

        List<Certification> certifications= certificationRepository.findByEmployeeEmpId(empId);

        long total=certifications.size();
        long valid=certifications.stream()
                .filter(c->c.getStatus()==Certification.Status.VALID)
                .count();

        long expired= certifications.stream()
                .filter(c->c.getStatus()==Certification.Status.EXPIRED)
                .count();

        return ComplianceDTO.builder()
                .employeeName(employee.getFullName())
                .totalCertifications(total)
                .validCertifications(valid)
                .expiredCertifications(expired)
                .compliant(total>0 && expired==0)
                .build();
    }

}
