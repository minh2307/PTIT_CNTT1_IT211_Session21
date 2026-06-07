package com.example.ss21.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {
    private Long id;
    private String name;
    private String specialty;
    private String phone;
    private String email;
    private boolean available;
}