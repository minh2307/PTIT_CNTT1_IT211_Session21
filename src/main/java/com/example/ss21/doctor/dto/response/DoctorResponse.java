package com.example.ss21.doctor.dto.response;

public record DoctorResponse(
        Long id,
        String fullName,
        String specialty,
        String email,
        String phoneNumber,
        String licenseNumber,
        Integer experienceYears,
        Boolean active
) {
}
