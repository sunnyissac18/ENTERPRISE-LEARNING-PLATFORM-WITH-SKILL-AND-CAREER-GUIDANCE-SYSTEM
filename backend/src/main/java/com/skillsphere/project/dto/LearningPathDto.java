package com.skillsphere.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LearningPathDto {

    private UUID pathId;
    private String pathName;
    private String desc;
    private Integer progress;
    private Boolean active;
}
