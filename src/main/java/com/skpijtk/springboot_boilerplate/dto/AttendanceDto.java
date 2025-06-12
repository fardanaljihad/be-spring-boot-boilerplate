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
public class AttendanceDto {
    
    private Long attendanceId;
    private LocalDateTime checkinTime;
    private LocalDateTime checkoutTime;
    private LocalDate attendanceDate;
    private Boolean late;
    private String notesCheckin;
    private String notesCheckout;
    private String status;

}
