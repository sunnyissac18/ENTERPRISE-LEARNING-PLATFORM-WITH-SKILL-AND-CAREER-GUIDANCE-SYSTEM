package com.skillsphere.project.controller;

import com.skillsphere.project.dto.ComplianceDTO;
import com.skillsphere.project.service.ComplianceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/certifications/compliance")
@RequiredArgsConstructor
public class CertificationComplianceController {

    private final ComplianceService service;

    @GetMapping("{empId}")
    public ComplianceDTO getCompliance(
            @PathVariable UUID empId){

        return service.getCompliance(empId);
    }
}
