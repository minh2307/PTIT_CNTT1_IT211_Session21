package com.example.ss21.doctor.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.ss21.doctor.dto.request.DoctorCreateRequest;
import com.example.ss21.doctor.dto.request.DoctorUpdateRequest;
import com.example.ss21.doctor.dto.response.DoctorResponse;
import com.example.ss21.doctor.entity.Doctor;
import com.example.ss21.doctor.exception.DuplicateResourceException;
import com.example.ss21.doctor.exception.ResourceNotFoundException;
import com.example.ss21.doctor.repository.DoctorRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DoctorServiceImplTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    private Doctor doctor;

    @BeforeEach
    void setUp() {
        doctor = Doctor.builder()
                .id(1L)
                .fullName("Dr. Alice Smith")
                .specialty("Cardiology")
                .email("alice@example.com")
                .phoneNumber("0901234567")
                .licenseNumber("MED-001")
                .experienceYears(10)
                .active(true)
                .build();
    }

    @Test
    void createDoctor_shouldReturnCreatedDoctor() {
        DoctorCreateRequest request = new DoctorCreateRequest(
                "Dr. Bob Brown",
                "Neurology",
                "bob@example.com",
                "0907654321",
                "MED-002",
                8,
                true
        );

        when(doctorRepository.existsByEmail(request.email())).thenReturn(false);
        when(doctorRepository.existsByPhoneNumber(request.phoneNumber())).thenReturn(false);
        when(doctorRepository.existsByLicenseNumber(request.licenseNumber())).thenReturn(false);
        when(doctorRepository.save(any(Doctor.class))).thenAnswer(invocation -> {
            Doctor saved = invocation.getArgument(0);
            saved.setId(2L);
            return saved;
        });

        DoctorResponse response = doctorService.createDoctor(request);

        assertThat(response.id()).isEqualTo(2L);
        assertThat(response.fullName()).isEqualTo("Dr. Bob Brown");
        verify(doctorRepository).save(any(Doctor.class));
    }

    @Test
    void createDoctor_shouldThrowWhenEmailExists() {
        DoctorCreateRequest request = new DoctorCreateRequest(
                "Dr. Bob Brown",
                "Neurology",
                "bob@example.com",
                "0907654321",
                "MED-002",
                8,
                true
        );

        when(doctorRepository.existsByEmail(request.email())).thenReturn(true);

        assertThatThrownBy(() -> doctorService.createDoctor(request))
                .isInstanceOf(DuplicateResourceException.class);

        verify(doctorRepository, never()).save(any());
    }

    @Test
    void updateDoctor_shouldReturnUpdatedDoctor() {
        DoctorUpdateRequest request = new DoctorUpdateRequest(
                "Dr. Alice Smith Updated",
                "Cardiology",
                "alice.updated@example.com",
                "0901234568",
                "MED-001",
                11,
                false
        );

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(doctorRepository.existsByEmailAndIdNot(request.email(), 1L)).thenReturn(false);
        when(doctorRepository.existsByPhoneNumberAndIdNot(request.phoneNumber(), 1L)).thenReturn(false);
        when(doctorRepository.existsByLicenseNumberAndIdNot(request.licenseNumber(), 1L)).thenReturn(false);
        when(doctorRepository.save(any(Doctor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DoctorResponse response = doctorService.updateDoctor(1L, request);

        assertThat(response.fullName()).isEqualTo("Dr. Alice Smith Updated");
        assertThat(response.active()).isFalse();
    }

    @Test
    void getDoctorById_shouldReturnDoctor() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        DoctorResponse response = doctorService.getDoctorById(1L);

        assertThat(response.email()).isEqualTo("alice@example.com");
    }

    @Test
    void getAllDoctors_shouldReturnMappedDoctors() {
        when(doctorRepository.findAll()).thenReturn(List.of(doctor));

        List<DoctorResponse> responses = doctorService.getAllDoctors();

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).licenseNumber()).isEqualTo("MED-001");
    }

    @Test
    void deleteDoctor_shouldDeleteExistingDoctor() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        doctorService.deleteDoctor(1L);

        verify(doctorRepository).delete(doctor);
    }

    @Test
    void getDoctorById_shouldThrowWhenMissing() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> doctorService.getDoctorById(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
