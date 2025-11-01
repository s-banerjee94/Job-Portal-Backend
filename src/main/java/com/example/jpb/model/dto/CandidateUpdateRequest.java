package com.example.jpb.model.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateUpdateRequest {

    private String name;

    private String skills;

    @Min(value = 0, message = "Experience must be non-negative")
    private Integer experience;

    private String location;
}
