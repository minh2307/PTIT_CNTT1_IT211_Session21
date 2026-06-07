package com.example.ss21.doctor.service;

import com.example.ss21.doctor.dto.request.DoctorCreateRequest;
import com.example.ss21.doctor.dto.request.DoctorUpdateRequest;
import com.example.ss21.doctor.dto.response.DoctorResponse;
import java.util.List;

public interface DoctorService {

    DoctorResponse createDoctor(DoctorCreateRequest request);

    DoctorResponse updateDoctor(Long id, DoctorUpdateRequest request);

    DoctorResponse getDoctorById(Long id);

    List<DoctorResponse> getAllDoctors();

    void deleteDoctor(Long id);
}
