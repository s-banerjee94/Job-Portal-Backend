package com.example.jpb.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobResponse {
    private Long id;
    private String title;
    private String description;
    private String requiredSkills;
    private Integer experienceRequired;
    private String location;
    private RecruiterInfo recruiter;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RecruiterInfo {
        private Long id;
        private String name;
        private String email;
        private String company;
    }
}
