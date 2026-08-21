package com.skillsphere.project.controller;

import com.skillsphere.project.entity.CertificationAudit;
import com.skillsphere.project.service.CertificationAuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/certifications")
@RequiredArgsConstructor
public class CertificationAuditController {

    private final CertificationAuditService auditService;

    @GetMapping("/{certificationId}/audit")
    public List<CertificationAudit> getAudit(
            @PathVariable UUID certificationId){

        return auditService.getAudit(certificationId);
    }
}
