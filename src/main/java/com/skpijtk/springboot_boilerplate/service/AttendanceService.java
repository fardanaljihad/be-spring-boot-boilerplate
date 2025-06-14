package com.skpijtk.springboot_boilerplate.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.skpijtk.springboot_boilerplate.dto.ApiResponse;
import com.skpijtk.springboot_boilerplate.dto.AttendanceDto;
import com.skpijtk.springboot_boilerplate.dto.CheckInAllStudentsResponse;
import com.skpijtk.springboot_boilerplate.dto.CheckInCheckOutResponse;
import com.skpijtk.springboot_boilerplate.dto.CheckInRequest;
import com.skpijtk.springboot_boilerplate.dto.CheckOutRequest;
import com.skpijtk.springboot_boilerplate.dto.PaginationDto;
import com.skpijtk.springboot_boilerplate.dto.ResumeCheckInResponse;
import com.skpijtk.springboot_boilerplate.exception.IllegalArgumentException;
import com.skpijtk.springboot_boilerplate.exception.ResourceNotFoundException;
import com.skpijtk.springboot_boilerplate.handler.WebSocketHandler;
import com.skpijtk.springboot_boilerplate.exception.BadRequestException;
import com.skpijtk.springboot_boilerplate.model.AppSetting;
import com.skpijtk.springboot_boilerplate.model.Attendance;
import com.skpijtk.springboot_boilerplate.model.CheckInStatus;
import com.skpijtk.springboot_boilerplate.model.Student;
import com.skpijtk.springboot_boilerplate.model.User;
import com.skpijtk.springboot_boilerplate.repository.AppSettingRepository;
import com.skpijtk.springboot_boilerplate.repository.AttendanceRepository;
import com.skpijtk.springboot_boilerplate.repository.AttendanceSpecification;
import com.skpijtk.springboot_boilerplate.repository.StudentRepository;
import com.skpijtk.springboot_boilerplate.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final AppSettingRepository appSettingRepository;
    private final WebSocketHandler webSocketHandler;

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

    public ApiResponse<PaginationDto<CheckInAllStudentsResponse>> getAllStudents(
        String studentName,
        LocalDate startDate,
        LocalDate endDate,
        int page,
        int size
    ) {
        
        Pageable pageable = PageRequest.of(page, size);

        Specification<Attendance> spec = AttendanceSpecification.filterBy(
            studentName, startDate, endDate
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

    public ApiResponse<PaginationDto<CheckInAllStudentsResponse>> getListAttendanceStudent(
        long studentId,
        LocalDate startDate,
        LocalDate endDate,
        int page,
        int size
    ) {

        studentRepository.findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Student data not found", "T-ERR-005"));
        
        Pageable pageable = PageRequest.of(page, size);

        Specification<Attendance> spec = AttendanceSpecification.filterBy(
            studentId, startDate, endDate
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
            .message("T-SUCC-004")
            .statusCode(HttpStatus.OK.value())
            .status(HttpStatus.OK.name())
            .build();
    }

    public ApiResponse<CheckInCheckOutResponse> studentCheckIn(CheckInRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Student data not found", "T-ERR-005"));

        Student student = studentRepository.findByUser(user)
            .orElseThrow(() -> new ResourceNotFoundException("Student data not found", "T-ERR-005"));

        LocalDate today = LocalDate.now();
        boolean alreadyCheckedIn = attendanceRepository.existsByStudentIdAndAttendanceDate(student.getId(), today);
        if (alreadyCheckedIn) {
            throw new BadRequestException("You have already checked in today.", "T-ERR-003");
        }

        if (request.getNotesCheckin() == "") {
            throw new BadRequestException("Check in failed because the note is empty", "T-ERR-009");
        }

        LocalDateTime now = LocalDateTime.now();
        AppSetting appSetting = appSettingRepository.findById(1)
            .orElseThrow(() -> new ResourceNotFoundException("App setting not found", "T-ERR-004"));

        LocalTime defaultCheckInTime = appSetting.getDefaultCheckInTime();
        boolean isLate = now.toLocalTime().isAfter(defaultCheckInTime);
        CheckInStatus status = isLate ? CheckInStatus.TERLAMBAT : CheckInStatus.TEPAT_WAKTU;

        Attendance attendance = Attendance.builder()
            .attendanceDate(today)
            .checkInTime(now)
            .checkInStatus(status)
            .checkInNotes(request.getNotesCheckin())
            .student(student)
            .build();

        Attendance savedAttendance = attendanceRepository.save(attendance);

        CheckInCheckOutResponse response = CheckInCheckOutResponse.builder()
            .attendanceId(savedAttendance.getId())
            .checkinTime(savedAttendance.getCheckInTime())
            .attendanceDate(savedAttendance.getAttendanceDate())
            .notesCheckin(savedAttendance.getCheckInNotes())
            .statusCheckin(savedAttendance.getCheckInStatus().name())
            .studentId(student.getId())
            .studentName(user.getName())
            .nim(student.getNim())
            .build();

        webSocketHandler.sendNotificationToAdmins("Mahasiswa " + user.getName() + " berhasil checkin");

        return ApiResponse.<CheckInCheckOutResponse>builder()
            .data(response)
            .message(isLate ? "T-WAR-001" : "T-SUCC-007: Checkin successfully")
            .status(HttpStatus.OK.name())
            .statusCode(HttpStatus.OK.value())
            .build();
    }

    public ApiResponse<CheckInCheckOutResponse> studentCheckOut(CheckOutRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Student data not found", "T-ERR-005"));

        Student student = studentRepository.findByUser(user)
            .orElseThrow(() -> new ResourceNotFoundException("Student data not found", "T-ERR-005"));

        LocalDate today = LocalDate.now();
        Attendance attendance = attendanceRepository.findByStudentIdAndAttendanceDate(student.getId(), today)
            .orElseThrow(() -> new ResourceNotFoundException("Attendance not found for today", "T-ERR-005"));

        if (request.getNotesCheckout() == "") {
            throw new BadRequestException("Check out failed because the note is empty", "T-ERR-009");
        }

        if (attendance.getCheckOutTime() != null) {
            throw new BadRequestException("You have already checked out today.", "T-ERR-010");
        }

        attendance.setCheckOutTime(LocalDateTime.now());
        attendance.setCheckOutNotes(request.getNotesCheckout());
        Attendance updated = attendanceRepository.save(attendance);

        CheckInCheckOutResponse response = CheckInCheckOutResponse.builder()
            .attendanceId(updated.getId())
            .checkinTime(updated.getCheckInTime())
            .checkOutTime(updated.getCheckOutTime())
            .attendanceDate(updated.getAttendanceDate())
            .notesCheckin(updated.getCheckInNotes())
            .notesCheckout(updated.getCheckOutNotes())
            .statusCheckin(updated.getCheckInStatus().name())
            .studentId(student.getId())
            .studentName(user.getName())
            .nim(student.getNim())
            .build();

        return ApiResponse.<CheckInCheckOutResponse>builder()
            .data(response)
            .message("T-SUCC-002") // Checkout success
            .status(HttpStatus.OK.name())
            .statusCode(HttpStatus.OK.value())
            .build();
    }

    
}
