package com.example.ss21.appointment.service;

import com.example.ss21.appointment.dto.AppointmentRequest;
import com.example.ss21.appointment.dto.AppointmentResponse;
import com.example.ss21.appointment.entity.Appointment;
import com.example.ss21.appointment.entity.AppointmentStatus;
import com.example.ss21.appointment.exception.AppointmentConflictException;
import com.example.ss21.appointment.mapper.AppointmentMapper;
import com.example.ss21.appointment.repository.AppointmentRepository;
import com.example.ss21.doctor.entity.Doctor;
import com.example.ss21.doctor.repository.DoctorRepository;
import com.example.ss21.patient.entity.Patient;
import com.example.ss21.patient.repository.PatientRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentMapper appointmentMapper;

    public AppointmentServiceImpl(
            AppointmentRepository appointmentRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository,
            AppointmentMapper appointmentMapper) {

        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentMapper = appointmentMapper;
    }

    @Override
    public AppointmentResponse createAppointment(AppointmentRequest request) {

        boolean existed =
                appointmentRepository
                        .existsByDoctorIdAndAppointmentDateAndAppointmentTime(
                                request.getDoctorId(),
                                request.getAppointmentDate(),
                                request.getAppointmentTime()
                        );

        if (existed) {
            throw new AppointmentConflictException(
                    "Doctor already has an appointment at this time"
            );
        }

        Doctor doctor =
                doctorRepository.findById(request.getDoctorId())
                        .orElseThrow(() ->
                                new RuntimeException("Doctor not found"));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        Patient patient =
                patientRepository.findByUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException("Patient not found"));

        Appointment appointment = new Appointment();

        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setReason(request.getReason());
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        Appointment saved =
                appointmentRepository.save(appointment);

        return appointmentMapper.toResponse(saved);
    }

    @Override
    public List<AppointmentResponse> getAllAppointments() {

        return appointmentRepository.findAll()
                .stream()
                .map(appointmentMapper::toResponse)
                .toList();
    }

    @Override
    public List<AppointmentResponse> getDoctorAppointments() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        Doctor doctor =
                doctorRepository.findByUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException("Doctor not found"));

        return appointmentRepository
                .findByDoctorId(doctor.getId())
                .stream()
                .map(appointmentMapper::toResponse)
                .toList();
    }

    @Override
    public List<AppointmentResponse> getPatientAppointments() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        Patient patient =
                patientRepository.findByUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException("Patient not found"));

        return appointmentRepository
                .findByPatientId(patient.getId())
                .stream()
                .map(appointmentMapper::toResponse)
                .toList();
    }

    @Override
    public void cancelAppointment(Long id) {

        Appointment appointment =
                appointmentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Appointment not found"));

        appointment.setStatus(AppointmentStatus.CANCELLED);

        appointmentRepository.save(appointment);
    }
}