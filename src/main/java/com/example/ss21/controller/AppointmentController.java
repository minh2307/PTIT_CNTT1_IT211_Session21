package com.example.ss21.controller;

import com.example.ss21.dto.AppointmentDTO;
import com.example.ss21.entity.User;
import com.example.ss21.doctor.entity.Doctor;
import com.example.ss21.entity.Patient;
import com.example.ss21.doctor.repository.DoctorRepository;
import com.example.ss21.repository.PatientRepository;
import com.example.ss21.repository.UserRepository;
import com.example.ss21.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @PostMapping
    @PreAuthorize("hasAnyRole('PATIENT', 'ADMIN')")
    public ResponseEntity<AppointmentDTO> bookAppointment(@RequestBody AppointmentDTO dto, Authentication authentication) {
        if (hasRole(authentication, "ROLE_PATIENT")) {
            User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
            Patient patient = patientRepository.findById(dto.getPatientId()).orElseThrow();
            if (user.getEmail() == null || !user.getEmail().equalsIgnoreCase(patient.getEmail())) {
                throw new AccessDeniedException("Patients can only book appointments for themselves");
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.createAppointment(dto));
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByDoctor(
            @PathVariable Long doctorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Authentication authentication) {
        
        if (hasRole(authentication, "ROLE_DOCTOR")) {
            User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
            Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();
            if (user.getEmail() == null || !user.getEmail().equalsIgnoreCase(doctor.getEmail())) {
                throw new AccessDeniedException("Doctors can only view their own schedule");
            }
        }

        if (date != null) {
            return ResponseEntity.ok(appointmentService.getAppointmentsByDoctorAndDate(doctorId, date));
        }
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctor(doctorId));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('PATIENT', 'ADMIN')")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByPatient(
            @PathVariable Long patientId,
            Authentication authentication) {
        
        if (hasRole(authentication, "ROLE_PATIENT")) {
            User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
            Patient patient = patientRepository.findById(patientId).orElseThrow();
            if (user.getEmail() == null || !user.getEmail().equalsIgnoreCase(patient.getEmail())) {
                throw new AccessDeniedException("Patients can only view their own appointments");
            }
        }

        return ResponseEntity.ok(appointmentService.getAppointmentsByPatient(patientId));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable Long id, Authentication authentication) {
        AppointmentDTO dto = appointmentService.getAppointmentById(id);
        
        if (hasRole(authentication, "ROLE_DOCTOR")) {
            User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
            Doctor doctor = doctorRepository.findById(dto.getDoctorId()).orElseThrow();
            if (user.getEmail() == null || !user.getEmail().equalsIgnoreCase(doctor.getEmail())) {
                throw new AccessDeniedException("Doctors can only view their own appointments");
            }
        } else if (hasRole(authentication, "ROLE_PATIENT")) {
            User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
            Patient patient = patientRepository.findById(dto.getPatientId()).orElseThrow();
            if (user.getEmail() == null || !user.getEmail().equalsIgnoreCase(patient.getEmail())) {
                throw new AccessDeniedException("Patients can only view their own appointments");
            }
        }

        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<AppointmentDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            Authentication authentication) {
        
        AppointmentDTO dto = appointmentService.getAppointmentById(id);
        if (hasRole(authentication, "ROLE_DOCTOR")) {
            User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
            Doctor doctor = doctorRepository.findById(dto.getDoctorId()).orElseThrow();
            if (user.getEmail() == null || !user.getEmail().equalsIgnoreCase(doctor.getEmail())) {
                throw new AccessDeniedException("Doctors can only update status of their own appointments");
            }
        }

        return ResponseEntity.ok(appointmentService.updateAppointmentStatus(id, status));
    }

    private boolean hasRole(Authentication auth, String role) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(role));
    }
}
