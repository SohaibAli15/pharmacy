/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.pharmacy.dto.PayrollDto;
import com.pharmacy.entity.Employee;
import com.pharmacy.entity.Payroll;
import com.pharmacy.repository.EmployeeRepository;
import com.pharmacy.repository.PayrollRepository;
import com.pharmacy.service.PayrollManagementService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayrollManagementServiceImpl implements PayrollManagementService {
  private final PayrollRepository payrollRepository;
  private final EmployeeRepository employeeRepository;

  @Override
  public PayrollDto create(PayrollDto dto) {
    Payroll entity = new Payroll();
    Employee employee = employeeRepository.findById(dto.getEmployeeId()).orElseThrow();
    entity.setEmployee(employee);
    entity.setPayrollMonth(dto.getPayrollMonth());
    entity.setAmount(dto.getAmount());
    entity.setStatus(dto.getStatus());
    Payroll saved = payrollRepository.save(entity);
    PayrollDto result = new PayrollDto();
    BeanUtils.copyProperties(saved, result);
    result.setEmployeeId(employee.getId());
    result.setEmployeeName(employee.getName());
    return result;
  }

  @Override
  public PayrollDto update(Long id, PayrollDto dto) {
    Payroll entity = payrollRepository.findById(id).orElseThrow();
    entity.setAmount(dto.getAmount());
    entity.setStatus(dto.getStatus());
    Payroll saved = payrollRepository.save(entity);
    PayrollDto result = new PayrollDto();
    BeanUtils.copyProperties(saved, result);
    result.setEmployeeId(entity.getEmployee().getId());
    result.setEmployeeName(entity.getEmployee().getName());
    return result;
  }

  @Override
  public void delete(Long id) {
    payrollRepository.deleteById(id);
  }

  @Override
  public PayrollDto getById(Long id) {
    Payroll entity = payrollRepository.findById(id).orElseThrow();
    PayrollDto dto = new PayrollDto();
    BeanUtils.copyProperties(entity, dto);
    dto.setEmployeeId(entity.getEmployee().getId());
    dto.setEmployeeName(entity.getEmployee().getName());
    return dto;
  }

  @Override
  public List<PayrollDto> getAll() {
    return payrollRepository.findAll().stream()
        .map(
            entity -> {
              PayrollDto dto = new PayrollDto();
              BeanUtils.copyProperties(entity, dto);
              dto.setEmployeeId(entity.getEmployee().getId());
              dto.setEmployeeName(entity.getEmployee().getName());
              return dto;
            })
        .collect(Collectors.toList());
  }
}
