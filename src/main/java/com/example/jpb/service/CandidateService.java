package com.example.jpb.service;

import com.example.jpb.model.dto.CandidateResponse;
import com.example.jpb.model.dto.CandidateUpdateRequest;

public interface CandidateService {
    CandidateResponse getCandidateProfile();

    CandidateResponse updateCandidateProfile(CandidateUpdateRequest request);
}
