package com.skillsphere.project.repository;

import com.skillsphere.project.entity.CertificationAudit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CertificationAuditRepository extends JpaRepository<CertificationAudit, UUID> {

    List<CertificationAudit> findByCertificationIdOrderByPerformedAtDesc(UUID certificationId);
}