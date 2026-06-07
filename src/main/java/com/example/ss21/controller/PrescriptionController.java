package com.example.ss21.controller;

import com.example.ss21.dto.PrescriptionDTO;
import com.example.ss21.dto.PrescriptionDetailDTO;
import com.example.ss21.service.PrescriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PostMapping
    public ResponseEntity<PrescriptionDTO> create(@RequestBody PrescriptionDTO dto) {
        return ResponseEntity.ok(prescriptionService.createPrescription(dto));
    }

    @GetMapping
    public ResponseEntity<List<PrescriptionDTO>> getAll() {
        return ResponseEntity.ok(prescriptionService.getAllPrescriptions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionById(id));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PrescriptionDTO>> getByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionsByPatientId(patientId));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<PrescriptionDTO>> getByDoctorId(@PathVariable Long doctorId) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionsByDoctorId(doctorId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrescriptionDTO> update(@PathVariable Long id, @RequestBody PrescriptionDTO dto) {
        return ResponseEntity.ok(prescriptionService.updatePrescription(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        prescriptionService.deletePrescription(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/medicines")
    public ResponseEntity<PrescriptionDetailDTO> addMedicine(
            @PathVariable Long id,
            @RequestBody PrescriptionDetailDTO detailDTO) {
        return ResponseEntity.ok(prescriptionService.addMedicineToPrescription(id, detailDTO));
    }

    @DeleteMapping("/medicines/{detailId}")
    public ResponseEntity<Void> removeMedicine(@PathVariable Long detailId) {
        prescriptionService.removeMedicineFromPrescription(detailId);
        return ResponseEntity.noContent().build();
    }
}

