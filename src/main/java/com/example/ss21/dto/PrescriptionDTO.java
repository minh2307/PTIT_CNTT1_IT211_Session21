package com.example.ss21.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionDTO {
    private Long id;
    private Long patientId;
    private String patientName;
    private Long doctorId;
    private String doctorName;
    private String diagnosis;
    private String notes;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private List<PrescriptionDetailDTO> details;
    private boolean active;
}

