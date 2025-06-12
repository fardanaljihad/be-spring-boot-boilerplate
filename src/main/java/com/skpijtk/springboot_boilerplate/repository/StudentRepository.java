package com.skpijtk.springboot_boilerplate.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skpijtk.springboot_boilerplate.model.Student;
import com.skpijtk.springboot_boilerplate.model.User;

public interface StudentRepository extends JpaRepository<Student, Long> {
    
    boolean existsByNim(String nim);

    Optional<Student> findByUser(User user);
    
}
