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
@Table(name = "certification")
@Builder
public class Certification {

    @Id
    @GeneratedValue
    private UUID certId;

    @ManyToOne
    @JoinColumn(name="emp_id",nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private String name;

    private String issuingOrganization;
    private String credentialId;
    private LocalDate issued;
    private LocalDate expiry;

    @Enumerated(EnumType.STRING)
    private	Status	status;
    public	enum	Status	{	VALID,	EXPIRED,	PENDING_RENEWAL	}

}
