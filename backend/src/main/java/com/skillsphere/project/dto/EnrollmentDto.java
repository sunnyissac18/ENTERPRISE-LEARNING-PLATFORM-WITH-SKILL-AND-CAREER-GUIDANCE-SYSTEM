package com.skillsphere.project.dto;

import com.skillsphere.project.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentDto {

    private UUID enrollmentId;
    private UUID empId;
    private Course course;
    private LocalDateTime enrolledAt;
    private Integer progress;
    private Boolean completed;
    private Float score;
    private LocalDateTime completedAt;
}
