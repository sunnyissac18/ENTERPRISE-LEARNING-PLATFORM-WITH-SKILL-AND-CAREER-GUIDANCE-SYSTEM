package com.skillsphere.career_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "career_plans")
@Builder
@Getter
@Setter
public class CareerPlan {

    @Id
    @GeneratedValue
    private UUID planId;

    @Column(nullable = false)
    private UUID empId;

    private String employeeName;

    @Column(name = "current_role_name")
    private String currentRole;
    private String targetRole;
    private Integer progress;
    private String mentor;
    private String skillGaps;
    private String trainingPlan;
    private Integer promotionScore;
    private Boolean promotionEligible;

    @Enumerated(EnumType.STRING)
    private PlanStatus status;

    public enum PlanStatus {
        ACTIVE, COMPLETED, ON_HOLD

    }
}
