package com.skpijtk.springboot_boilerplate.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skpijtk.springboot_boilerplate.dto.ApiResponse;
import com.skpijtk.springboot_boilerplate.service.StudentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/total_mahasiswa")
    public ResponseEntity<ApiResponse<Object>> getTotalMahasiswa() {
        return ResponseEntity.ok(studentService.getTotalMahasiswa());
    }

}
