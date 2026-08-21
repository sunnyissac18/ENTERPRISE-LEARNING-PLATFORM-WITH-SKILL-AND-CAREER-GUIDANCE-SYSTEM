package com.skillsphere.project.controller;

import com.skillsphere.project.dto.CertificationDto;
import com.skillsphere.project.service.CertificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/certifications")
public class CertificationController {

    private final CertificationService certificationService;

    @PostMapping
    public CertificationDto register(@RequestBody CertificationDto dto) {
        return certificationService.register(dto);
    }

    @GetMapping("/{id}")
    public CertificationDto getById(@PathVariable UUID id) {
        return certificationService.getById(id);
    }

    @GetMapping("/employee/{empId}")
    public List<CertificationDto> getByEmployee(@PathVariable UUID empId) {
        return certificationService.getByEmployee(empId);
    }

    @PutMapping("/{id}")
    public CertificationDto update(@PathVariable UUID id,
                                   @RequestBody CertificationDto dto) {
        return certificationService.update(id, dto);
    }

    @PutMapping("/{id}/refresh-status")
    public CertificationDto refreshStatus(@PathVariable UUID id) {

        return certificationService.refreshStatus(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        certificationService.delete(id);
    }

    @GetMapping("/expiring")
    public List<CertificationDto> expiring() {
        return certificationService.getExpiring();
    }

    @GetMapping("/expired")
    public List<CertificationDto> expired() {
        return certificationService.getExpired();
    }
}


