package com.example.ss21.service;

import com.example.ss21.dto.PrescriptionDTO;
import com.example.ss21.dto.PrescriptionDetailDTO;
import com.example.ss21.entity.*;
import com.example.ss21.medicine.Medicine;
import com.example.ss21.repository.*;
import com.example.ss21.doctor.entity.Doctor;
import com.example.ss21.doctor.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PrescriptionServiceTest {

    @Mock
    private PrescriptionRepository prescriptionRepository;

    @Mock
    private PrescriptionDetailRepository prescriptionDetailRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private MedicineRepository medicineRepository;

    @InjectMocks
    private PrescriptionService prescriptionService;

    private Prescription prescription;
    private Patient patient;
    private Doctor doctor;
    private Medicine medicine;
    private PrescriptionDetail prescriptionDetail;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setId(1L);
        patient.setName("Nguyễn Văn A");
        patient.setEmail("nguyen@example.com");
        patient.setAge(30);

        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("Dr. Lê Văn B");
        doctor.setSpecialty("Tim mạch");

        medicine = new Medicine();
        medicine.setId(1L);
        medicine.setName("Paracetamol");
        medicine.setPrice(5000);

        prescription = new Prescription();
        prescription.setId(1L);
        prescription.setPatient(patient);
        prescription.setDoctor(doctor);
        prescription.setDiagnosis("Cảm cúm");
        prescription.setNotes("Hạ sốt 39 độ");
        prescription.setCreatedDate(LocalDateTime.now());
        prescription.setUpdatedDate(LocalDateTime.now());
        prescription.setActive(true);

        prescriptionDetail = new PrescriptionDetail();
        prescriptionDetail.setId(1L);
        prescriptionDetail.setPrescription(prescription);
        prescriptionDetail.setMedicine(medicine);
        prescriptionDetail.setQuantity(10);
        prescriptionDetail.setDosage("2 viên");
        prescriptionDetail.setInstructions("Uống sau bữa ăn");
        prescriptionDetail.setDuration(3);
        prescriptionDetail.setUnit("ngày");

        prescription.getPrescriptionDetails().add(prescriptionDetail);
    }

    @Test
    void testCreatePrescription() {
        PrescriptionDTO dto = new PrescriptionDTO();
        dto.setPatientId(1L);
        dto.setDoctorId(1L);
        dto.setDiagnosis("Cảm cúm");
        dto.setNotes("Hạ sốt");
        dto.setDetails(new ArrayList<>());

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(prescription);
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(prescription));
        when(prescriptionDetailRepository.findByPrescriptionId(any())).thenReturn(new ArrayList<>());

        PrescriptionDTO result = prescriptionService.createPrescription(dto);

        assertNotNull(result);
        assertEquals("Cảm cúm", result.getDiagnosis());
        verify(patientRepository, times(1)).findById(1L);
        verify(doctorRepository, times(1)).findById(1L);
        verify(prescriptionRepository, times(1)).save(any(Prescription.class));
    }

    @Test
    void testGetPrescriptionById() {
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(prescription));
        when(prescriptionDetailRepository.findByPrescriptionId(1L)).thenReturn(List.of(prescriptionDetail));

        PrescriptionDTO result = prescriptionService.getPrescriptionById(1L);

        assertNotNull(result);
        assertEquals("Cảm cúm", result.getDiagnosis());
        assertEquals("Nguyễn Văn A", result.getPatientName());
        verify(prescriptionRepository, times(1)).findById(1L);
    }

    @Test
    void testGetPrescriptionByIdNotFound() {
        when(prescriptionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> prescriptionService.getPrescriptionById(999L));
        verify(prescriptionRepository, times(1)).findById(999L);
    }

    @Test
    void testGetPrescriptionsByPatientId() {
        List<Prescription> prescriptions = List.of(prescription);

        when(prescriptionRepository.findByPatientId(1L)).thenReturn(prescriptions);
        when(prescriptionDetailRepository.findByPrescriptionId(1L)).thenReturn(List.of(prescriptionDetail));

        List<PrescriptionDTO> result = prescriptionService.getPrescriptionsByPatientId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(prescriptionRepository, times(1)).findByPatientId(1L);
    }

    @Test
    void testGetPrescriptionsByDoctorId() {
        List<Prescription> prescriptions = List.of(prescription);

        when(prescriptionRepository.findByDoctorId(1L)).thenReturn(prescriptions);
        when(prescriptionDetailRepository.findByPrescriptionId(1L)).thenReturn(List.of(prescriptionDetail));

        List<PrescriptionDTO> result = prescriptionService.getPrescriptionsByDoctorId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(prescriptionRepository, times(1)).findByDoctorId(1L);
    }

    @Test
    void testUpdatePrescription() {
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(prescription));
        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(prescription);
        when(prescriptionDetailRepository.findByPrescriptionId(1L)).thenReturn(List.of(prescriptionDetail));

        PrescriptionDTO updateDTO = new PrescriptionDTO();
        updateDTO.setDiagnosis("Cảm cúm nặng");
        updateDTO.setNotes("Hạ sốt 40 độ");

        PrescriptionDTO result = prescriptionService.updatePrescription(1L, updateDTO);

        assertNotNull(result);
        verify(prescriptionRepository, times(1)).findById(1L);
        verify(prescriptionRepository, times(1)).save(any(Prescription.class));
    }

    @Test
    void testDeletePrescription() {
        when(prescriptionRepository.existsById(1L)).thenReturn(true);

        prescriptionService.deletePrescription(1L);

        verify(prescriptionRepository, times(1)).existsById(1L);
        verify(prescriptionRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeletePrescriptionNotFound() {
        when(prescriptionRepository.existsById(999L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> prescriptionService.deletePrescription(999L));
        verify(prescriptionRepository, times(1)).existsById(999L);
    }

    @Test
    void testAddMedicineToPrescription() {
        PrescriptionDetailDTO detailDTO = new PrescriptionDetailDTO();
        detailDTO.setMedicineId(1L);
        detailDTO.setQuantity(10);
        detailDTO.setDosage("2 viên");
        detailDTO.setInstructions("Uống sau bữa ăn");
        detailDTO.setDuration(3);
        detailDTO.setUnit("ngày");

        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(prescription));
        when(medicineRepository.findById(1L)).thenReturn(Optional.of(medicine));
        when(prescriptionDetailRepository.save(any(PrescriptionDetail.class))).thenReturn(prescriptionDetail);
        when(prescriptionDetailRepository.findByPrescriptionId(1L)).thenReturn(List.of(prescriptionDetail));

        PrescriptionDetailDTO result = prescriptionService.addMedicineToPrescription(1L, detailDTO);

        assertNotNull(result);
        assertEquals(1L, result.getMedicineId());
        verify(prescriptionRepository, atLeast(1)).findById(1L);
        verify(medicineRepository, atLeast(1)).findById(1L);
    }

    @Test
    void testRemoveMedicineFromPrescription() {
        when(prescriptionDetailRepository.existsById(1L)).thenReturn(true);

        prescriptionService.removeMedicineFromPrescription(1L);

        verify(prescriptionDetailRepository, times(1)).existsById(1L);
        verify(prescriptionDetailRepository, times(1)).deleteById(1L);
    }
}





