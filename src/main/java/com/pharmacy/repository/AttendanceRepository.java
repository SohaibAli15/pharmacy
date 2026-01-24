/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pharmacy.entity.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {}
