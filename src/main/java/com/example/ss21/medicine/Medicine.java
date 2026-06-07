package com.example.ss21.medicine;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@Table(name = "medicines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên thuốc không được để trống")
    private String name;

    private String description;

    @Positive(message = "Giá thuốc phải lớn hơn 0")
    private double price;

    private String unit;

    private int quantity;

    private boolean active = true;
}
