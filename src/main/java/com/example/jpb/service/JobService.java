package com.example.jpb.service;

import com.example.jpb.exception.JobNotFoundException;
import com.example.jpb.exception.UserNotFoundException;
import com.example.jpb.model.dto.JobRequest;
import com.example.jpb.model.dto.JobResponse;
import com.example.jpb.model.dto.PageResponse;
import com.example.jpb.model.entity.Job;
import com.example.jpb.model.entity.Recruiter;
import com.example.jpb.repository.JobRepository;
import com.example.jpb.repository.RecruiterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;

    private final RecruiterRepository recruiterRepository;

    public JobResponse createJob(JobRequest jobRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String recruiterEmail = authentication.getName();

        Recruiter recruiter = recruiterRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new UserNotFoundException(recruiterEmail));

        Job job = new Job();
        job.setTitle(jobRequest.getTitle());
        job.setDescription(jobRequest.getDescription());
        job.setRequiredSkills(jobRequest.getRequiredSkills());
        job.setExperienceRequired(jobRequest.getExperienceRequired());
        job.setLocation(jobRequest.getLocation());
        job.setPostedBy(recruiter);

        Job savedJob = jobRepository.save(job);


        return JobResponse.builder()
                .id(savedJob.getId())
                .title(savedJob.getTitle())
                .description(savedJob.getDescription())
                .requiredSkills(savedJob.getRequiredSkills())
                .experienceRequired(savedJob.getExperienceRequired())
                .location(savedJob.getLocation())
                .build();
    }

    @Transactional(readOnly = true)
    public JobResponse getJobById(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException(jobId));

        Recruiter recruiter = job.getPostedBy();

        JobResponse.RecruiterInfo recruiterInfo = JobResponse.RecruiterInfo.builder()
                .id(recruiter.getId())
                .name(recruiter.getName())
                .email(recruiter.getEmail())
                .company(recruiter.getCompany())
                .build();

        return JobResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .requiredSkills(job.getRequiredSkills())
                .experienceRequired(job.getExperienceRequired())
                .location(job.getLocation())
                .recruiter(recruiterInfo)
                .build();
    }

    @Transactional(readOnly = true)
    public PageResponse<JobResponse> getAllJobs(Pageable pageable) {
        Page<Job> jobPage = jobRepository.findAll(pageable);

        List<JobResponse> jobResponses = jobPage.getContent().stream()
                .map(job -> {
//                    Recruiter recruiter = job.getPostedBy();
//
//                    JobResponse.RecruiterInfo recruiterInfo = JobResponse.RecruiterInfo.builder()
//                            .id(recruiter.getId())
//                            .name(recruiter.getName())
//                            .email(recruiter.getEmail())
//                            .company(recruiter.getCompany())
//                            .build();

                    return JobResponse.builder()
                            .id(job.getId())
                            .title(job.getTitle())
                            .description(job.getDescription())
                            .requiredSkills(job.getRequiredSkills())
                            .experienceRequired(job.getExperienceRequired())
                            .location(job.getLocation())
//                            .recruiter(recruiterInfo)
                            .build();
                })
                .collect(Collectors.toList());

        return PageResponse.<JobResponse>builder()
                .content(jobResponses)
                .pageNumber(jobPage.getNumber())
                .pageSize(jobPage.getSize())
                .totalElements(jobPage.getTotalElements())
                .totalPages(jobPage.getTotalPages())
                .last(jobPage.isLast())
                .first(jobPage.isFirst())
                .build();
    }
}
