package com.skpijtk.springboot_boilerplate.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skpijtk.springboot_boilerplate.dto.ApiResponse;
import com.skpijtk.springboot_boilerplate.dto.CheckInAllStudentsResponse;
import com.skpijtk.springboot_boilerplate.dto.PaginationDto;
import com.skpijtk.springboot_boilerplate.dto.ResumeCheckInResponse;
import com.skpijtk.springboot_boilerplate.service.AttendanceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/resume_checkin")
    public ResponseEntity<ApiResponse<ResumeCheckInResponse>> getResumeCheckin() {
        return ResponseEntity.ok(attendanceService.getResumeCheckin());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/list_checkin_mahasiswa")
    public ResponseEntity<ApiResponse<PaginationDto<CheckInAllStudentsResponse>>> getListCheckInStudents(
            @RequestParam(required = false) String studentName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "nim") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(attendanceService.getCheckInAllStudents(studentName, startDate, endDate, sortBy, sortDir, page, size));
    }
}
