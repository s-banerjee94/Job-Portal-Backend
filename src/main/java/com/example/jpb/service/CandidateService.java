package com.example.jpb.service;


import com.example.jpb.exception.UserNotFoundException;
import com.example.jpb.model.dto.CandidateResponse;
import com.example.jpb.model.dto.CandidateUpdateRequest;
import com.example.jpb.model.entity.Candidate;
import com.example.jpb.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CandidateService {
    private final CandidateRepository candidateRepository;

    public CandidateResponse getCandidateProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Candidate candidate = candidateRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        return CandidateResponse.builder()
                .id(candidate.getId())
                .email(candidate.getEmail())
                .name(candidate.getName())
                .skills(candidate.getSkills())
                .experience(candidate.getExperience())
                .location(candidate.getLocation())
                .build();
    }

    public CandidateResponse updateCandidateProfile(CandidateUpdateRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Candidate candidate = candidateRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        if (request.getName() != null) {
            candidate.setName(request.getName());
        }
        if (request.getSkills() != null) {
            candidate.setSkills(request.getSkills());
        }
        if (request.getExperience() != null) {
            candidate.setExperience(request.getExperience());
        }
        if (request.getLocation() != null) {
            candidate.setLocation(request.getLocation());
        }

        Candidate updatedCandidate = candidateRepository.save(candidate);

        return CandidateResponse.builder()
                .id(updatedCandidate.getId())
                .email(updatedCandidate.getEmail())
                .name(updatedCandidate.getName())
                .skills(updatedCandidate.getSkills())
                .experience(updatedCandidate.getExperience())
                .location(updatedCandidate.getLocation())
                .build();
    }
}
