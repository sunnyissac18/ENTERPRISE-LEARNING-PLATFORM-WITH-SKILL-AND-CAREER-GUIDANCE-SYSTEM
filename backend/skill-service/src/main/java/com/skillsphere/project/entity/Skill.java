package com.skillsphere.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "skills")
@Builder
public class Skill {

    @Id
    @GeneratedValue
    private UUID skillId;

    @Column(name = "skill_name")
    private String name;

    @Enumerated(EnumType.STRING)
    private Category category;

    public enum Category {
        TECHNICAL, DOMAIN, SOFT

    }
}
