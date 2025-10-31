package com.example.jpb.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Required skills are required")
    private String requiredSkills;

    @NotNull(message = "Experience required is required")
    @Min(value = 0, message = "Experience required must be non-negative")
    private Integer experienceRequired;

    @NotBlank(message = "Location is required")
    private String location;
}
