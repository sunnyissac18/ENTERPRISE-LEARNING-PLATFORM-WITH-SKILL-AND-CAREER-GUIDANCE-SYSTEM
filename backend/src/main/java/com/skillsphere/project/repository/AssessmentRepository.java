package com.skillsphere.project.repository;

import com.skillsphere.project.entity.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment,UUID> {
    public List<Assessment> findByEmployeeEmpId(UUID empId);
}
