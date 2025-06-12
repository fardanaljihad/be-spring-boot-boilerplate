package com.skpijtk.springboot_boilerplate.dto;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppSettingResponse {

    private LocalTime defaultCheckInTime;
    private LocalTime defaultCheckOutTime;
    private int checkInLateToleranceMinutes;
    
}
