package com.example.jpb.controller;

import com.example.jpb.model.dto.ApplicantResponse;
import com.example.jpb.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @GetMapping("/{jobId}")
    public ResponseEntity<List<ApplicantResponse>> getApplicantsByJobId(@PathVariable Long jobId) {
        List<ApplicantResponse> applicants = applicationService.getApplicantsByJobId(jobId);
        return ResponseEntity.ok(applicants);
    }
}
