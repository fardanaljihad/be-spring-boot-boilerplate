package com.skpijtk.springboot_boilerplate.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.skpijtk.springboot_boilerplate.dto.ApiResponse;
import com.skpijtk.springboot_boilerplate.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    
    public ApiResponse<Object> getTotalMahasiswa() {
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

}
