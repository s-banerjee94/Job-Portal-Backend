package com.example.jpb.controller;

import com.example.jpb.model.dto.CandidateResponse;
import com.example.jpb.model.dto.CandidateUpdateRequest;
import com.example.jpb.service.CandidateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RequiredArgsConstructor
@RestController
@RequestMapping("/api/candidates")
public class CandidateController {
    private final CandidateService candidateService;

    @GetMapping("/profile")
    public ResponseEntity<CandidateResponse> getCandidateProfile() {
        CandidateResponse candidateResponse = candidateService.getCandidateProfile();
        return ResponseEntity.ok(candidateResponse);
    }

    @PatchMapping("/profile")
    public ResponseEntity<CandidateResponse> updateCandidateProfile(
            @Valid @RequestBody CandidateUpdateRequest request) {
        CandidateResponse candidateResponse = candidateService.updateCandidateProfile(request);
        return ResponseEntity.ok(candidateResponse);
    }

}
