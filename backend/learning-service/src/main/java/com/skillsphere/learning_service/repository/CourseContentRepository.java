package com.skillsphere.learning_service.repository;

import com.skillsphere.learning_service.entity.CourseContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CourseContentRepository extends JpaRepository<CourseContent, UUID> {
    List<CourseContent>
    findByCourseCourseIdOrderBySequenceOrder(UUID courseId);
}
