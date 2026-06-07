package com.example.ss21.appointment.controller;

import com.example.ss21.appointment.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppointmentController.class)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService appointmentService;

    @Test
    void getAllAppointments_shouldReturnStatusOk() throws Exception {

        mockMvc.perform(
                        get("/api/appointments")
                )
                .andExpect(status().isOk());
    }

    @Test
    void getDoctorAppointments_shouldReturnStatusOk() throws Exception {

        mockMvc.perform(
                        get("/api/appointments/doctor")
                )
                .andExpect(status().isOk());
    }

    @Test
    void getPatientAppointments_shouldReturnStatusOk() throws Exception {

        mockMvc.perform(
                        get("/api/appointments/patient")
                )
                .andExpect(status().isOk());
    }

    @Test
    void cancelAppointment_shouldReturnStatusOk() throws Exception {

        mockMvc.perform(
                        delete("/api/appointments/1")
                )
                .andExpect(status().isOk());
    }
}