package com.example.ss21.service;

import com.example.ss21.dto.MedicineDTO;
import com.example.ss21.medicine.Medicine;
import com.example.ss21.repository.MedicineRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service quản lý thuốc
 */
@Service
public class MedicineService {

    private final MedicineRepository medicineRepository;

    public MedicineService(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    // Hàm lấy danh sách tất cả thuốc
    public List<MedicineDTO> getAllMedicines() {
        return medicineRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    // Hàm lấy thuốc theo id
    public MedicineDTO getMedicineById(Long id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thuốc"));
        return convertToDTO(medicine);
    }

    // Hàm thêm thuốc mới
    public MedicineDTO createMedicine(MedicineDTO dto) {
        Medicine medicine = new Medicine();
        medicine.setName(dto.getName());
        medicine.setDescription(dto.getDescription());
        medicine.setPrice(dto.getPrice());
        medicine.setUnit(dto.getUnit());
        medicine.setQuantity(dto.getQuantity());
        medicine.setActive(dto.isActive());

        Medicine saved = medicineRepository.save(medicine);
        return convertToDTO(saved);
    }

    // Hàm cập nhật thuốc
    public MedicineDTO updateMedicine(Long id, MedicineDTO dto) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thuốc"));

        medicine.setName(dto.getName());
        medicine.setDescription(dto.getDescription());
        medicine.setPrice(dto.getPrice());
        medicine.setUnit(dto.getUnit());
        medicine.setQuantity(dto.getQuantity());
        medicine.setActive(dto.isActive());

        Medicine updated = medicineRepository.save(medicine);
        return convertToDTO(updated);
    }

    // Hàm xóa thuốc
    public void deleteMedicine(Long id) {
        if (!medicineRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy thuốc để xóa");
        }
        medicineRepository.deleteById(id);
    }

    // Hàm chuyển entity sang DTO
    private MedicineDTO convertToDTO(Medicine medicine) {
        return new MedicineDTO(
                medicine.getId(),
                medicine.getName(),
                medicine.getDescription(),
                medicine.getPrice(),
                medicine.getUnit(),
                medicine.getQuantity(),
                medicine.isActive()
        );
    }
}
