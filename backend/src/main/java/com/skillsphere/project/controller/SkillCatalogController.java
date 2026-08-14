package com.skillsphere.project.controller;

import com.skillsphere.project.dto.SkillDto;
import com.skillsphere.project.service.SkillCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/skills/catalog")
@RequiredArgsConstructor
public	class	SkillCatalogController	{

	private	final SkillCatalogService skillCatalogService;

	@GetMapping
	public List<SkillDto> getCatalog(){

		return	skillCatalogService.getAllSkills();
	}

	@PostMapping
	@PreAuthorize("hasRole('HR')")
	public SkillDto addSkill(@RequestBody SkillDto	dto){

		return	skillCatalogService.addSkill(dto);
	}
}
