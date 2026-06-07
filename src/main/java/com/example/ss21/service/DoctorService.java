package com.example.ss21.service;

import com.example.ss21.dto.DoctorDTO;
import com.example.ss21.entity.Doctor;
import com.example.ss21.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service quản lý bác sĩ
 */
@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    // Hàm thêm bác sĩ mới
    public DoctorDTO createDoctor(DoctorDTO dto) {
        Doctor doctor = new Doctor();
        doctor.setName(dto.getName());
        doctor.setSpecialty(dto.getSpecialty());
        doctor.setPhone(dto.getPhone());
        doctor.setEmail(dto.getEmail());
        doctor.setAvailable(dto.isAvailable());

        Doctor saved = doctorRepository.save(doctor);
        return convertToDTO(saved);
    }

    // Hàm lấy danh sách tất cả bác sĩ
    public List<DoctorDTO> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    // Hàm lấy bác sĩ theo id
    public DoctorDTO getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));
        return convertToDTO(doctor);
    }

    // Hàm cập nhật bác sĩ
    public DoctorDTO updateDoctor(Long id, DoctorDTO dto) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));

        doctor.setName(dto.getName());
        doctor.setSpecialty(dto.getSpecialty());
        doctor.setPhone(dto.getPhone());
        doctor.setEmail(dto.getEmail());
        doctor.setAvailable(dto.isAvailable());

        Doctor updated = doctorRepository.save(doctor);
        return convertToDTO(updated);
    }

    // Hàm xóa bác sĩ
    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy bác sĩ để xóa");
        }
        doctorRepository.deleteById(id);
    }

    // Hàm chuyển entity sang DTO
    private DoctorDTO convertToDTO(Doctor doctor) {
        return new DoctorDTO(
                doctor.getId(),
                doctor.getName(),
                doctor.getSpecialty(),
                doctor.getPhone(),
                doctor.getEmail(),
                doctor.isAvailable()
        );
    }
}
