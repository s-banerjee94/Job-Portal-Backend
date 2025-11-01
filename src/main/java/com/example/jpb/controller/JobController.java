package com.example.jpb.controller;

import com.example.jpb.model.dto.JobRequest;
import com.example.jpb.model.dto.JobResponse;
import com.example.jpb.model.dto.PageResponse;
import com.example.jpb.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping
    public ResponseEntity<JobResponse> createJob(@Valid @RequestBody JobRequest jobRequest) {
        JobResponse jobResponse = jobService.createJob(jobRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(jobResponse);
    }

    @GetMapping
    public ResponseEntity<PageResponse<JobResponse>> getAllJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        Sort.Direction direction = sortDirection.equalsIgnoreCase("ASC")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        PageResponse<JobResponse> response = jobService.getAllJobs(pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJobById(@PathVariable Long id) {
        JobResponse jobResponse = jobService.getJobById(id);
        return ResponseEntity.ok(jobResponse);
    }

    @GetMapping("/mine")
    public ResponseEntity<List<JobResponse>> getMyJobs() {
        List<JobResponse> jobs = jobService.getMyJobs();
        return ResponseEntity.ok(jobs);
    }
}
