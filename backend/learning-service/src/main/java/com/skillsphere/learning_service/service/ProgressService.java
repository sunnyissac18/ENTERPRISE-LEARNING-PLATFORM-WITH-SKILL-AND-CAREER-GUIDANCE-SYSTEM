package com.skillsphere.learning_service.service;


import com.skillsphere.learning_service.entity.Enrollment;
import com.skillsphere.learning_service.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final EnrollmentRepository enrollmentRepository;

    public Enrollment updateProgress(
            UUID enrollmentId, Integer progress) {

        if (progress < 0 || progress > 100) {
            throw new IllegalArgumentException("Progress must be between 0 and 100");
        }

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        enrollment.setProgress(progress);

        if (progress == 100) {
            enrollment.setCompleted(true);
            enrollment.setCompletedAt(LocalDateTime.now());
        }
        return enrollmentRepository.save(enrollment);
    }

    public Enrollment submitAssessment(UUID enrollmentId, Float score) {
        if (score < 0 || score > 100) {
            throw new IllegalArgumentException("Score must be between 0 and 100");
        }

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        enrollment.setScore(score);
        return enrollmentRepository.save(enrollment);
    }

    public Enrollment completeCourse(UUID enrollmentId) {

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        enrollment.setProgress(100);
        enrollment.setCompleted(true);
        enrollment.setCompletedAt(LocalDateTime.now());
        return enrollmentRepository.save(enrollment);

    }
}
