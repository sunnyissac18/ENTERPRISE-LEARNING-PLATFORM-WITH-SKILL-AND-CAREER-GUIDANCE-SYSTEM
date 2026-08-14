package com.skillsphere.project.repository;

import com.skillsphere.project.entity.Certification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CertificationRepository extends JpaRepository<Certification,UUID> {
    public List<Certification> findByEmployeeEmpId(UUID empId);
}
