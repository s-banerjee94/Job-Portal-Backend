package com.example.jpb.repository;

import com.example.jpb.model.entity.Job;
import com.example.jpb.model.entity.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByPostedBy(Recruiter recruiter);

    List<Job> findByRequiredSkillsContainingIgnoreCaseOrLocationContainingIgnoreCase(String skill, String location);
}
