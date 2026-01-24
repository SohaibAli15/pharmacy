/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.pharmacy.dto.AttendanceDto;
import com.pharmacy.entity.Attendance;
import com.pharmacy.entity.Employee;
import com.pharmacy.repository.AttendanceRepository;
import com.pharmacy.repository.EmployeeRepository;
import com.pharmacy.service.AttendanceManagementService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceManagementServiceImpl implements AttendanceManagementService {
  private final AttendanceRepository attendanceRepository;
  private final EmployeeRepository employeeRepository;

  @Override
  public AttendanceDto create(AttendanceDto dto) {
    Attendance entity = new Attendance();
    Employee employee = employeeRepository.findById(dto.getEmployeeId()).orElseThrow();
    entity.setEmployee(employee);
    entity.setDate(dto.getDate());
    entity.setStatus(dto.getStatus());
    Attendance saved = attendanceRepository.save(entity);
    AttendanceDto result = new AttendanceDto();
    BeanUtils.copyProperties(saved, result);
    result.setEmployeeId(employee.getId());
    result.setEmployeeName(employee.getName());
    return result;
  }

  @Override
  public AttendanceDto update(Long id, AttendanceDto dto) {
    Attendance entity = attendanceRepository.findById(id).orElseThrow();
    entity.setStatus(dto.getStatus());
    Attendance saved = attendanceRepository.save(entity);
    AttendanceDto result = new AttendanceDto();
    BeanUtils.copyProperties(saved, result);
    result.setEmployeeId(entity.getEmployee().getId());
    result.setEmployeeName(entity.getEmployee().getName());
    return result;
  }

  @Override
  public void delete(Long id) {
    attendanceRepository.deleteById(id);
  }

  @Override
  public AttendanceDto getById(Long id) {
    Attendance entity = attendanceRepository.findById(id).orElseThrow();
    AttendanceDto dto = new AttendanceDto();
    BeanUtils.copyProperties(entity, dto);
    dto.setEmployeeId(entity.getEmployee().getId());
    dto.setEmployeeName(entity.getEmployee().getName());
    return dto;
  }

  @Override
  public List<AttendanceDto> getAll() {
    return attendanceRepository.findAll().stream()
        .map(
            entity -> {
              AttendanceDto dto = new AttendanceDto();
              BeanUtils.copyProperties(entity, dto);
              dto.setEmployeeId(entity.getEmployee().getId());
              dto.setEmployeeName(entity.getEmployee().getName());
              return dto;
            })
        .collect(Collectors.toList());
  }
}
