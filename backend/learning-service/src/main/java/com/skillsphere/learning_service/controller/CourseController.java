package com.skillsphere.learning_service.controller;


import com.skillsphere.learning_service.dto.CourseDto;
import com.skillsphere.learning_service.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/learning/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public List<CourseDto> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public CourseDto getCourse(@PathVariable UUID id) {
        return courseService.getCourseById(id);
    }

    @PostMapping
    public CourseDto createCourse(@RequestBody CourseDto dto) {
        return courseService.createCourse(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable UUID id) {
        courseService.deleteCourse(id);
    }
}


