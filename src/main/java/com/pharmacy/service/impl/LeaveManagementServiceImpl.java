/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.pharmacy.dto.LeaveRequestDto;
import com.pharmacy.entity.Employee;
import com.pharmacy.entity.LeaveRequest;
import com.pharmacy.repository.EmployeeRepository;
import com.pharmacy.repository.LeaveRequestRepository;
import com.pharmacy.service.LeaveManagementService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeaveManagementServiceImpl implements LeaveManagementService {
  private final LeaveRequestRepository leaveRequestRepository;
  private final EmployeeRepository employeeRepository;

  @Override
  public LeaveRequestDto create(LeaveRequestDto dto) {
    LeaveRequest entity = new LeaveRequest();
    Employee employee = employeeRepository.findById(dto.getEmployeeId()).orElseThrow();
    entity.setEmployee(employee);
    entity.setLeaveType(dto.getLeaveType());
    entity.setStartDate(dto.getStartDate());
    entity.setEndDate(dto.getEndDate());
    entity.setStatus(dto.getStatus());
    LeaveRequest saved = leaveRequestRepository.save(entity);
    LeaveRequestDto result = new LeaveRequestDto();
    BeanUtils.copyProperties(saved, result);
    result.setEmployeeId(employee.getId());
    result.setEmployeeName(employee.getName());
    return result;
  }

  @Override
  public LeaveRequestDto update(Long id, LeaveRequestDto dto) {
    LeaveRequest entity = leaveRequestRepository.findById(id).orElseThrow();
    entity.setLeaveType(dto.getLeaveType());
    entity.setStartDate(dto.getStartDate());
    entity.setEndDate(dto.getEndDate());
    entity.setStatus(dto.getStatus());
    LeaveRequest saved = leaveRequestRepository.save(entity);
    LeaveRequestDto result = new LeaveRequestDto();
    BeanUtils.copyProperties(saved, result);
    result.setEmployeeId(entity.getEmployee().getId());
    result.setEmployeeName(entity.getEmployee().getName());
    return result;
  }

  @Override
  public void delete(Long id) {
    leaveRequestRepository.deleteById(id);
  }

  @Override
  public LeaveRequestDto getById(Long id) {
    LeaveRequest entity = leaveRequestRepository.findById(id).orElseThrow();
    LeaveRequestDto dto = new LeaveRequestDto();
    BeanUtils.copyProperties(entity, dto);
    dto.setEmployeeId(entity.getEmployee().getId());
    dto.setEmployeeName(entity.getEmployee().getName());
    return dto;
  }

  @Override
  public List<LeaveRequestDto> getAll() {
    return leaveRequestRepository.findAll().stream()
        .map(
            entity -> {
              LeaveRequestDto dto = new LeaveRequestDto();
              BeanUtils.copyProperties(entity, dto);
              dto.setEmployeeId(entity.getEmployee().getId());
              dto.setEmployeeName(entity.getEmployee().getName());
              return dto;
            })
        .collect(Collectors.toList());
  }
}
