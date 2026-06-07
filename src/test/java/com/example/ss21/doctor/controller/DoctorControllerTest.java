package com.example.ss21.doctor.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.ss21.doctor.dto.request.DoctorCreateRequest;
import com.example.ss21.doctor.dto.request.DoctorUpdateRequest;
import com.example.ss21.doctor.entity.Doctor;
import com.example.ss21.doctor.repository.DoctorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private DoctorRepository doctorRepository;

    @BeforeEach
    void setUp() {
        doctorRepository.deleteAll();
    }

    @Test
    void createDoctor_shouldReturn201() throws Exception {
        DoctorCreateRequest request = new DoctorCreateRequest(
                "Dr. Bob Brown",
                "Neurology",
                "bob@example.com",
                "0907654321",
                "MED-002",
                8,
                true
        );

        mockMvc.perform(post("/api/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.fullName").value("Dr. Bob Brown"));
    }

    @Test
    void createDoctor_shouldReturn400WhenInvalid() throws Exception {
        DoctorCreateRequest request = new DoctorCreateRequest(
                "",
                "",
                "invalid-email",
                "abc",
                "",
                -1,
                true
        );

        mockMvc.perform(post("/api/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void getDoctorById_shouldReturn200() throws Exception {
        Doctor doctor = doctorRepository.save(Doctor.builder()
                .fullName("Dr. Alice Smith")
                .specialty("Cardiology")
                .email("alice@example.com")
                .phoneNumber("0901234567")
                .licenseNumber("MED-001")
                .experienceYears(10)
                .active(true)
                .build());

        mockMvc.perform(get("/api/doctors/" + doctor.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void getAllDoctors_shouldReturn200() throws Exception {
        doctorRepository.save(Doctor.builder()
                .fullName("Dr. Alice Smith")
                .specialty("Cardiology")
                .email("alice@example.com")
                .phoneNumber("0901234567")
                .licenseNumber("MED-001")
                .experienceYears(10)
                .active(true)
                .build());

        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNumber());
    }

    @Test
    void updateDoctor_shouldReturn200() throws Exception {
        Doctor doctor = doctorRepository.save(Doctor.builder()
                .fullName("Dr. Alice Smith")
                .specialty("Cardiology")
                .email("alice@example.com")
                .phoneNumber("0901234567")
                .licenseNumber("MED-001")
                .experienceYears(10)
                .active(true)
                .build());

        DoctorUpdateRequest request = new DoctorUpdateRequest(
                "Dr. Alice Smith Updated",
                "Cardiology",
                "alice.updated@example.com",
                "0901234568",
                "MED-001",
                11,
                false
        );

        mockMvc.perform(put("/api/doctors/" + doctor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));
    }

    @Test
    void deleteDoctor_shouldReturn204() throws Exception {
        Doctor doctor = doctorRepository.save(Doctor.builder()
                .fullName("Dr. Alice Smith")
                .specialty("Cardiology")
                .email("alice@example.com")
                .phoneNumber("0901234567")
                .licenseNumber("MED-001")
                .experienceYears(10)
                .active(true)
                .build());

        mockMvc.perform(delete("/api/doctors/" + doctor.getId()))
                .andExpect(status().isNoContent());
    }
}
