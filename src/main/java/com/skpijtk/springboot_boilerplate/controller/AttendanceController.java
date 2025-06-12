package com.skpijtk.springboot_boilerplate.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skpijtk.springboot_boilerplate.dto.ApiResponse;
import com.skpijtk.springboot_boilerplate.dto.CheckInAllStudentsResponse;
import com.skpijtk.springboot_boilerplate.dto.CheckInCheckOutResponse;
import com.skpijtk.springboot_boilerplate.dto.CheckInRequest;
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
            @RequestParam(required = false) String student_name,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startdate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate enddate,
            @RequestParam(defaultValue = "nim") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(attendanceService.getCheckInAllStudents(student_name, startdate, enddate, sortBy, sortDir, page, size));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/list_all_mahasiswa")
    public ResponseEntity<ApiResponse<PaginationDto<CheckInAllStudentsResponse>>> getListAllStudents(
            @RequestParam(required = false) String student_name,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startdate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate enddate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(attendanceService.getAllStudents(student_name, startdate, enddate, page, size));
    }

    @PreAuthorize("hasRole('MAHASISWA')")
    @PostMapping("/mahasiswa/checkin")
    public ResponseEntity<ApiResponse<CheckInCheckOutResponse>> studentCheckIn(@RequestBody CheckInRequest request) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(attendanceService.studentCheckIn(request));
    }

}
