package com.example.ss21.service;

import com.example.ss21.dto.PatientDTO;
import com.example.ss21.entity.Patient;
import com.example.ss21.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service quản lý bệnh nhân
 */
@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    // Hàm thêm bệnh nhân mới
    public PatientDTO createPatient(PatientDTO dto) {
        Patient patient = new Patient();
        patient.setName(dto.getName());
        patient.setEmail(dto.getEmail());
        patient.setPhone(dto.getPhone());
        patient.setAddress(dto.getAddress());
        patient.setAge(dto.getAge());
        patient.setGender(dto.getGender());
        patient.setMedicalHistory(dto.getMedicalHistory());
        patient.setActive(dto.isActive());

        Patient saved = patientRepository.save(patient);
        return convertToDTO(saved);
    }

    // Hàm lấy danh sách tất cả bệnh nhân
    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    // Hàm lấy bệnh nhân theo id
    public PatientDTO getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bệnh nhân"));
        return convertToDTO(patient);
    }

    // Hàm cập nhật bệnh nhân
    public PatientDTO updatePatient(Long id, PatientDTO dto) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bệnh nhân"));

        patient.setName(dto.getName());
        patient.setEmail(dto.getEmail());
        patient.setPhone(dto.getPhone());
        patient.setAddress(dto.getAddress());
        patient.setAge(dto.getAge());
        patient.setGender(dto.getGender());
        patient.setMedicalHistory(dto.getMedicalHistory());
        patient.setActive(dto.isActive());

        Patient updated = patientRepository.save(patient);
        return convertToDTO(updated);
    }

    // Hàm xóa bệnh nhân
    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy bệnh nhân để xóa");
        }
        patientRepository.deleteById(id);
    }

    // Hàm chuyển entity sang DTO
    private PatientDTO convertToDTO(Patient patient) {
        return new PatientDTO(
                patient.getId(),
                patient.getName(),
                patient.getEmail(),
                patient.getPhone(),
                patient.getAddress(),
                patient.getAge(),
                patient.getGender(),
                patient.getMedicalHistory(),
                patient.isActive()
        );
    }
}

