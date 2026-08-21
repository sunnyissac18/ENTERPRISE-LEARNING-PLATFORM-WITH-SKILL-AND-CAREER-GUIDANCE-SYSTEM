package com.skillsphere.project.controller;

import com.skillsphere.project.dto.CertificationReportDto;
import com.skillsphere.project.service.CertificationReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/certifications/report")
@RequiredArgsConstructor
public class CertificationReportController {

    private final CertificationReportService service;

    @GetMapping()
    public CertificationReportDto reportDto(){
        return service.generate();
    }
}
