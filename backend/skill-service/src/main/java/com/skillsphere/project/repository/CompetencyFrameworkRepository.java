package com.skillsphere.project.repository;

import com.skillsphere.project.entity.CompetencyFramework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CompetencyFrameworkRepository extends JpaRepository<CompetencyFramework, UUID> {
    List<CompetencyFramework> findByRoleTitle(String targetRoleTitle);

}
