package com.skpijtk.springboot_boilerplate.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skpijtk.springboot_boilerplate.dto.ApiResponse;
import com.skpijtk.springboot_boilerplate.dto.AppSettingResponse;
import com.skpijtk.springboot_boilerplate.service.AppSettingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AppSettingController {

    private final AppSettingService appSettingService;
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/system-settings")
    public ResponseEntity<ApiResponse<AppSettingResponse>> getStudent() {
        return ResponseEntity.ok(appSettingService.getAppSetting());
    }

}
