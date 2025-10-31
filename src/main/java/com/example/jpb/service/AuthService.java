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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public void registerCandidate(CandidateRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        Candidate candidate = new Candidate();
        candidate.setEmail(request.getEmail());
        candidate.setName(request.getName());
        candidate.setPassword(request.getPassword());

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
        recruiter.setPassword(request.getPassword());
        recruiter.setLocation(request.getLocation());

        recruiterRepository.save(recruiter);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException(request.getEmail()));

        if (!request.getPassword().equals(user.getPassword())) {
            throw new InvalidPasswordException();
        }

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
