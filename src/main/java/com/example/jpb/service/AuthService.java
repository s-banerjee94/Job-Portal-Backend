package com.example.jpb.service;

import com.example.jpb.exception.EmailAlreadyExistsException;
import com.example.jpb.exception.InvalidPasswordException;
import com.example.jpb.exception.UserNotFoundException;
import com.example.jpb.model.dto.AuthResponse;
import com.example.jpb.model.dto.CandidateRegisterRequest;
import com.example.jpb.model.dto.LoginRequest;
import com.example.jpb.model.dto.RecruiterRegisterRequest;
import com.example.jpb.model.entity.Candidate;
import com.example.jpb.model.entity.Recruiter;
import com.example.jpb.model.entity.User;
import com.example.jpb.repository.CandidateRepository;
import com.example.jpb.repository.RecruiterRepository;
import com.example.jpb.repository.UserRepository;
import com.example.jpb.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CandidateRepository candidateRepository;
    private final RecruiterRepository recruiterRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    public void registerCandidate(CandidateRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        Candidate candidate = new Candidate();
        candidate.setEmail(request.getEmail());
        candidate.setName(request.getName());
        candidate.setPassword(passwordEncoder.encode(request.getPassword()));

        candidateRepository.save(candidate);
    }

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

        recruiterRepository.save(recruiter);
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException(request.getEmail()));


        String role;
        if (user instanceof Candidate) {
            role = "ROLE_CANDIDATE";
        } else if (user instanceof Recruiter) {
            role = "ROLE_RECRUITER";
        } else {
            throw new UserNotFoundException("Unknown user type");
        }

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
