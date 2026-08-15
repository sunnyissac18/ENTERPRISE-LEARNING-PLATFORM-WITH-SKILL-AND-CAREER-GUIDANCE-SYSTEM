package com.skillsphere.learning_service.service;


import com.skillsphere.learning_service.dto.EnrollmentDto;
import com.skillsphere.learning_service.entity.Course;
import com.skillsphere.learning_service.entity.Enrollment;
import com.skillsphere.learning_service.repository.CourseRepository;
import com.skillsphere.learning_service.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentDto enroll(UUID empId, UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Enrollment enrollment = Enrollment.builder()
                .empId(empId)
                .course(course)
                .enrolledAt(LocalDateTime.now())
                .progress(0)
                .completed(false)
                .score(0.0f)
                .build();
        return toDto(enrollmentRepository.save(enrollment));
    }

    public List<EnrollmentDto>
    getEmployeeEnrollments(UUID empId) {
        return enrollmentRepository.findByEmpId(empId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    private EnrollmentDto toDto(Enrollment e) {

        return EnrollmentDto.builder()
                .enrollmentId(e.getEnrollmentId())
                .empId(e.getEmpId())
                .courseId(e.getCourse().getCourseId())
                .enrolledAt(e.getEnrolledAt())
                .progress(e.getProgress())
                .completed(e.getCompleted())
                .score(e.getScore())
                .completedAt(e.getCompletedAt())
                .build();
    }
}
