package com.skpijtk.springboot_boilerplate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterStudentRequest {

    @NotBlank(message = "Student name cannot be blank")
    @Size(min = 1, max = 100, message = "Nama harus antara 1 hingga 50 karakter")
    @Pattern(
        regexp = "^[A-Za-z]+(\\s[A-Za-z]+)+$",
        message = "Nama harus terdiri dari minimal dua kata, hanya huruf dan tidak boleh ada angka, karakter spesial, atau spasi di akhir"
    )
    private String studentName;

    @NotBlank(message = "NIM cannot be blank")
    @Pattern(
        regexp = "^\\d{9}$",
        message = "NIM harus terdiri dari 9 digit angka"
    )
    private String nim;
    
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    @Size(min = 1, max = 50)
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,12}$",
        message = "Password must have a combination of numbers and letters, between 6 and 12 characters, and no spaces or special characters"
    )
    private String password;

}
