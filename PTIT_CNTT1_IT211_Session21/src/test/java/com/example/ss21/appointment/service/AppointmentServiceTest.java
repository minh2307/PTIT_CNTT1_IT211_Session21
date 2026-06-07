package com.example.ss21.appointment.service;

import com.example.ss21.appointment.dto.AppointmentRequest;
import com.example.ss21.appointment.entity.Appointment;
import com.example.ss21.appointment.exception.AppointmentConflictException;
import com.example.ss21.appointment.mapper.AppointmentMapper;
import com.example.ss21.appointment.repository.AppointmentRepository;
import com.example.ss21.doctor.repository.DoctorRepository;
import com.example.ss21.patient.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private AppointmentMapper appointmentMapper;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private AppointmentRequest request;

    @BeforeEach
    void setUp() {

        request = new AppointmentRequest();

        request.setDoctorId(1L);
        request.setAppointmentDate(LocalDate.now().plusDays(1));
        request.setAppointmentTime(LocalTime.of(10, 0));
        request.setReason("General checkup");
    }

    @Test
    void createAppointment_duplicateSchedule_shouldThrowException() {

        when(
                appointmentRepository
                        .existsByDoctorIdAndAppointmentDateAndAppointmentTime(
                                request.getDoctorId(),
                                request.getAppointmentDate(),
                                request.getAppointmentTime()
                        )
        ).thenReturn(true);

        assertThrows(
                AppointmentConflictException.class,
                () -> appointmentService.createAppointment(request)
        );
    }

    @Test
    void cancelAppointment_notFound_shouldThrowException() {

        assertThrows(
                RuntimeException.class,
                () -> appointmentService.cancelAppointment(999L)
        );
    }
}