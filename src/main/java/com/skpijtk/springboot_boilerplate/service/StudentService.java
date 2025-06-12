package com.skpijtk.springboot_boilerplate.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.skpijtk.springboot_boilerplate.dto.ApiResponse;
import com.skpijtk.springboot_boilerplate.dto.AttendanceDto;
import com.skpijtk.springboot_boilerplate.dto.StudentResponse;
import com.skpijtk.springboot_boilerplate.exception.ResourceNotFoundException;
import com.skpijtk.springboot_boilerplate.model.CheckInStatus;
import com.skpijtk.springboot_boilerplate.model.Student;
import com.skpijtk.springboot_boilerplate.model.User;
import com.skpijtk.springboot_boilerplate.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    
    public ApiResponse<Object> getTotalStudent() {
        long totalMahasiswa = studentRepository.count();

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("totalMahasiswa", totalMahasiswa);

        ApiResponse<Object> response = ApiResponse.builder()
            .data(responseData)
            .message("T-SUCC-005")
            .statusCode(HttpStatus.OK.value())
            .status(HttpStatus.OK.name())
            .build();

        return response;
    }

    public ApiResponse<StudentResponse> getStudent(Long studentId) {
    Student student = studentRepository.findById(studentId)
        .orElseThrow(() -> new ResourceNotFoundException("Student data not found", "T-ERR-005"));

    User user = student.getUser();

    List<AttendanceDto> attendanceDtos = student.getAttendances().stream()
        .map(att -> AttendanceDto.builder()
            .attendanceId(att.getId())
            .attendanceDate(att.getAttendanceDate())
            .checkinTime(att.getCheckInTime())
            .checkoutTime(att.getCheckOutTime())
            .late(att.getCheckInStatus() == CheckInStatus.TERLAMBAT)
            .notesCheckin(att.getCheckInNotes())
            .notesCheckout(att.getCheckOutNotes())
            .status(att.getCheckInStatus().name())
            .build()
        ).collect(Collectors.toList());

    StudentResponse responseData = StudentResponse.builder()
        .studentId(student.getId())
        .userId(user.getId())
        .studentName(user.getName())
        .nim(student.getNim())
        .email(user.getEmail())
        .attendanceData(attendanceDtos)
        .build();

    return ApiResponse.<StudentResponse>builder()
        .data(responseData)
        .message("T-SUCC-004")
        .statusCode(HttpStatus.OK.value())
        .status(HttpStatus.OK.name())
        .build();
}

}
