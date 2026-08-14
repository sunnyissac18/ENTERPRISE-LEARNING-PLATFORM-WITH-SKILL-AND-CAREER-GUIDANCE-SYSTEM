package com.skillsphere.project.dto;

import com.skillsphere.project.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDto {

    private UUID courseId;
    private String title;
    private String description;
    private Integer duration;
    private Course.CourseType type;
    private String instructor;
    private Double rating;
    private Boolean active;
}
