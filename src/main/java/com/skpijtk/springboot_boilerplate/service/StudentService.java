package com.skpijtk.springboot_boilerplate.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.skpijtk.springboot_boilerplate.dto.ApiResponse;
import com.skpijtk.springboot_boilerplate.dto.AttendanceDto;
import com.skpijtk.springboot_boilerplate.dto.ProfileStudentResponse;
import com.skpijtk.springboot_boilerplate.dto.RegisterStudentRequest;
import com.skpijtk.springboot_boilerplate.dto.StudentResponse;
import com.skpijtk.springboot_boilerplate.dto.UpdateStudentRequest;
import com.skpijtk.springboot_boilerplate.exception.ResourceNotFoundException;
import com.skpijtk.springboot_boilerplate.exception.IllegalArgumentException;
import com.skpijtk.springboot_boilerplate.model.Attendance;
import com.skpijtk.springboot_boilerplate.model.CheckInStatus;
import com.skpijtk.springboot_boilerplate.model.Role;
import com.skpijtk.springboot_boilerplate.model.Student;
import com.skpijtk.springboot_boilerplate.model.User;
import com.skpijtk.springboot_boilerplate.repository.AttendanceRepository;
import com.skpijtk.springboot_boilerplate.repository.StudentRepository;
import com.skpijtk.springboot_boilerplate.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;
    private final PasswordEncoder passwordEncoder;

    public ApiResponse<StudentResponse> registerStudent(RegisterStudentRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Data failed to saved", "T-ERR-010");
        }
        
        if (studentRepository.existsByNim(request.getNim())) {
            throw new IllegalArgumentException("Data failed to saved", "T-ERR-010");
        }
        
        User user = User.builder()
            .name(request.getStudentName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.MAHASISWA)
            .build();

        Student student = Student.builder()
            .nim(request.getNim())
            .user(user)
            .build();

        user.setStudent(student);
        User savedUser = userRepository.save(user);
        Student savedStudent = savedUser.getStudent();

        return ApiResponse.<StudentResponse>builder()
            .data(toStudentResponse(savedStudent))
            .message("T-SUCC-008: Data successfully saved")
            .statusCode(HttpStatus.CREATED.value())
            .status(HttpStatus.CREATED.name())
            .build();
    }

    public ApiResponse<StudentResponse> updateStudent(long studentId, UpdateStudentRequest request) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found", "T-ERR-005"));

        User user = student.getUser();

        if (!user.getEmail().equals(request.getEmail()) &&
            userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use", "T-ERR-010");
        }

        if (!student.getNim().equals(request.getNim()) &&
            studentRepository.existsByNim(request.getNim())) {
            throw new IllegalArgumentException("NIM already in use", "T-ERR-010");
        }

        user.setName(request.getStudentName());
        user.setEmail(request.getEmail());
        student.setNim(request.getNim());
        student.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        return ApiResponse.<StudentResponse>builder()
            .data(toStudentResponse(student))
            .message("T-SUCC-008: Data successfully updated")
            .statusCode(HttpStatus.OK.value())
            .status(HttpStatus.OK.name())
            .build();
    }
    
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

        return ApiResponse.<StudentResponse>builder()
            .data(toStudentResponse(student))
            .message("T-SUCC-004")
            .statusCode(HttpStatus.OK.value())
            .status(HttpStatus.OK.name())
            .build();
    }

    public static StudentResponse toStudentResponse(Student student) {
        User user = student.getUser();

        return StudentResponse.builder()
            .studentId(student.getId())
            .userId(user.getId())
            .studentName(user.getName())
            .nim(student.getNim())
            .email(user.getEmail())
            .attendanceData(toAttendanceDtoList(student.getAttendances()))
            .build();
    }

    public static List<AttendanceDto> toAttendanceDtoList(List<Attendance> attendances) {
        return attendances.stream().map(att -> AttendanceDto.builder()
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
    }

    public ApiResponse<Object> deleteStudentById(Long studentId) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found", "T-ERR-005"));

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("student_id", student.getId());
        responseData.put("student_name", student.getUser().getName());
        responseData.put("nim", student.getNim());

        userRepository.delete(student.getUser());

        ApiResponse<Object> response = ApiResponse.<Object>builder()
            .data(responseData)
            .message("T-SUCC-006: Student " + student.getUser().getName() + " successfully deleted")
            .statusCode(HttpStatus.OK.value())
            .status(HttpStatus.OK.name())
            .build();

        return response;
    }

    public ApiResponse<ProfileStudentResponse> getProfileStudent(LocalDate startDate, LocalDate endDate, int page, int size) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Data failed to display", "T-ERR-006"));

        Student student = studentRepository.findByUser(user)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found", "T-ERR-005"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("attendanceDate").descending());

        Page<Attendance> attendancePage = attendanceRepository
            .findByStudentIdAndAttendanceDateBetween(student.getId(), startDate, endDate, pageable);

        List<AttendanceDto> attendanceData = toAttendanceDtoList(student.getAttendances());

        ProfileStudentResponse dataResponse = ProfileStudentResponse.builder()
            .studentId(student.getId())
            .studentName(user.getName())
            .nim(student.getNim())
            .attendanceData(attendanceData)
            .totalData((int) attendancePage.getTotalElements())
            .totalPage(attendancePage.getTotalPages())
            .currentPage(attendancePage.getNumber())
            .pageSize(attendancePage.getSize())
            .build();

        return ApiResponse.<ProfileStudentResponse>builder()
            .data(dataResponse)
            .message("T-SUCC-005")
            .statusCode(HttpStatus.OK.value())
            .status(HttpStatus.OK.name())
            .build();
    }

}
