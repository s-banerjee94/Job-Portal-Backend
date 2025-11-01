package com.example.jpb.controller;

import com.example.jpb.model.dto.CandidateResponse;
import com.example.jpb.service.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/candidate")
public class CandidateController {
    private final CandidateService candidateService;

    @GetMapping
    public ResponseEntity<CandidateResponse> getCandidateProfile() {
        CandidateResponse candidateResponse = candidateService.getCandidateProfile();
        return ResponseEntity.ok(candidateResponse);
    }

}
