package com.skillsphere.project.dto;

import java.util.UUID;


import	lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public	class AssessmentDto {
    private	UUID	assessId;
    private	UUID	empId;
    private	UUID	skillId;
    private	Float	score;
    private	Boolean	passed;
    private	Boolean	verified;
}
