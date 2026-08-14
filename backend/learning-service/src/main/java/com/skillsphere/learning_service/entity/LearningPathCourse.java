package com.skillsphere.learning_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "learning_path_course")
@Builder
@Getter
@Setter
public class LearningPathCourse {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "path_id")
    private LearningPath learningPath;

    private Integer sequenceOrder;

}
