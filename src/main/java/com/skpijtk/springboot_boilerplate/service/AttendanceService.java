package com.skpijtk.springboot_boilerplate.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.skpijtk.springboot_boilerplate.dto.ApiResponse;
import com.skpijtk.springboot_boilerplate.dto.AttendanceDto;
import com.skpijtk.springboot_boilerplate.dto.CheckInAllStudentsResponse;
import com.skpijtk.springboot_boilerplate.dto.PaginationDto;
import com.skpijtk.springboot_boilerplate.dto.ResumeCheckInResponse;
import com.skpijtk.springboot_boilerplate.exception.IllegalArgumentException;
import com.skpijtk.springboot_boilerplate.model.Attendance;
import com.skpijtk.springboot_boilerplate.model.CheckInStatus;
import com.skpijtk.springboot_boilerplate.model.Student;
import com.skpijtk.springboot_boilerplate.model.User;
import com.skpijtk.springboot_boilerplate.repository.AttendanceRepository;
import com.skpijtk.springboot_boilerplate.repository.AttendanceSpecification;
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

    public ApiResponse<PaginationDto<CheckInAllStudentsResponse>> getCheckInAllStudents(
        String studentName,
        LocalDate startDate,
        LocalDate endDate,
        String sortBy,
        String sortDir,
        int page,
        int size
    ) {
        
        Pageable pageable = PageRequest.of(page, size);

        if (!"nim".equalsIgnoreCase(sortBy) && !"status".equalsIgnoreCase(sortBy)) {
            throw new IllegalArgumentException("Data failed to display", "T-ERR-006");
        }

        Specification<Attendance> spec = AttendanceSpecification.filterBy(
            studentName, startDate, endDate, sortBy, sortDir
        );

        Page<Attendance> attendancePage = attendanceRepository.findAll(spec, pageable);

        Page<CheckInAllStudentsResponse> resultPage = attendancePage.map(att -> {
            Student student = att.getStudent();
            User user = student.getUser();

            return CheckInAllStudentsResponse.builder()
                .studentId(student.getId())
                .userId(user.getId())
                .studentName(user.getName())
                .nim(student.getNim())
                .email(user.getEmail())
                .attendanceData(AttendanceDto.builder()
                    .attendanceId(att.getId())
                    .attendanceDate(att.getAttendanceDate())
                    .checkinTime(att.getCheckInTime())
                    .checkoutTime(att.getCheckOutTime()) 
                    .late(att.getCheckInStatus() == CheckInStatus.TERLAMBAT)
                    .notesCheckin(att.getCheckInNotes())
                    .notesCheckout(att.getCheckOutNotes())
                    .status(att.getCheckInStatus().name())
                    .build())
                .build();
        });

        PaginationDto<CheckInAllStudentsResponse> responseData = PaginationDto.<CheckInAllStudentsResponse>builder()
            .data(resultPage.getContent())
            .totalData(resultPage.getTotalElements())
            .totalPage(resultPage.getTotalPages())
            .currentPage(resultPage.getNumber())
            .pageSize(resultPage.getSize())
            .build();

        return ApiResponse.<PaginationDto<CheckInAllStudentsResponse>>builder()
            .data(responseData)
            .message("T-SUCC-005")
            .statusCode(HttpStatus.OK.value())
            .status(HttpStatus.OK.name())
            .build();
    }
    
}
