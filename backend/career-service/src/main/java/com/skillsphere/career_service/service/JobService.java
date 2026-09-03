package com.skillsphere.career_service.service;

import com.skillsphere.career_service.dto.JobDto;
import com.skillsphere.career_service.entity.Job;
import com.skillsphere.career_service.repository.CareerPlanRepository;
import com.skillsphere.career_service.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository repository;

    private JobDto toDTO(Job j) {

        return JobDto.builder()
                .jobId(j.getJobId())
                .title(j.getTitle())
                .department(j.getDepartment())
                .requiredSkills(j.getRequiredSkills())
                .minimumExperience(j.getMinimumExperience())
                .active(j.getActive())
                .build();
    }

    public JobDto create(JobDto dto) {

        Job job = Job.builder()
                .title(dto.getTitle())
                .department(dto.getDepartment())
                .requiredSkills(dto.getRequiredSkills())
                .minimumExperience(dto.getMinimumExperience())
                .active(true)
                .build();

        return toDTO(repository.save(job));
    }

    public List<JobDto> getAll(){

        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<JobDto> getActiveJobs(){

        return repository.findByActiveTrue()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public void delete(UUID id){
        repository.deleteById(id);
    }
}
