package com.example.jpb.service;

import com.example.jpb.model.dto.JobApplicationResponse;
import com.example.jpb.model.dto.JobRequest;
import com.example.jpb.model.dto.JobResponse;
import com.example.jpb.model.dto.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface JobService {
    JobResponse createJob(JobRequest jobRequest);

    @Transactional(readOnly = true)
    JobResponse getJobById(Long jobId);

    @Transactional(readOnly = true)
    PageResponse<JobResponse> getAllJobs(Pageable pageable);

    @Transactional(readOnly = true)
    List<JobResponse> getMyJobs();

    @Transactional(readOnly = true)
    List<JobResponse> searchJobs(String skill, String location);

    JobApplicationResponse applyForJob(Long jobId);
}
