package com.skpijtk.springboot_boilerplate.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skpijtk.springboot_boilerplate.dto.ApiResponse;
import com.skpijtk.springboot_boilerplate.dto.CheckInAllStudentsResponse;
import com.skpijtk.springboot_boilerplate.dto.PaginationDto;
import com.skpijtk.springboot_boilerplate.dto.StudentResponse;
import com.skpijtk.springboot_boilerplate.service.AttendanceService;
import com.skpijtk.springboot_boilerplate.service.StudentService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final AttendanceService attendanceService;
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/total_mahasiswa")
    public ResponseEntity<ApiResponse<Object>> getTotalStudent() {
        return ResponseEntity.ok(studentService.getTotalStudent());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/mahasiswa/{id_student}")
    public ResponseEntity<ApiResponse<StudentResponse>> getStudent(@PathVariable("id_student") long id_student) {
        return ResponseEntity.ok(studentService.getStudent(id_student));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/list_attendance_mahasiswa")
    public ResponseEntity<ApiResponse<PaginationDto<CheckInAllStudentsResponse>>> getListAttendanceStudent(
        @RequestParam(required = false) long student_id,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startdate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate enddate,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(attendanceService.getListAttendanceStudent(student_id, startdate, enddate, page, size));
    }
    

}
