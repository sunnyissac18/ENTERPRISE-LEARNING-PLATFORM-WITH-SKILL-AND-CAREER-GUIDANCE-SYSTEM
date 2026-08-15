package com.skillsphere.learning_service.service;


import com.skillsphere.learning_service.entity.Course;
import com.skillsphere.learning_service.entity.LearningPath;
import com.skillsphere.learning_service.entity.LearningPathCourse;
import com.skillsphere.learning_service.repository.CourseRepository;
import com.skillsphere.learning_service.repository.LearningPathCourseRepository;
import com.skillsphere.learning_service.repository.LearningPathRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LearningPathService {

    private final LearningPathRepository pathRepository;
    private final CourseRepository courseRepository;
    private final LearningPathCourseRepository pathCourseRepository;


    public LearningPath createPath(LearningPath path) {
        path.setProgress(0);
        path.setActive(true);
        return pathRepository.save(path);
    }

    public LearningPath addCourseToPath(UUID pathId, UUID courseId, Integer sequence) {
        LearningPath path = pathRepository.findById(pathId)
                .orElseThrow(() -> new RuntimeException("Learning path not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        LearningPathCourse pathCourse =LearningPathCourse.builder()
                .learningPath(path)
                .course(course)
                .sequenceOrder(sequence)
                .build();
        pathCourseRepository.save(pathCourse);
        return path;
    }

    public List<LearningPathCourse> getPathCourses(UUID pathId) {
        return pathCourseRepository.findByLearningPathPathIdOrderBySequenceOrder(pathId);
    }
}
