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
@Table(name = "employees")
@Builder
public class Employee {

    @Id
    @GeneratedValue
    private UUID empId;

    @Column(name = "full_name" , nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "department")
    private String dept;

    public	enum	Role	{	DEVELOPER,	MANAGER,	TECH_LEAD,	HR,	ADMIN,
        TRAINING_MANAGER	}
}


