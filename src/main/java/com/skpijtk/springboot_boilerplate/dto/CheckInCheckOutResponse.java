package com.skpijtk.springboot_boilerplate.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckInCheckOutResponse {
    
    private Long studentId;
    private String studentName;
    private String nim;
    private Long attendanceId;
    private LocalDateTime checkinTime;
    private LocalDateTime checkOutTime;
    private LocalDate attendanceDate;
    private String notesCheckin;
    private String notesCheckout;
    private String statusCheckin;

}
