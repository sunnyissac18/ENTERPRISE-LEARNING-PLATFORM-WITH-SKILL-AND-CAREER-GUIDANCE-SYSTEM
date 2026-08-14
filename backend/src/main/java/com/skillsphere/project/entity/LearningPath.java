package com.skillsphere.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "learning_path")
@Builder
public class LearningPath {

    @Id
    @GeneratedValue
    private UUID pathId;

    @Column(nullable = false)
    private String pathName;
    private String desc;
    private Integer progress;
    private Boolean active;

}
