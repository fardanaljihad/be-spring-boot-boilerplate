package com.skpijtk.springboot_boilerplate.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skpijtk.springboot_boilerplate.model.AppSetting;

public interface AppSettingRepository extends JpaRepository<AppSetting, Integer> {
    
}
