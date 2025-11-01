package com.example.jpb.service;

import com.example.jpb.model.dto.JobApplicationResponse;
import com.example.jpb.model.dto.JobRequest;
import com.example.jpb.model.dto.JobResponse;
import com.example.jpb.model.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JobService {
    JobResponse createJob(JobRequest jobRequest);

    JobResponse getJobById(Long jobId);

    PageResponse<JobResponse> getAllJobs(Pageable pageable);

    List<JobResponse> getMyJobs();

    List<JobResponse> searchJobs(String skill, String location);

    JobApplicationResponse applyForJob(Long jobId);
}
