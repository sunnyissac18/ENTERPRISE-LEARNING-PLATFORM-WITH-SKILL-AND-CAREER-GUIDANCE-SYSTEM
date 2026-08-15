package com.skillsphere.learning_service.repository;

import com.skillsphere.learning_service.entity.LearningCertificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LearningCertificateRepository extends JpaRepository<LearningCertificate, UUID> {

}
