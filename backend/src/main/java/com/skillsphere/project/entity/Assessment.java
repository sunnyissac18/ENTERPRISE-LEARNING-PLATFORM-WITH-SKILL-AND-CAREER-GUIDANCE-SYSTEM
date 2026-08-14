package com.skillsphere.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "assessment")
@Builder
public class Assessment {

    @Id
    @GeneratedValue
    private UUID assessId;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;

    private	Float	score;
    private	Boolean	passed;
    private	Boolean	verified;
}
