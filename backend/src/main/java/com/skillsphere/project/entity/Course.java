package com.skillsphere.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "course")
@Builder
public class Course {

    @Id
    @GeneratedValue
    private UUID courseId;

    @Column(nullable = false)
    private String title;

    private String description;
    private Integer duration;

    @Enumerated(EnumType.STRING)
    private CourseType type;
    private String instructor;
    private Double rating;
    private Boolean active;

    public enum CourseType {
        ONLINE, WORKSHOP, WEBINAR, BOOTCAMP

    }
}
