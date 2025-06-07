package com.skpijtk.springboot_boilerplate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    
    private T data;
    private String message;
    private int statusCode;
    private String status;

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(data, message, 200, "OK");
    }

    public static <T> ApiResponse<T> error(String message, int statusCode, String status) {
        return new ApiResponse<>(null, message, statusCode, status);
    }
}
