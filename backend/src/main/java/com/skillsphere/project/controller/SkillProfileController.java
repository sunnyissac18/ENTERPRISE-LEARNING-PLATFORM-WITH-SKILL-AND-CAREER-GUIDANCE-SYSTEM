package com.skillsphere.project.controller;

import com.skillsphere.project.service.SkillProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/skill-profile")
@RequiredArgsConstructor
public class SkillProfileController {

    private final SkillProfileService service;

    @GetMapping("{empId}")
    public Map<String,Object> getSkillsProfile(
            @PathVariable UUID empId){
        return service.getSkillProfile(empId);
    }
}
