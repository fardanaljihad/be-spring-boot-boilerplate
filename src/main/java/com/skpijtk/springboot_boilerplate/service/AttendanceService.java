package com.skpijtk.springboot_boilerplate.service;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.skpijtk.springboot_boilerplate.dto.ApiResponse;
import com.skpijtk.springboot_boilerplate.dto.ResumeCheckInResponse;
import com.skpijtk.springboot_boilerplate.model.CheckInStatus;
import com.skpijtk.springboot_boilerplate.repository.AttendanceRepository;
import com.skpijtk.springboot_boilerplate.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;

    public ApiResponse<ResumeCheckInResponse> getResumeCheckin() {
        LocalDate today = LocalDate.now();
        long totalMahasiswa = studentRepository.count();
        long totalCheckIn = attendanceRepository.countByAttendanceDate(today);
        long totalBelumCheckIn = totalMahasiswa - totalCheckIn;
        long totalTelatCheckIn = attendanceRepository.countByAttendanceDateAndCheckInStatus(today, CheckInStatus.TERLAMBAT);

        ResumeCheckInResponse responseData = ResumeCheckInResponse.builder()
            .totalMahasiswa(totalMahasiswa)
            .totalCheckIn(totalCheckIn)
            .totalBelumCheckIn(totalBelumCheckIn)
            .totalTelatCheckIn(totalTelatCheckIn)
            .build();

        ApiResponse<ResumeCheckInResponse> response = ApiResponse.<ResumeCheckInResponse>builder()
            .data(responseData)
            .message("T-SUCC-005")
            .statusCode(HttpStatus.OK.value())
            .status(HttpStatus.OK.name())
            .build();

        return response;
    }
    
}
