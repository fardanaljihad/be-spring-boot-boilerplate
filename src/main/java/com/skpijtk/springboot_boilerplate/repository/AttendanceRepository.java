package com.skpijtk.springboot_boilerplate.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skpijtk.springboot_boilerplate.model.Attendance;
import com.skpijtk.springboot_boilerplate.model.CheckInStatus;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    long countByAttendanceDate(LocalDate date);
    
    long countByAttendanceDateAndCheckInStatus(LocalDate date, CheckInStatus status);
    
}
