package com.skillsphere.project.controller;

import com.skillsphere.project.service.SkillProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/skills/profile")
@RequiredArgsConstructor
public class SkillProfileController {

    private final SkillProfileService service;

    @GetMapping("{empId}")
    public Map<String,Object> getSkillsProfile(
            @PathVariable UUID empId){
        return service.getSkillProfile(empId);
    }

    @PostMapping("{empId}/skills/{skillId}")
    public org.springframework.http.ResponseEntity<?> addSkillToProfile(
            @PathVariable UUID empId,
            @PathVariable UUID skillId) {
        return org.springframework.http.ResponseEntity.ok(service.addSkillToProfile(empId, skillId));
    }

    @PutMapping("{empId}/skills/{skillId}")
    public org.springframework.http.ResponseEntity<?> updateSkillProficiency(
            @PathVariable UUID empId,
            @PathVariable UUID skillId,
            @org.springframework.web.bind.annotation.RequestParam int proficiency) {
        return org.springframework.http.ResponseEntity.ok(service.updateSkillProficiency(empId, skillId, proficiency));
    }
}


