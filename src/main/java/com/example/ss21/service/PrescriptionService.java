package com.example.ss21.service;

import com.example.ss21.dto.PrescriptionDTO;
import com.example.ss21.dto.PrescriptionDetailDTO;
import com.example.ss21.entity.*;
import com.example.ss21.medicine.Medicine;
import com.example.ss21.repository.*;
import com.example.ss21.doctor.entity.Doctor;
import com.example.ss21.doctor.repository.DoctorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service quản lý đơn thuốc
 */
@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PrescriptionDetailRepository prescriptionDetailRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final MedicineRepository medicineRepository;

    public PrescriptionService(
            PrescriptionRepository prescriptionRepository,
            PrescriptionDetailRepository prescriptionDetailRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository,
            MedicineRepository medicineRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.prescriptionDetailRepository = prescriptionDetailRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.medicineRepository = medicineRepository;
    }

    // Hàm tạo đơn thuốc mới với chi tiết thuốc
    @Transactional
    public PrescriptionDTO createPrescription(PrescriptionDTO dto) {
        // Lấy bệnh nhân
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bệnh nhân"));

        // Lấy bác sĩ
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));

        // Tạo đơn thuốc
        Prescription prescription = new Prescription();
        prescription.setPatient(patient);
        prescription.setDoctor(doctor);
        prescription.setDiagnosis(dto.getDiagnosis());
        prescription.setNotes(dto.getNotes());
        prescription.setCreatedDate(LocalDateTime.now());
        prescription.setUpdatedDate(LocalDateTime.now());
        prescription.setActive(true);

        Prescription saved = prescriptionRepository.save(prescription);

        // Thêm chi tiết thuốc
        if (dto.getDetails() != null && !dto.getDetails().isEmpty()) {
            for (PrescriptionDetailDTO detailDTO : dto.getDetails()) {
                addPrescriptionDetail(saved.getId(), detailDTO);
            }
        }

        return getPrescriptionById(saved.getId());
    }

    // Hàm thêm sau để lấy lại đơn thuốc đầy đủ
    private void addPrescriptionDetail(Long prescriptionId, PrescriptionDetailDTO detailDTO) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn thuốc"));

        Medicine medicine = medicineRepository.findById(detailDTO.getMedicineId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thuốc"));

        PrescriptionDetail detail = new PrescriptionDetail();
        detail.setPrescription(prescription);
        detail.setMedicine(medicine);
        detail.setQuantity(detailDTO.getQuantity());
        detail.setDosage(detailDTO.getDosage());
        detail.setInstructions(detailDTO.getInstructions());
        detail.setDuration(detailDTO.getDuration());
        detail.setUnit(detailDTO.getUnit());

        prescriptionDetailRepository.save(detail);
    }

    // Hàm lấy tất cả đơn thuốc
    public List<PrescriptionDTO> getAllPrescriptions() {
        return prescriptionRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    // Hàm lấy đơn thuốc theo id
    public PrescriptionDTO getPrescriptionById(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn thuốc"));
        return convertToDTO(prescription);
    }

    // Hàm lấy đơn thuốc của bệnh nhân
    public List<PrescriptionDTO> getPrescriptionsByPatientId(Long patientId) {
        return prescriptionRepository.findByPatientId(patientId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    // Hàm lấy đơn thuốc của bác sĩ
    public List<PrescriptionDTO> getPrescriptionsByDoctorId(Long doctorId) {
        return prescriptionRepository.findByDoctorId(doctorId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    // Hàm cập nhật đơn thuốc
    @Transactional
    public PrescriptionDTO updatePrescription(Long id, PrescriptionDTO dto) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn thuốc"));

        prescription.setDiagnosis(dto.getDiagnosis());
        prescription.setNotes(dto.getNotes());
        prescription.setUpdatedDate(LocalDateTime.now());

        Prescription updated = prescriptionRepository.save(prescription);
        return convertToDTO(updated);
    }

    // Hàm xóa đơn thuốc
    @Transactional
    public void deletePrescription(Long id) {
        if (!prescriptionRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy đơn thuốc để xóa");
        }
        prescriptionRepository.deleteById(id);
    }

    // Hàm thêm thuốc vào đơn thuốc
    @Transactional
    public PrescriptionDetailDTO addMedicineToPrescription(Long prescriptionId, PrescriptionDetailDTO detailDTO) {
        addPrescriptionDetail(prescriptionId, detailDTO);
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn thuốc"));

        // Lấy chi tiết vừa thêm
        Medicine medicine = medicineRepository.findById(detailDTO.getMedicineId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thuốc"));

        List<PrescriptionDetail> details = prescriptionDetailRepository.findByPrescriptionId(prescriptionId);
        PrescriptionDetail lastDetail = details.stream()
                .filter(d -> d.getMedicine().getId().equals(medicine.getId()))
                .findFirst()
                .orElseThrow();

        return convertDetailToDTO(lastDetail);
    }

    // Hàm xóa thuốc khỏi đơn thuốc
    @Transactional
    public void removeMedicineFromPrescription(Long detailId) {
        if (!prescriptionDetailRepository.existsById(detailId)) {
            throw new RuntimeException("Không tìm thấy chi tiết đơn thuốc");
        }
        prescriptionDetailRepository.deleteById(detailId);
    }

    // Hàm chuyển entity sang DTO
    private PrescriptionDTO convertToDTO(Prescription prescription) {
        List<PrescriptionDetailDTO> details = prescriptionDetailRepository
                .findByPrescriptionId(prescription.getId()).stream()
                .map(this::convertDetailToDTO)
                .toList();

        return new PrescriptionDTO(
                prescription.getId(),
                prescription.getPatient().getId(),
                prescription.getPatient().getName(),
                prescription.getDoctor().getId(),
                prescription.getDoctor().getName(),
                prescription.getDiagnosis(),
                prescription.getNotes(),
                prescription.getCreatedDate(),
                prescription.getUpdatedDate(),
                details,
                prescription.isActive()
        );
    }

    // Hàm chuyển PrescriptionDetail entity sang DTO
    private PrescriptionDetailDTO convertDetailToDTO(PrescriptionDetail detail) {
        return new PrescriptionDetailDTO(
                detail.getId(),
                detail.getMedicine().getId(),
                detail.getMedicine().getName(),
                detail.getQuantity(),
                detail.getDosage(),
                detail.getInstructions(),
                detail.getDuration(),
                detail.getUnit()
        );
    }
}


