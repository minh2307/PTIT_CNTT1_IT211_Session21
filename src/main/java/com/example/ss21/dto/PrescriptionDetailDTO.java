package com.example.ss21.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionDetailDTO {
    private Long id;
    private Long medicineId;
    private String medicineName;
    private int quantity;
    private String dosage;
    private String instructions;
    private int duration;
    private String unit;
}

