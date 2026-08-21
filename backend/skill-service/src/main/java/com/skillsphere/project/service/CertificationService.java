package com.skillsphere.project.service;

import com.skillsphere.project.dto.CertificationDto;
import com.skillsphere.project.entity.Certification;
import com.skillsphere.project.entity.Employee;
import com.skillsphere.project.repository.CertificationRepository;
import com.skillsphere.project.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CertificationService {

    private final EmployeeRepository employeeRepository;
    private final CertificationRepository certificationRepository;

    public CertificationDto register(CertificationDto dto) {

        Employee employee = employeeRepository.findById(dto.getEmpId())
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));

        Certification certification = Certification.builder()
                .employee(employee)
                .name(dto.getName())
                .issuingOrganization(dto.getIssuingOrganization())
                .credentialId(dto.getCredentialId())
                .issued(dto.getIssued())
                .expiry(dto.getExpiry())
                .status(calculateStatus(dto.getExpiry()))
                .build();

        Certification saved =certificationRepository.save(certification);
        return toDTO(saved);
    }

    public CertificationDto getById(UUID id) {
        Certification certification =certificationRepository.findById(id)
                .orElseThrow(() ->new RuntimeException("Certification not found"));

        return toDTO(certification);
    }

    public List<CertificationDto> getByEmployee(UUID empId) {
        return certificationRepository
                .findByEmployeeEmpId(empId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public CertificationDto update(UUID id,CertificationDto dto) {

        Certification certification =certificationRepository.findById(id)
                .orElseThrow(() ->new RuntimeException("Certification not found"));

        certification.setName(dto.getName());
        certification.setIssuingOrganization(dto.getIssuingOrganization());
        certification.setCredentialId(dto.getCredentialId());
        certification.setIssued(dto.getIssued());
        certification.setExpiry(dto.getExpiry());
        certification.setStatus(calculateStatus(dto.getExpiry()));
        return toDTO(certificationRepository.save(certification));
    }

    public void delete(UUID id) {
        if (!certificationRepository.existsById(id)) {
            throw new RuntimeException(
                    "Certification not found");
        }
        certificationRepository.deleteById(id);
    }

    public List<CertificationDto> getExpiring() {
        LocalDate today = LocalDate.now();
        LocalDate end = today.plusDays(30);
        return certificationRepository
                .findByExpiryBetween(today, end)
                .stream()
                .map(cert -> {
                    cert.setStatus(Certification.Status.PENDING_RENEWAL);
                    certificationRepository.save(cert);
                    return toDTO(cert);
                })
                .toList();
    }

    public List<CertificationDto> getExpired() {
        return certificationRepository
                .findByStatus(Certification.Status.EXPIRED)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public CertificationDto refreshStatus(UUID id) {

        Certification certification = certificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Certification not found"));

        certification.setStatus(calculateStatus(certification.getExpiry()));
        Certification saved = certificationRepository.save(certification);
        return toDTO(saved);
    }

    private Certification.Status calculateStatus(LocalDate expiry) {

        LocalDate today = LocalDate.now();

        if (expiry == null) {
            return Certification.Status.EXPIRED;
        }
        if (expiry.isBefore(today)) {
            return Certification.Status.EXPIRED;
        }
        if (!expiry.isAfter(today.plusDays(30))) {
            return Certification.Status.PENDING_RENEWAL;
        }
        return Certification.Status.VALID;
    }

    private CertificationDto toDTO(
            Certification certification) {
        return CertificationDto.builder()
                .certId(certification.getCertId())
                .empId(certification.getEmployee().getEmpId())
                .employeeName(certification.getName())
                .name(certification.getName())
                .issuingOrganization(certification.getIssuingOrganization())
                .credentialId(certification.getCredentialId())
                .issued(certification.getIssued())
                .expiry(certification.getExpiry())
                .status(certification.getStatus().name())
                .build();
    }

}
