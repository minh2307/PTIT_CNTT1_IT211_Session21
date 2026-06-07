package com.example.ss21.service;

import com.example.ss21.doctor.entity.Doctor;
import com.example.ss21.doctor.repository.DoctorRepository;
import com.example.ss21.dto.AppointmentDTO;
import com.example.ss21.entity.Appointment;
import com.example.ss21.entity.Patient;
import com.example.ss21.exception.ResourceNotFoundException;
import com.example.ss21.repository.AppointmentRepository;
import com.example.ss21.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    private Patient patient;
    private Doctor doctor;
    private Appointment appointment;

    @BeforeEach
    void setUp() {
        patient = Patient.builder()
                .id(1L)
                .name("Nguyen Van A")
                .email("nva@example.com")
                .build();

        doctor = Doctor.builder()
                .id(2L)
                .fullName("Dr. Bob")
                .email("bob@example.com")
                .specialty("Cardiology")
                .active(true)
                .build();

        appointment = Appointment.builder()
                .id(10L)
                .patient(patient)
                .doctor(doctor)
                .appointmentDate(LocalDate.now())
                .timeSlot("09:00 - 10:00")
                .symptoms("Chest pain")
                .status("PENDING")
                .active(true)
                .build();
    }

    @Test
    void testCreateAppointment() {
        AppointmentDTO dto = AppointmentDTO.builder()
                .patientId(1L)
                .doctorId(2L)
                .appointmentDate(LocalDate.now())
                .timeSlot("09:00 - 10:00")
                .symptoms("Chest pain")
                .build();

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctor));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        AppointmentDTO result = appointmentService.createAppointment(dto);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("nva@example.com", patient.getEmail());
        assertEquals("Dr. Bob", result.getDoctorName());
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    void testGetAppointmentsByDoctorAndDate() {
        LocalDate today = LocalDate.now();
        when(appointmentRepository.findByDoctorIdAndAppointmentDate(2L, today))
                .thenReturn(Collections.singletonList(appointment));

        List<AppointmentDTO> results = appointmentService.getAppointmentsByDoctorAndDate(2L, today);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Dr. Bob", results.get(0).getDoctorName());
    }

    @Test
    void testGetAppointmentsByPatient() {
        when(appointmentRepository.findByPatientId(1L))
                .thenReturn(Collections.singletonList(appointment));

        List<AppointmentDTO> results = appointmentService.getAppointmentsByPatient(1L);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Nguyen Van A", results.get(0).getPatientName());
    }

    @Test
    void testGetAppointmentById() {
        when(appointmentRepository.findById(10L)).thenReturn(Optional.of(appointment));

        AppointmentDTO result = appointmentService.getAppointmentById(10L);

        assertNotNull(result);
        assertEquals("Chest pain", result.getSymptoms());
    }

    @Test
    void testGetAppointmentByIdNotFound() {
        when(appointmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> appointmentService.getAppointmentById(99L));
    }

    @Test
    void testUpdateAppointmentStatus() {
        when(appointmentRepository.findById(10L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AppointmentDTO result = appointmentService.updateAppointmentStatus(10L, "CONFIRMED");

        assertNotNull(result);
        assertEquals("CONFIRMED", result.getStatus());
    }
}
