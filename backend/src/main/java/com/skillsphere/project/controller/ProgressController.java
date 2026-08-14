package com.skillsphere.project.controller;

import com.skillsphere.project.entity.Enrollment;
import com.skillsphere.project.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/learning/progress")
@RequiredArgsConstructor
public class ProgressController {

    private final ProgressService progressService;

    @PutMapping("/{enrollmentId}")
    public Enrollment updateProgress(
            @PathVariable UUID enrollmentId,
            @RequestParam Integer progress) {
        return progressService.updateProgress(
                enrollmentId, progress);
    }

    @PostMapping("/{enrollmentId}/assessment")
    public Enrollment submitAssessment(
            @PathVariable UUID enrollmentId,
            @RequestParam Float score) {
        return progressService.submitAssessment(
                enrollmentId, score);
    }

    @PostMapping("/{enrollmentId}/complete")
    public Enrollment completeCourse(
            @PathVariable UUID enrollmentId) {
        return progressService.completeCourse(enrollmentId);
    }

}
