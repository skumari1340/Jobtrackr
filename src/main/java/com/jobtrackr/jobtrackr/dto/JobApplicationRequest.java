package com.jobtrackr.jobtrackr.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class JobApplicationRequest {
    private String company;
    private String role;
    private String status;
    private LocalDate appliedDate;
    private String notes;
    private Long userId;
}
