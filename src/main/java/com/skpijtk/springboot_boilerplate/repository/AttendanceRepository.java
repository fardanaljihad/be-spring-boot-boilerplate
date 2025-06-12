package com.skpijtk.springboot_boilerplate.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.skpijtk.springboot_boilerplate.model.Attendance;
import com.skpijtk.springboot_boilerplate.model.CheckInStatus;

public interface AttendanceRepository extends JpaRepository<Attendance, Long>, JpaSpecificationExecutor<Attendance> {

    long countByAttendanceDate(LocalDate date);
    
    long countByAttendanceDateAndCheckInStatus(LocalDate date, CheckInStatus status);

    Page<Attendance> findByStudentIdAndAttendanceDateBetween(
        Long studentId, LocalDate startDate, LocalDate endDate, Pageable pageable
    );
    
}
