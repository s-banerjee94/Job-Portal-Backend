package com.example.jpb.service;

import com.example.jpb.model.dto.AuthResponse;
import com.example.jpb.model.dto.CandidateRegisterRequest;
import com.example.jpb.model.dto.LoginRequest;
import com.example.jpb.model.dto.RecruiterRegisterRequest;

public interface AuthService {
    void registerCandidate(CandidateRegisterRequest request);

    void registerRecruiter(RecruiterRegisterRequest request);

    AuthResponse login(LoginRequest request);
}
