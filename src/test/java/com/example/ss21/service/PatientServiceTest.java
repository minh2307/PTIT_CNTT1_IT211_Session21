package com.example.ss21.service;

import com.example.ss21.dto.PatientDTO;
import com.example.ss21.entity.Patient;
import com.example.ss21.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    private Patient patient;
    private PatientDTO patientDTO;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setId(1L);
        patient.setName("Nguyễn Văn A");
        patient.setEmail("nguyen@example.com");
        patient.setPhone("0123456789");
        patient.setAddress("123 Đường Nguyễn Huệ");
        patient.setAge(30);
        patient.setGender("Nam");
        patient.setMedicalHistory("Tiểu đường");
        patient.setActive(true);

        patientDTO = new PatientDTO();
        patientDTO.setId(1L);
        patientDTO.setName("Nguyễn Văn A");
        patientDTO.setEmail("nguyen@example.com");
        patientDTO.setPhone("0123456789");
        patientDTO.setAddress("123 Đường Nguyễn Huệ");
        patientDTO.setAge(30);
        patientDTO.setGender("Nam");
        patientDTO.setMedicalHistory("Tiểu đường");
        patientDTO.setActive(true);
    }

    @Test
    void testGetAllPatients() {
        List<Patient> patients = new ArrayList<>();
        patients.add(patient);

        when(patientRepository.findAll()).thenReturn(patients);

        List<PatientDTO> result = patientService.getAllPatients();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Nguyễn Văn A", result.get(0).getName());
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    void testGetPatientById() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        PatientDTO result = patientService.getPatientById(1L);

        assertNotNull(result);
        assertEquals("Nguyễn Văn A", result.getName());
        assertEquals("0123456789", result.getPhone());
        verify(patientRepository, times(1)).findById(1L);
    }

    @Test
    void testGetPatientByIdNotFound() {
        when(patientRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> patientService.getPatientById(999L));
        verify(patientRepository, times(1)).findById(999L);
    }

    @Test
    void testCreatePatient() {
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        PatientDTO result = patientService.createPatient(patientDTO);

        assertNotNull(result);
        assertEquals("Nguyễn Văn A", result.getName());
        assertEquals(30, result.getAge());
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void testUpdatePatient() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        PatientDTO updateDTO = new PatientDTO();
        updateDTO.setName("Nguyễn Văn B");
        updateDTO.setEmail("nguyen.b@example.com");
        updateDTO.setPhone("0987654321");
        updateDTO.setAddress("456 Đường Trần Hưng Đạo");
        updateDTO.setAge(35);
        updateDTO.setGender("Nam");
        updateDTO.setMedicalHistory("Huyết áp cao");
        updateDTO.setActive(true);

        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        PatientDTO result = patientService.updatePatient(1L, updateDTO);

        assertNotNull(result);
        verify(patientRepository, times(1)).findById(1L);
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void testDeletePatient() {
        when(patientRepository.existsById(1L)).thenReturn(true);

        patientService.deletePatient(1L);

        verify(patientRepository, times(1)).existsById(1L);
        verify(patientRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeletePatientNotFound() {
        when(patientRepository.existsById(999L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> patientService.deletePatient(999L));
        verify(patientRepository, times(1)).existsById(999L);
    }
}

