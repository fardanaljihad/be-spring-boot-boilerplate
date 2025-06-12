package com.skpijtk.springboot_boilerplate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeCheckInResponse {
    
    private long totalMahasiswa;
    private long totalCheckIn;
    private long totalBelumCheckIn;
    private long totalTelatCheckIn;

}
