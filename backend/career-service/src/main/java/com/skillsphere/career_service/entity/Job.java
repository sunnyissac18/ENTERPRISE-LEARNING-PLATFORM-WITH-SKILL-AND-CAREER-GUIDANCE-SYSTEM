package com.skillsphere.career_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "internal_jobs")
@Builder
@Getter
@Setter
public class Job {

    @Id
    @GeneratedValue
    private UUID jobId;

    private String title;
    private String department;
    private String requiredSkills;
    private Integer minimumExperience;
    private Boolean active;
}
