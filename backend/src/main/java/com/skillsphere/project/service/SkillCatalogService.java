package com.skillsphere.project.service;

import com.skillsphere.project.dto.SkillDto;
import com.skillsphere.project.entity.Skill;
import com.skillsphere.project.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public	class	SkillCatalogService	{


	private	final SkillRepository skillRepository;

	public List<SkillDto> getAllSkills(){
		return skillRepository.findAll()
				.stream()
				.map(this::toDto)
				.collect(Collectors.toList());
	}

	public	SkillDto addSkill(SkillDto	dto){

		Skill skill	=	Skill.builder()
				.name(dto.getName())
				.category(Skill.Category.valueOf(dto.getCategory()))
				.build();

		return	toDto(skillRepository.save(skill));
	}


	private	SkillDto toDto(Skill	skill)	{

		return	SkillDto.builder()
				.skillId(skill.getSkillId())
				.name(skill.getName())
				.category(skill.getCategory().name())
				.build();
	}

}