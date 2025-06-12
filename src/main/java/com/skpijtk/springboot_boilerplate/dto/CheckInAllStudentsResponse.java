package com.skpijtk.springboot_boilerplate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckInAllStudentsResponse {
    
    private Long studentId;
    private Long userId;
    private String studentName;
    private String nim;
    private String email;
    private AttendanceDto attendanceData;

}
