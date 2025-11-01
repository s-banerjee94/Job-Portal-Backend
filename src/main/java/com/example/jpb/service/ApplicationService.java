package com.example.jpb.service;

import com.example.jpb.exception.ForbiddenAccessException;
import com.example.jpb.exception.JobNotFoundException;
import com.example.jpb.exception.UserNotFoundException;
import com.example.jpb.model.dto.ApplicantResponse;
import com.example.jpb.model.entity.Candidate;
import com.example.jpb.model.entity.Job;
import com.example.jpb.model.entity.JobApplication;
import com.example.jpb.model.entity.Recruiter;
import com.example.jpb.repository.JobApplicationRepository;
import com.example.jpb.repository.JobRepository;
import com.example.jpb.repository.RecruiterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobRepository jobRepository;
    private final RecruiterRepository recruiterRepository;

    @Transactional(readOnly = true)
    public List<ApplicantResponse> getApplicantsByJobId(Long jobId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String recruiterEmail = authentication.getName();

        Recruiter recruiter = recruiterRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new UserNotFoundException(recruiterEmail));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException(jobId));

        if (!job.getPostedBy().getId().equals(recruiter.getId())) {
            throw new ForbiddenAccessException("You do not have permission to view applicants for this job");
        }

        List<JobApplication> applications = jobApplicationRepository.findByJobId(jobId);

        return applications.stream()
                .map(application -> {
                    Candidate candidate = application.getCandidate();
                    return ApplicantResponse.builder()
                            .applicationId(application.getId())
                            .candidateId(candidate.getId())
                            .candidateName(candidate.getName())
                            .candidateEmail(candidate.getEmail())
                            .skills(candidate.getSkills())
                            .experience(candidate.getExperience())
                            .location(candidate.getLocation())
                            .appliedAt(application.getAppliedAt())
                            .build();
                })
                .collect(Collectors.toList());
    }
}
