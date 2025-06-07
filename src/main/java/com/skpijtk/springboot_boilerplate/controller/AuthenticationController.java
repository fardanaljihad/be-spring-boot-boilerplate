package com.skpijtk.springboot_boilerplate.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skpijtk.springboot_boilerplate.dto.ApiResponse;
import com.skpijtk.springboot_boilerplate.dto.AuthenticationRequest;
import com.skpijtk.springboot_boilerplate.dto.AuthenticationResponse;
import com.skpijtk.springboot_boilerplate.dto.RegisterRequest;
import com.skpijtk.springboot_boilerplate.dto.RegisterResponse;
import com.skpijtk.springboot_boilerplate.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));

    }

}
