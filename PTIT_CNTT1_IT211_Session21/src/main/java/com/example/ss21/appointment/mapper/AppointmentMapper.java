package com.example.ss21.appointment.mapper;

import com.example.ss21.appointment.dto.AppointmentResponse;
import com.example.ss21.appointment.entity.Appointment;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    public AppointmentResponse toResponse(Appointment appointment) {

        AppointmentResponse response = new AppointmentResponse();

        response.setId(appointment.getId());

        if (appointment.getDoctor() != null) {
            response.setDoctorName(
                    appointment.getDoctor().toString()
            );
        }

        if (appointment.getPatient() != null) {
            response.setPatientName(
                    appointment.getPatient().toString()
            );
        }

        response.setAppointmentDate(
                appointment.getAppointmentDate()
        );

        response.setAppointmentTime(
                appointment.getAppointmentTime()
        );

        response.setReason(
                appointment.getReason()
        );

        if (appointment.getStatus() != null) {
            response.setStatus(
                    appointment.getStatus().name()
            );
        }

        return response;
    }
}