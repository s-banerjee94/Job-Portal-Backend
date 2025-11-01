package com.example.jpb.service.impl;


import com.example.jpb.exception.UserNotFoundException;
import com.example.jpb.model.dto.CandidateResponse;
import com.example.jpb.model.dto.CandidateUpdateRequest;
import com.example.jpb.model.entity.Candidate;
import com.example.jpb.repository.CandidateRepository;
import com.example.jpb.service.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {
    private final CandidateRepository candidateRepository;

    @Override
    @Cacheable(value = "candidateProfile", key = "T(org.springframework.security.core.context.SecurityContextHolder).getContext().getAuthentication().getName()")
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

    @Override
    @Caching(
            put = @CachePut(value = "candidateProfile", key = "T(org.springframework.security.core.context.SecurityContextHolder).getContext().getAuthentication().getName()"),
            evict = @CacheEvict(value = "user", key = "T(org.springframework.security.core.context.SecurityContextHolder).getContext().getAuthentication().getName()")
    )
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
