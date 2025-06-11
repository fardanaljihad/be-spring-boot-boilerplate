package com.skpijtk.springboot_boilerplate.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skpijtk.springboot_boilerplate.dto.ApiResponse;
import com.skpijtk.springboot_boilerplate.service.AdminService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    
    private final AdminService adminService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<Object>> getProfile() {
        return ResponseEntity.ok(adminService.getProfile());
    }

    @GetMapping("/total_mahasiswa")
    public ResponseEntity<ApiResponse<Object>> getTotalMahasiswa() {
        return ResponseEntity.ok(adminService.getTotalMahasiswa());
    }
    
    
}
