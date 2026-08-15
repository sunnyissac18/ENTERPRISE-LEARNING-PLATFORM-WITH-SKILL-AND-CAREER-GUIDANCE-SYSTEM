package com.skillsphere.learning_service.service;

import com.skillsphere.learning_service.entity.Enrollment;
import com.skillsphere.learning_service.entity.LearningCertificate;
import com.skillsphere.learning_service.repository.EnrollmentRepository;
import com.skillsphere.learning_service.repository.LearningCertificateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LearningCertificateService {

    private final LearningCertificateRepository certificateRepository;
    private final EnrollmentRepository enrollmentRepository;

    public LearningCertificate generateCertificate(
            UUID enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        if (!Boolean.TRUE.equals(enrollment.getCompleted())) {
            throw new RuntimeException("Course is not completed");
        }

        LearningCertificate certificate = LearningCertificate.builder()
                        .empId(enrollment.getEmpId())
                        .courseId(enrollment.getCourse().getCourseId())
                        .courseName(enrollment.getCourse().getTitle())
                        .score(enrollment.getScore())
                        .issuedDate(LocalDate.now())
                        .certificateNumber("SS-" + UUID.randomUUID())
                        .build();

        return certificateRepository.save(certificate);
    }
}
