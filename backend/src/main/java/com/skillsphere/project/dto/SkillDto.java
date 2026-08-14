package com.skillsphere.project.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public	class SkillDto {
    private	UUID	skillId;
    private	String	name;
    private	String	category;
}
