package com.skillsphere.project.controller;

import com.skillsphere.project.dto.CompetencyGapDto;
import com.skillsphere.project.service.CompetencyMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/competency")
@RequiredArgsConstructor
public class CompetencyMappingController {

    private final CompetencyMappingService competencyMappingService;

    @GetMapping("/gaps")
    public List<CompetencyGapDto> getGaps(
            @RequestParam UUID empId,
            @RequestParam String targetRole) {

        return competencyMappingService.getGapsForRole(
                empId,
                targetRole
        );
    }
}