package com.skillsphere.project.service;

import com.skillsphere.project.dto.CourseDto;
import com.skillsphere.project.entity.Course;
import com.skillsphere.project.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public List<CourseDto> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public CourseDto getCourseById(UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new RuntimeException("Course not found"));
        return toDto(course);
    }

    public CourseDto createCourse(CourseDto dto) {
        Course course = Course.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .duration(dto.getDuration())
                .type(dto.getType())
                .instructor(dto.getInstructor())
                .rating(dto.getRating())
                .active(true)
                .build();
        return toDto(courseRepository.save(course));
    }

    public void deleteCourse(UUID courseId) {
        courseRepository.deleteById(courseId);
    }

    private CourseDto toDto(Course course) {
        return CourseDto.builder()
                .courseId(course.getCourseId())
                .title(course.getTitle())
                .description(course.getDescription())
                .duration(course.getDuration())
                .type(course.getType())
                .instructor(course.getInstructor())
                .rating(course.getRating())
                .active(course.getActive())
                .build();

    }
}
