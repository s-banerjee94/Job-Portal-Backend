package com.example.jpb.controller;

import com.example.jpb.exception.EmailAlreadyExistsException;
import com.example.jpb.model.dto.CandidateRegisterRequest;
import com.example.jpb.model.dto.ErrorResponse;
import com.example.jpb.model.dto.RecruiterRegisterRequest;
import com.example.jpb.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register/candidate")
    public ResponseEntity<Void> registerCandidate(@Valid @RequestBody CandidateRegisterRequest request) {
        authService.registerCandidate(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/register/recruiter")
    public ResponseEntity<Void> registerRecruiter(@Valid @RequestBody RecruiterRegisterRequest request) {
        authService.registerRecruiter(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(
            EmailAlreadyExistsException ex,
            HttpServletRequest request) {
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Conflict")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}
