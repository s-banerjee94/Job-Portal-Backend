package com.example.jpb.service.impl;

import com.example.jpb.exception.DuplicateJobApplicationException;
import com.example.jpb.exception.InsufficientExperienceException;
import com.example.jpb.exception.JobNotFoundException;
import com.example.jpb.exception.UserNotFoundException;
import com.example.jpb.model.dto.JobApplicationResponse;
import com.example.jpb.model.dto.JobRequest;
import com.example.jpb.model.dto.JobResponse;
import com.example.jpb.model.dto.PageResponse;
import com.example.jpb.model.entity.Candidate;
import com.example.jpb.model.entity.Job;
import com.example.jpb.model.entity.JobApplication;
import com.example.jpb.model.entity.Recruiter;
import com.example.jpb.repository.CandidateRepository;
import com.example.jpb.repository.JobApplicationRepository;
import com.example.jpb.repository.JobRepository;
import com.example.jpb.repository.RecruiterRepository;
import com.example.jpb.service.JobService;
import lombok.RequiredArgsConstructor;
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
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final RecruiterRepository recruiterRepository;
    private final CandidateRepository candidateRepository;
    private final JobApplicationRepository jobApplicationRepository;

    @Override
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
    @Override
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
    @Override
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

    @Transactional(readOnly = true)
    @Override
    public List<JobResponse> getMyJobs() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String recruiterEmail = authentication.getName();

        Recruiter recruiter = recruiterRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new UserNotFoundException(recruiterEmail));

        List<Job> jobs = jobRepository.findByPostedBy(recruiter);

        return jobs.stream()
                .map(job -> JobResponse.builder()
                        .id(job.getId())
                        .title(job.getTitle())
                        .description(job.getDescription())
                        .requiredSkills(job.getRequiredSkills())
                        .experienceRequired(job.getExperienceRequired())
                        .location(job.getLocation())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<JobResponse> searchJobs(String skill, String location) {
        List<Job> jobs = jobRepository.findByRequiredSkillsContainingIgnoreCaseOrLocationContainingIgnoreCase(skill, location);

        return jobs.stream()
                .map(job -> JobResponse.builder()
                        .id(job.getId())
                        .title(job.getTitle())
                        .description(job.getDescription())
                        .requiredSkills(job.getRequiredSkills())
                        .experienceRequired(job.getExperienceRequired())
                        .location(job.getLocation())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public JobApplicationResponse applyForJob(Long jobId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String candidateEmail = authentication.getName();

        Candidate candidate = candidateRepository.findByEmail(candidateEmail)
                .orElseThrow(() -> new UserNotFoundException(candidateEmail));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException(jobId));

        jobApplicationRepository.findByJobIdAndCandidateId(jobId, candidate.getId())
                .ifPresent(existingApplication -> {
                    throw new DuplicateJobApplicationException();
                });
        if (candidate.getExperience() == null || candidate.getExperience() < job.getExperienceRequired()) {
            throw new InsufficientExperienceException(job.getExperienceRequired(),
                    candidate.getExperience() != null ? candidate.getExperience() : 0);
        }

        JobApplication jobApplication = new JobApplication();
        jobApplication.setJob(job);
        jobApplication.setCandidate(candidate);

        JobApplication savedApplication = jobApplicationRepository.save(jobApplication);

        return JobApplicationResponse.builder()
                .id(savedApplication.getId())
                .jobId(job.getId())
                .jobTitle(job.getTitle())
                .candidateId(candidate.getId())
                .candidateName(candidate.getName())
                .appliedAt(savedApplication.getAppliedAt())
                .build();
    }
}
