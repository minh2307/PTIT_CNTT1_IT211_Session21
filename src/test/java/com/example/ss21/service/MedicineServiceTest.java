package com.example.ss21.service;

import com.example.ss21.dto.MedicineDTO;
import com.example.ss21.medicine.Medicine;
import com.example.ss21.repository.MedicineRepository;
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
public class MedicineServiceTest {

    @Mock
    private MedicineRepository medicineRepository;

    @InjectMocks
    private MedicineService medicineService;

    private Medicine medicine;
    private MedicineDTO medicineDTO;

    @BeforeEach
    void setUp() {
        medicine = new Medicine();
        medicine.setId(1L);
        medicine.setName("Paracetamol");
        medicine.setDescription("Giảm đau hạ sốt");
        medicine.setPrice(5000);
        medicine.setUnit("viên");
        medicine.setQuantity(100);
        medicine.setActive(true);

        medicineDTO = new MedicineDTO();
        medicineDTO.setId(1L);
        medicineDTO.setName("Paracetamol");
        medicineDTO.setDescription("Giảm đau hạ sốt");
        medicineDTO.setPrice(5000);
        medicineDTO.setUnit("viên");
        medicineDTO.setQuantity(100);
        medicineDTO.setActive(true);
    }

    @Test
    void testGetAllMedicines() {
        List<Medicine> medicines = new ArrayList<>();
        medicines.add(medicine);

        when(medicineRepository.findAll()).thenReturn(medicines);

        List<MedicineDTO> result = medicineService.getAllMedicines();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Paracetamol", result.get(0).getName());
        verify(medicineRepository, times(1)).findAll();
    }

    @Test
    void testGetMedicineById() {
        when(medicineRepository.findById(1L)).thenReturn(Optional.of(medicine));

        MedicineDTO result = medicineService.getMedicineById(1L);

        assertNotNull(result);
        assertEquals("Paracetamol", result.getName());
        assertEquals(5000, result.getPrice());
        verify(medicineRepository, times(1)).findById(1L);
    }

    @Test
    void testGetMedicineByIdNotFound() {
        when(medicineRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> medicineService.getMedicineById(999L));
        verify(medicineRepository, times(1)).findById(999L);
    }

    @Test
    void testCreateMedicine() {
        when(medicineRepository.save(any(Medicine.class))).thenReturn(medicine);

        MedicineDTO result = medicineService.createMedicine(medicineDTO);

        assertNotNull(result);
        assertEquals("Paracetamol", result.getName());
        verify(medicineRepository, times(1)).save(any(Medicine.class));
    }

    @Test
    void testUpdateMedicine() {
        when(medicineRepository.findById(1L)).thenReturn(Optional.of(medicine));

        MedicineDTO updateDTO = new MedicineDTO();
        updateDTO.setName("Paracetamol Updated");
        updateDTO.setPrice(6000);
        updateDTO.setDescription("Updated");
        updateDTO.setUnit("hộp");
        updateDTO.setQuantity(50);
        updateDTO.setActive(true);

        when(medicineRepository.save(any(Medicine.class))).thenReturn(medicine);

        MedicineDTO result = medicineService.updateMedicine(1L, updateDTO);

        assertNotNull(result);
        verify(medicineRepository, times(1)).findById(1L);
        verify(medicineRepository, times(1)).save(any(Medicine.class));
    }

    @Test
    void testDeleteMedicine() {
        when(medicineRepository.existsById(1L)).thenReturn(true);

        medicineService.deleteMedicine(1L);

        verify(medicineRepository, times(1)).existsById(1L);
        verify(medicineRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteMedicineNotFound() {
        when(medicineRepository.existsById(999L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> medicineService.deleteMedicine(999L));
        verify(medicineRepository, times(1)).existsById(999L);
    }
}

