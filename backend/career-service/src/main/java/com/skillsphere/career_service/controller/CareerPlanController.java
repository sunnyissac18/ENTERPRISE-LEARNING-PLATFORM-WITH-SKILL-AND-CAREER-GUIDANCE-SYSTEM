package com.skillsphere.career_service.controller;

import com.skillsphere.career_service.dto.CareerPlanDto;
import com.skillsphere.career_service.service.CareerPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/career/plans")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CareerPlanController {

    private final CareerPlanService service;

    @PostMapping
    public CareerPlanDto create (@RequestBody CareerPlanDto dto){
        return service.create(dto);

    }

    @GetMapping
    public List<CareerPlanDto> getAll(){
        return service.getAll();
    }

    @GetMapping("/id")
    public CareerPlanDto getById(@PathVariable() UUID id){
        return service.getById(id);
    }

    @GetMapping("/employee/{empId}")
    public List<CareerPlanDto> getByEmployee(@PathVariable UUID empId){

        return service.getByEmployee(empId);
    }

    @PutMapping("/{id}")
    public CareerPlanDto update(
            @PathVariable UUID id,
            @RequestBody CareerPlanDto dto){

        return service.update(id, dto);

    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id){
        service.delete(id);
    }
}
