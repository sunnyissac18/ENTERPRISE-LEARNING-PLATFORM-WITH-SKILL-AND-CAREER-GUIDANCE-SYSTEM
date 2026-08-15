package com.skillsphere.learning_service.controller;


import com.skillsphere.learning_service.dto.EnrollmentDto;
import com.skillsphere.learning_service.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/learning/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    public EnrollmentDto enroll(
            @RequestParam UUID empId,
            @RequestParam UUID courseId) {
        return enrollmentService.enroll(empId, courseId);
    }

    @GetMapping("/employee/{empId}")
    public List<EnrollmentDto> getEmployeeEnrollments(
            @PathVariable UUID empId) {
        return enrollmentService.getEmployeeEnrollments(empId);
    }
}
