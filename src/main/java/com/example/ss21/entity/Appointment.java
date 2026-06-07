package com.example.ss21.entity;

import com.example.ss21.doctor.entity.Doctor;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(nullable = false)
    private LocalDate appointmentDate;

    @Column(nullable = false)
    private String timeSlot; // e.g. "08:00 - 09:00"

    private String symptoms;

    @Builder.Default
    private String status = "PENDING"; // PENDING, CONFIRMED, COMPLETED, CANCELLED

    @Builder.Default
    private boolean active = true;
}
