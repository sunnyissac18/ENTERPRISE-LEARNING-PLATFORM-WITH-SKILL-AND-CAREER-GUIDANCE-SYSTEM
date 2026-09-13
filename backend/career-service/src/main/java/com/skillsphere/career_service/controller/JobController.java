package com.skillsphere.career_service.controller;

import com.skillsphere.career_service.dto.JobDto;
import com.skillsphere.career_service.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/career/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('TRAINING_MANAGER','ADMIN')")
    public JobDto create(@RequestBody JobDto dto){
        return service.create(dto);
    }

    @GetMapping
    public List<JobDto> getAll(){
        return service.getAll();
    }

    @GetMapping("/active")
    public List<JobDto> getActive(){
        return service.getActiveJobs();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('TRAINING_MANAGER','ADMIN')")
    public void delete(@PathVariable UUID id){
        service.delete(id);
    }
}
