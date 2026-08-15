package com.skillsphere.learning_service.controller;

import com.skillsphere.learning_service.entity.CourseContent;
import com.skillsphere.learning_service.service.CourseContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/learning/content")
@RequiredArgsConstructor
public class CourseContentController {

    private final CourseContentService courseContentService;

    @PostMapping("/course/{courseId}")
    public CourseContent addContent(
            @PathVariable UUID courseId,
            @RequestBody CourseContent content) {
        return courseContentService.addContent(courseId, content);
    }

    @GetMapping("/course/{courseId}")
    public List<CourseContent> getContent(
            @PathVariable UUID courseId) {
        return courseContentService.getCourseContent(courseId);
    }
}
