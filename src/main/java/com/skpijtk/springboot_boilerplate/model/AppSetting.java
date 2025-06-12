package com.skpijtk.springboot_boilerplate.model;

import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appsettings")
public class AppSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "default_check_in_time", nullable = false)
    private LocalTime defaultCheckInTime;

    @Column(name = "default_check_out_time", nullable = false)
    private LocalTime defaultCheckOutTime;

    @Column(name = "check_in_late_tolerance_minutes", nullable = false)
    private int checkInLateToleranceMinutes;

    @Column(name = "check_out_late_tolerance_minutes", nullable = false)
    private int checkOutLateToleranceMinutes;

    @Builder.Default
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
}
