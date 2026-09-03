package com.skillsphere.career_service.repository;

import com.skillsphere.career_service.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JobRepository extends JpaRepository<Job, UUID> {

    List<Job> findByActiveTrue();
    List<Job> findByDepartmentIgnoreCase(String department);

}
