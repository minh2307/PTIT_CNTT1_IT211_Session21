package com.example.ss21.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private int age;
    private String gender;
    private String medicalHistory;
    private boolean active;
}

