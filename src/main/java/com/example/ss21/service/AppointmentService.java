package com.example.ss21.service;

import com.example.ss21.doctor.entity.Doctor;
import com.example.ss21.doctor.repository.DoctorRepository;
import com.example.ss21.dto.AppointmentDTO;
import com.example.ss21.entity.Appointment;
import com.example.ss21.entity.Patient;
import com.example.ss21.exception.ResourceNotFoundException;
import com.example.ss21.repository.AppointmentRepository;
import com.example.ss21.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @Transactional
    public AppointmentDTO createAppointment(AppointmentDTO dto) {
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + dto.getPatientId()));

        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + dto.getDoctorId()));

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentDate(dto.getAppointmentDate())
                .timeSlot(dto.getTimeSlot())
                .symptoms(dto.getSymptoms())
                .status("PENDING")
                .active(true)
                .build();

        Appointment saved = appointmentRepository.save(appointment);
        return convertToDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAppointmentsByDoctorAndDate(Long doctorId, LocalDate date) {
        // Find appointments and map to DTOs using Java Stream API
        return appointmentRepository.findByDoctorIdAndAppointmentDate(doctorId, date).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAppointmentsByPatient(Long patientId) {
        // Find appointments and map to DTOs using Java Stream API
        return appointmentRepository.findByPatientId(patientId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAppointmentsByDoctor(Long doctorId) {
        // Find appointments and map to DTOs using Java Stream API
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public AppointmentDTO getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
        return convertToDTO(appointment);
    }

    @Transactional
    public AppointmentDTO updateAppointmentStatus(Long id, String status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
        appointment.setStatus(status);
        Appointment updated = appointmentRepository.save(appointment);
        return convertToDTO(updated);
    }

    private AppointmentDTO convertToDTO(Appointment appointment) {
        return AppointmentDTO.builder()
                .id(appointment.getId())
                .patientId(appointment.getPatient().getId())
                .patientName(appointment.getPatient().getName())
                .doctorId(appointment.getDoctor().getId())
                .doctorName(appointment.getDoctor().getFullName())
                .appointmentDate(appointment.getAppointmentDate())
                .timeSlot(appointment.getTimeSlot())
                .symptoms(appointment.getSymptoms())
                .status(appointment.getStatus())
                .active(appointment.isActive())
                .build();
    }
}
