package com.skillsphere.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "competency_frameworks")
@Builder
public	class	CompetencyFramework	{

	@Id
	@GeneratedValue
	private UUID id;
	private	String	roleTitle;
	@ManyToOne
	@JoinColumn(name ="skill_id")
	private	Skill skill;
	private	Integer	requiredProficiency;
}