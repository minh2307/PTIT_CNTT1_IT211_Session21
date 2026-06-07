package com.example.ss21.doctor.service;

import com.example.ss21.doctor.dto.request.DoctorCreateRequest;
import com.example.ss21.doctor.dto.request.DoctorUpdateRequest;
import com.example.ss21.doctor.dto.response.DoctorResponse;
import com.example.ss21.doctor.entity.Doctor;
import com.example.ss21.doctor.exception.DuplicateResourceException;
import com.example.ss21.doctor.exception.ResourceNotFoundException;
import com.example.ss21.doctor.repository.DoctorRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    @Override
    public DoctorResponse createDoctor(DoctorCreateRequest request) {
        validateUniqueness(request.email(), request.phoneNumber(), request.licenseNumber(), null);

        Doctor savedDoctor = doctorRepository.save(Doctor.builder()
                .fullName(request.fullName())
                .specialty(request.specialty())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .licenseNumber(request.licenseNumber())
                .experienceYears(request.experienceYears())
                .active(request.active() == null ? Boolean.TRUE : request.active())
                .build());

        return toResponse(savedDoctor);
    }

    @Override
    public DoctorResponse updateDoctor(Long id, DoctorUpdateRequest request) {
        Doctor doctor = getDoctorEntityById(id);
        validateUniqueness(request.email(), request.phoneNumber(), request.licenseNumber(), id);

        doctor.setFullName(request.fullName());
        doctor.setSpecialty(request.specialty());
        doctor.setEmail(request.email());
        doctor.setPhoneNumber(request.phoneNumber());
        doctor.setLicenseNumber(request.licenseNumber());
        doctor.setExperienceYears(request.experienceYears());
        doctor.setActive(request.active() == null ? doctor.getActive() : request.active());

        return toResponse(doctorRepository.save(doctor));
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorResponse getDoctorById(Long id) {
        return toResponse(getDoctorEntityById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorResponse> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void deleteDoctor(Long id) {
        Doctor doctor = getDoctorEntityById(id);
        doctorRepository.delete(doctor);
    }

    private Doctor getDoctorEntityById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
    }

    private void validateUniqueness(String email, String phoneNumber, String licenseNumber, Long currentDoctorId) {
        boolean emailExists = currentDoctorId == null
                ? doctorRepository.existsByEmail(email)
                : doctorRepository.existsByEmailAndIdNot(email, currentDoctorId);
        if (emailExists) {
            throw new DuplicateResourceException("Doctor email already exists: " + email);
        }

        boolean phoneExists = currentDoctorId == null
                ? doctorRepository.existsByPhoneNumber(phoneNumber)
                : doctorRepository.existsByPhoneNumberAndIdNot(phoneNumber, currentDoctorId);
        if (phoneExists) {
            throw new DuplicateResourceException("Doctor phone number already exists: " + phoneNumber);
        }

        boolean licenseExists = currentDoctorId == null
                ? doctorRepository.existsByLicenseNumber(licenseNumber)
                : doctorRepository.existsByLicenseNumberAndIdNot(licenseNumber, currentDoctorId);
        if (licenseExists) {
            throw new DuplicateResourceException("Doctor license number already exists: " + licenseNumber);
        }
    }

    private DoctorResponse toResponse(Doctor doctor) {
        return new DoctorResponse(
                doctor.getId(),
                doctor.getFullName(),
                doctor.getSpecialty(),
                doctor.getEmail(),
                doctor.getPhoneNumber(),
                doctor.getLicenseNumber(),
                doctor.getExperienceYears(),
                doctor.getActive()
        );
    }
}
