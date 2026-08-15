package com.skillsphere.learning_service.controller;


import com.skillsphere.learning_service.entity.LearningPath;
import com.skillsphere.learning_service.entity.LearningPathCourse;
import com.skillsphere.learning_service.service.LearningPathService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/learning/paths")
@RequiredArgsConstructor
public class LearningPathController {

    private final LearningPathService learningPathService;

    @PostMapping
    public LearningPath createPath(
            @RequestBody LearningPath path) {

        return learningPathService.createPath(path);
    }

    @PostMapping("/{pathId}/courses/{courseId}")
    public LearningPath addCourse(
            @PathVariable UUID pathId,
            @PathVariable UUID courseId,
            @RequestParam Integer sequence) {

        return learningPathService.addCourseToPath(
                pathId, courseId, sequence);
    }

    @GetMapping("/{pathId}/courses")
    public List<LearningPathCourse> getCourses(
            @PathVariable UUID pathId) {

        return learningPathService.getPathCourses(pathId);

    }
}
