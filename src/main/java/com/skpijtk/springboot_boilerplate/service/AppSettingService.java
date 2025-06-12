package com.skpijtk.springboot_boilerplate.service;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.skpijtk.springboot_boilerplate.dto.ApiResponse;
import com.skpijtk.springboot_boilerplate.dto.AppSettingRequest;
import com.skpijtk.springboot_boilerplate.dto.AppSettingResponse;
import com.skpijtk.springboot_boilerplate.exception.ResourceNotFoundException;
import com.skpijtk.springboot_boilerplate.model.AppSetting;
import com.skpijtk.springboot_boilerplate.repository.AppSettingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppSettingService {

    private final AppSettingRepository appSettingRepository;

    public ApiResponse<AppSettingResponse> getAppSetting() {
        AppSetting appSetting = appSettingRepository.findById(1)
            .orElseThrow(() -> new ResourceNotFoundException("App setting data not found", "T-ERR-005"));

        AppSettingResponse responseData = AppSettingResponse.builder()
            .defaultCheckInTime(appSetting.getDefaultCheckInTime())
            .defaultCheckOutTime(appSetting.getDefaultCheckOutTime())
            .checkInLateToleranceMinutes(appSetting.getCheckInLateToleranceMinutes())
            .build();

        return ApiResponse.<AppSettingResponse>builder()
            .data(responseData)
            .message("T-SUCC-004")
            .statusCode(HttpStatus.OK.value())
            .status(HttpStatus.OK.name())
            .build();
    }

    public ApiResponse<AppSettingResponse> updateAppSetting(AppSettingRequest request) {
        AppSetting appSetting = appSettingRepository.findById(1)
            .orElseThrow(() -> new ResourceNotFoundException("App setting data not found", "T-ERR-005"));

        appSetting.setDefaultCheckInTime(request.getDefaultCheckInTime());
        appSetting.setDefaultCheckOutTime(request.getDefaultCheckOutTime());
        appSetting.setCheckInLateToleranceMinutes(request.getCheckInLateToleranceMinutes());
        appSetting.setUpdatedAt(LocalDateTime.now());

        appSettingRepository.save(appSetting);

        AppSettingResponse responseData = AppSettingResponse.builder()
            .defaultCheckInTime(appSetting.getDefaultCheckInTime())
            .defaultCheckOutTime(appSetting.getDefaultCheckOutTime())
            .checkInLateToleranceMinutes(appSetting.getCheckInLateToleranceMinutes())
            .build();

        return ApiResponse.<AppSettingResponse>builder()
            .data(responseData)
            .message("T-SUCC-008: Data successfully updated")
            .statusCode(HttpStatus.OK.value())
            .status(HttpStatus.OK.name())
            .build();
    }
    
}
