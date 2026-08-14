package com.skillsphere.project.dto;


import com.skillsphere.project.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {

    private UUID empId;
    private String fullName;
    private Employee.Role role;
    private String dept;
}
