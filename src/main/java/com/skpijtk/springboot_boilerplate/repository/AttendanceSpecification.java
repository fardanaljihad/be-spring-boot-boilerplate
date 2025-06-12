package com.skpijtk.springboot_boilerplate.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.skpijtk.springboot_boilerplate.model.Attendance;
import com.skpijtk.springboot_boilerplate.model.CheckInStatus;
import com.skpijtk.springboot_boilerplate.model.Student;
import com.skpijtk.springboot_boilerplate.model.User;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

public class AttendanceSpecification {
    
    public static Specification<Attendance> filterBy(
        String studentName, 
        LocalDate startDate, 
        LocalDate endDate,
        String sortBy,
        String sortDir
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<Attendance, Student> studentJoin = root.join("student");
            Join<Student, User> userJoin = studentJoin.join("user");

            if (studentName != null && !studentName.isBlank()) {
                predicates.add(cb.like(
                    cb.lower(userJoin.get("name")),
                    "%" + studentName.toLowerCase() + "%"
                ));
            }

            if (startDate != null && endDate == null) {
                predicates.add(cb.equal(root.get("attendanceDate"), startDate));
            }

            if (startDate != null && endDate != null) {
                predicates.add(cb.between(root.get("attendanceDate"), startDate, endDate));
            }

            if ("status".equalsIgnoreCase(sortBy)) {
                System.out.println(sortBy);
                Expression<Integer> statusOrder = cb.<Integer>selectCase()
                    .when(cb.equal(root.get("checkInStatus"), CheckInStatus.TEPAT_WAKTU), 1)
                    .when(cb.equal(root.get("checkInStatus"), CheckInStatus.TERLAMBAT), 2)
                    .when(cb.isNull(root.get("checkOutTime")), 3)
                    .otherwise(4);

                query.orderBy("desc".equalsIgnoreCase(sortDir) ? cb.desc(statusOrder) : cb.asc(statusOrder));
            } else {
                Path<?> sortPath;
                switch (sortBy) {
                    case "nim" -> sortPath = studentJoin.get("nim");
                    case "studentName" -> sortPath = userJoin.get("name");
                    case "attendanceDate" -> sortPath = root.get("attendanceDate");
                    default -> sortPath = studentJoin.get("nim");
                }

                query.orderBy("desc".equalsIgnoreCase(sortDir) ? cb.desc(sortPath) : cb.asc(sortPath));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Attendance> filterBy(
        String studentName, 
        LocalDate startDate, 
        LocalDate endDate
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<Attendance, Student> studentJoin = root.join("student");
            Join<Student, User> userJoin = studentJoin.join("user");

            if (studentName != null && !studentName.isBlank()) {
                predicates.add(cb.like(
                    cb.lower(userJoin.get("name")),
                    "%" + studentName.toLowerCase() + "%"
                ));
            }

            if (startDate != null && endDate == null) {
                predicates.add(cb.equal(root.get("attendanceDate"), startDate));
            }

            if (startDate != null && endDate != null) {
                predicates.add(cb.between(root.get("attendanceDate"), startDate, endDate));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
