package com.skpijtk.springboot_boilerplate.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponse {

    private long studentId;
    private long userId;
    private String studentName;
    private String nim;
    private String email;
    private List<AttendanceDto> attendanceData;
    
}
