package com.example.ss21.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicineDTO {
    private Long id;
    private String name;
    private String description;
    private double price;
    private String unit;
    private int quantity;
    private boolean active;
}

