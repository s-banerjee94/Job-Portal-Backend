package com.example.jpb.service.impl;

import com.example.jpb.exception.EmailAlreadyExistsException;
import com.example.jpb.exception.UserNotFoundException;
import com.example.jpb.model.dto.AuthResponse;
import com.example.jpb.model.dto.CandidateRegisterRequest;
import com.example.jpb.model.dto.LoginRequest;
import com.example.jpb.model.dto.RecruiterRegisterRequest;
import com.example.jpb.model.entity.Candidate;
import com.example.jpb.model.entity.Recruiter;
import com.example.jpb.model.entity.Role;
import com.example.jpb.model.entity.User;
import com.example.jpb.repository.CandidateRepository;
import com.example.jpb.repository.RecruiterRepository;
import com.example.jpb.repository.UserRepository;
import com.example.jpb.security.JwtUtil;
import com.example.jpb.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final CandidateRepository candidateRepository;
    private final RecruiterRepository recruiterRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    @Override
    public void registerCandidate(CandidateRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        Candidate candidate = new Candidate();
        candidate.setEmail(request.getEmail());
        candidate.setName(request.getName());
        candidate.setPassword(passwordEncoder.encode(request.getPassword()));
        candidate.setRole(Role.ROLE_CANDIDATE);

        candidateRepository.save(candidate);
    }

    @Override
    public void registerRecruiter(RecruiterRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        Recruiter recruiter = new Recruiter();
        recruiter.setName(request.getName());
        recruiter.setCompany(request.getCompany());
        recruiter.setEmail(request.getEmail());
        recruiter.setPassword(passwordEncoder.encode(request.getPassword()));
        recruiter.setLocation(request.getLocation());
        recruiter.setRole(Role.ROLE_RECRUITER);

        recruiterRepository.save(recruiter);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException(request.getEmail()));

        String role = user.getRole().name();

        String token = jwtUtil.generateToken(user.getEmail(), role, user.getId());

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .email(user.getEmail())
                .name(user.getName())
                .role(role)
                .build();
    }
}
