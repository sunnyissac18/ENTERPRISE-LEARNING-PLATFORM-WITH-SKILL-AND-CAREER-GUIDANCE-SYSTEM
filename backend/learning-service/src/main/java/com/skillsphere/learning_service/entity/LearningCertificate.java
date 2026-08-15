package com.skillsphere.learning_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "learning_certificates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LearningCertificate {

    @Id
    @GeneratedValue
    private UUID certificateId;

    private UUID empId;
    private UUID courseId;
    private String courseName;
    private Float score;
    private LocalDate issuedDate;
    private String certificateNumber;
}