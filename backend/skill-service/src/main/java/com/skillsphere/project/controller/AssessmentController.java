package com.skillsphere.project.controller;

import com.skillsphere.project.dto.AssessmentDto;
import com.skillsphere.project.service.AssessmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/assessments")
@RequiredArgsConstructor
public class AssessmentController {

    private final AssessmentService assessmentService;

    @PostMapping("/assess")
    @PreAuthorize("hasRole('HR')")
    public AssessmentDto createAssessment(@RequestBody AssessmentDto dto) {
        return assessmentService.createAssessment(dto);
    }

    @PutMapping("/{id}/verify")
    @PreAuthorize("hasRole('HR')")
    public AssessmentDto verifyAssessment(@PathVariable UUID id) {
        return assessmentService.verifyAssessment(id);
    }
}