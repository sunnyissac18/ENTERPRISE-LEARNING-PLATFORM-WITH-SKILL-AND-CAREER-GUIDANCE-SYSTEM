package com.skillsphere.project.repository;

import com.skillsphere.project.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {

    List<Enrollment> findByEmpId(UUID empId);
    List<Enrollment> findByCourseCourseId(UUID courseId);

}
