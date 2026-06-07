package com.example.ss21.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên bệnh nhân không được để trống")
    private String name;

    @Email(message = "Email không hợp lệ")
    private String email;

    private String phone;

    private String address;

    private int age;

    private String gender;

    private String medicalHistory;

    private boolean active = true;
}

