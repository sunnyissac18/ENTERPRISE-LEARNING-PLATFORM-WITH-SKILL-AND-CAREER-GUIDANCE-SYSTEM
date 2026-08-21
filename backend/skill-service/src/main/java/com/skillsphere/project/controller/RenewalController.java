package com.skillsphere.project.controller;

import com.skillsphere.project.dto.RenewalDTO;
import com.skillsphere.project.service.RenewalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/certifications/renewals")
@RequiredArgsConstructor
public class RenewalController {

    private final RenewalService renewalService;

    @PostMapping("/{certificationId}")
    public RenewalDTO request(@PathVariable UUID certificationId,
                              @RequestParam String requestedBy) {

        return renewalService.requestRenewal(certificationId, requestedBy);
    }

    @PutMapping("/{renewalId}/approve")
    public RenewalDTO approve(@PathVariable UUID renewalId,
                              @RequestParam LocalDate newExpiry,
                              @RequestParam String approvedBy) {

        return renewalService.approveRenewal(renewalId, newExpiry, approvedBy);
    }
}