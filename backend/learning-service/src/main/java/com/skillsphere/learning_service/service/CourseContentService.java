package com.skillsphere.learning_service.service;

import com.skillsphere.learning_service.entity.Course;
import com.skillsphere.learning_service.entity.CourseContent;
import com.skillsphere.learning_service.repository.CourseContentRepository;
import com.skillsphere.learning_service.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseContentService {

    private final CourseContentRepository courseContentRepository;
    private final CourseRepository courseRepository;

    public CourseContent addContent(
            UUID courseId, CourseContent content) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        content.setCourse(course);
        return courseContentRepository.save(content);
    }

    public List<CourseContent>
    getCourseContent(UUID courseId) {
        return courseContentRepository
                .findByCourseCourseIdOrderBySequenceOrder(courseId);
    }
}
