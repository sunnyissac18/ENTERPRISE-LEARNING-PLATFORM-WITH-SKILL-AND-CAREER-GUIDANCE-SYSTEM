package com.skillsphere.learning_service.repository;


import com.skillsphere.learning_service.entity.LearningPath;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LearningPathRepository extends JpaRepository<LearningPath, UUID> {


}
