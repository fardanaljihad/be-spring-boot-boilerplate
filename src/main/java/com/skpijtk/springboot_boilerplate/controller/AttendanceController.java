package com.skpijtk.springboot_boilerplate.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skpijtk.springboot_boilerplate.dto.ApiResponse;
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

}
