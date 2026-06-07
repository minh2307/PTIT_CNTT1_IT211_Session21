package com.example.ss21.appointment.service;

import com.example.ss21.appointment.dto.AppointmentRequest;
import com.example.ss21.appointment.dto.AppointmentResponse;

import java.util.List;

public interface AppointmentService {

    AppointmentResponse createAppointment(AppointmentRequest request);

    List<AppointmentResponse> getAllAppointments();

    List<AppointmentResponse> getDoctorAppointments();

    List<AppointmentResponse> getPatientAppointments();

    void cancelAppointment(Long id);
}