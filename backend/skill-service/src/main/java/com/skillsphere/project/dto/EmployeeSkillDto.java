package com.skillsphere.project.dto;

import com.skillsphere.project.entity.Employee;
import com.skillsphere.project.entity.Skill;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeSkillDto {

    private UUID id;
    private UUID empId;
    private UUID skillId;
    private Integer yearOfExperience;

}
