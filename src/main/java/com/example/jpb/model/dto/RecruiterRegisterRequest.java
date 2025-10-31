package com.example.jpb.model.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecruiterRegisterRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 4, message = "Name must be at least 4 characters")
    private String name;

    @NotBlank(message = "Company is required")
    private String company;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Location is required")
    private String location;
}
