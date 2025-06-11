package com.skpijtk.springboot_boilerplate.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.skpijtk.springboot_boilerplate.dto.ApiResponse;
import com.skpijtk.springboot_boilerplate.exception.ResourceNotFoundException;
import com.skpijtk.springboot_boilerplate.model.User;
import com.skpijtk.springboot_boilerplate.repository.StudentRepository;
import com.skpijtk.springboot_boilerplate.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;

    public ApiResponse<Object> getProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Data failed to display", "T-ERR-006"));

        Locale locale = new Locale("id", "ID");
        String time = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", locale));

        Map<String, Object> profileData = new HashMap<>();
        profileData.put("name", user.getName());
        profileData.put("role", user.getRole());
        profileData.put("time", time);

        ApiResponse<Object> response = ApiResponse.builder()
            .data(profileData)
            .message("T-SUCC-005")
            .statusCode(HttpStatus.OK.value())
            .status(HttpStatus.OK.name())
            .build();

        return response;
    }

    public ApiResponse<Object> getTotalMahasiswa() {
        long totalMahasiswa = studentRepository.count();

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("total_mahasiswa", totalMahasiswa);

        ApiResponse<Object> response = ApiResponse.builder()
            .data(responseData)
            .message("T-SUCC-005")
            .statusCode(HttpStatus.OK.value())
            .status(HttpStatus.OK.name())
            .build();

        return response;
    }
    
}
