package com.skillsphere.career_service.controller;

import com.skillsphere.career_service.dto.AnalyticsDto;
import com.skillsphere.career_service.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/career/analytics")
public class AnalyticsController {

    private final AnalyticsService service;

    @GetMapping
    public AnalyticsDto getAnalytics() {
        return service.getAnalytics();
    }

}