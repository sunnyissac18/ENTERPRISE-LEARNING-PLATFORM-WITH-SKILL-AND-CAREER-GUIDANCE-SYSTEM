package com.skillsphere.project.repository;

import com.skillsphere.project.entity.EmployeeSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmployeeSkillRepository extends JpaRepository<EmployeeSkill,UUID> {

    public List<EmployeeSkill> findByEmployeeEmpId(UUID empId);
}
