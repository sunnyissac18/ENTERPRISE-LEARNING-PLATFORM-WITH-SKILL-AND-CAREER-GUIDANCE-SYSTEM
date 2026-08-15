package com.skillsphere.learning_service.controller;

import com.skillsphere.learning_service.entity.LearningCertificate;
import com.skillsphere.learning_service.service.LearningCertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/learning/certificates")
@RequiredArgsConstructor
public class LearningCertificateController {

    private final LearningCertificateService certificateService;

    @PostMapping("/{enrollmentId}")
    public LearningCertificate generateCertificate(
            @PathVariable UUID enrollmentId) {

        return certificateService.generateCertificate(enrollmentId);
    }
}
