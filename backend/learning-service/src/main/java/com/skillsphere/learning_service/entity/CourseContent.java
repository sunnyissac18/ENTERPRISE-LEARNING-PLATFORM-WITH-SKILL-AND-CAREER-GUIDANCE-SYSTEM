package com.skillsphere.learning_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "course_contents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseContent {

    @Id
    @GeneratedValue
    private UUID contentId;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    private String title;

    @Enumerated(EnumType.STRING)
    private ContentType type;

    private String contentUrl;
    private Integer duration;
    private Integer sequenceOrder;

    public enum ContentType {
        VIDEO, PDF, DOCUMENT, QUIZ, ASSIGNMENT
    }
}