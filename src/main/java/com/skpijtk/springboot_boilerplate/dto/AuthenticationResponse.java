package com.skpijtk.springboot_boilerplate.dto;

import com.skpijtk.springboot_boilerplate.model.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    
    private Long idUser;
    private String token;
    private String name;
    private Role role;
    
}
