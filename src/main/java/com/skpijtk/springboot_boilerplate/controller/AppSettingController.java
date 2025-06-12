package com.skpijtk.springboot_boilerplate.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skpijtk.springboot_boilerplate.dto.ApiResponse;
import com.skpijtk.springboot_boilerplate.dto.AppSettingRequest;
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
    public ResponseEntity<ApiResponse<AppSettingResponse>> getAppSetting() {
        return ResponseEntity.ok(appSettingService.getAppSetting());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/system-settings")
    public ResponseEntity<ApiResponse<AppSettingResponse>> updateAppSetting(@RequestBody AppSettingRequest request) {
        return ResponseEntity.ok(appSettingService.updateAppSetting(request));
    }

}
