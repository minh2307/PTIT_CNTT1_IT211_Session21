package com.example.ss21.doctor.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DoctorCreateRequest(
        @NotBlank(message = "Full name is required")
        @Size(max = 100, message = "Full name must not exceed 100 characters")
        String fullName,

        @NotBlank(message = "Specialty is required")
        @Size(max = 100, message = "Specialty must not exceed 100 characters")
        String specialty,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 120, message = "Email must not exceed 120 characters")
        String email,

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^[0-9+()\\-\\s]{8,20}$", message = "Phone number is invalid")
        String phoneNumber,

        @NotBlank(message = "License number is required")
        @Size(max = 50, message = "License number must not exceed 50 characters")
        String licenseNumber,

        @Min(value = 0, message = "Experience years must be greater than or equal to 0")
        @Max(value = 60, message = "Experience years must be less than or equal to 60")
        Integer experienceYears,

        Boolean active
) {
}
