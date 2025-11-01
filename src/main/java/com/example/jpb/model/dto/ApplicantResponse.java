package com.example.jpb.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicantResponse {

    private Long applicationId;
    private Long candidateId;
    private String candidateName;
    private String candidateEmail;
    private String skills;
    private Integer experience;
    private String location;
    private LocalDateTime appliedAt;
}
