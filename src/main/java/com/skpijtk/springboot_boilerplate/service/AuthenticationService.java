package com.skpijtk.springboot_boilerplate.service;

import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.skpijtk.springboot_boilerplate.dto.AuthenticationRequest;
import com.skpijtk.springboot_boilerplate.dto.AuthenticationResponse;
import com.skpijtk.springboot_boilerplate.dto.RegisterRequest;
import com.skpijtk.springboot_boilerplate.dto.RegisterResponse;
import com.skpijtk.springboot_boilerplate.model.Role;
import com.skpijtk.springboot_boilerplate.model.User;
import com.skpijtk.springboot_boilerplate.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public RegisterResponse register(RegisterRequest request) {
        var user = User.builder()
            .name(request.getName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.ADMIN)
            .build();

        userRepository.save(user);
        return RegisterResponse.builder()
            .data(Map.of(
                "email", user.getEmail()
            ))
            .message(List.of("T-SUCC-001"))
            .statusCode(200)
            .status("OK")
            .build();
    }
    
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        var user = userRepository.findByEmail(request.getEmail())
            .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();
    }

}
