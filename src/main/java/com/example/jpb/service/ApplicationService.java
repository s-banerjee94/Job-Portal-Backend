package com.example.jpb.service;

import com.example.jpb.model.dto.ApplicantResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ApplicationService {
    @Transactional(readOnly = true)
    List<ApplicantResponse> getApplicantsByJobId(Long jobId);
}
