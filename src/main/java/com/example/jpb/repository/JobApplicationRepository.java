package com.example.jpb.repository;

import com.example.jpb.model.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    Optional<JobApplication> findByJobIdAndCandidateId(Long jobId, Long candidateId);

    List<JobApplication> findByJobId(Long jobId);
}
