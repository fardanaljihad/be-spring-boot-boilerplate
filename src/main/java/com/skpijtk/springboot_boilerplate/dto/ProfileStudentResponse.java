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
public class ProfileStudentResponse {
    
    private Long studentId;
    private String studentName;
    private String nim;
    private List<AttendanceDto> attendanceData;

    private int totalData;
    private int totalPage;
    private int currentPage;
    private int pageSize;

}
