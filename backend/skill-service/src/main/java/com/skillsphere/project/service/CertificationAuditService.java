package com.skillsphere.project.service;

import com.skillsphere.project.entity.CertificationAudit;
import com.skillsphere.project.repository.CertificationAuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CertificationAuditService {

    private final CertificationAuditRepository repository;

    public void log(
            UUID certificationId,
            UUID employeeId,
            String action,
            String performedBy) {
        CertificationAudit audit =
                CertificationAudit.builder()
                        .certificationId(certificationId)
                        .employeeId(employeeId)
                        .action(action)
                        .performedBy(performedBy)
                        .performedAt(LocalDateTime.now())
                        .build();
        repository.save(audit);
    }

    public List<CertificationAudit> getAudit(UUID certificationId) {

        return repository.findByCertificationIdOrderByPerformedAtDesc(certificationId);
    }
}