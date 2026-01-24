/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.pharmacy.dto.EmployeeDto;
import com.pharmacy.entity.Employee;
import com.pharmacy.repository.EmployeeRepository;
import com.pharmacy.service.EmployeeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
  private final EmployeeRepository employeeRepository;

  @Override
  public EmployeeDto create(EmployeeDto dto) {
    Employee entity = new Employee();
    BeanUtils.copyProperties(dto, entity);
    Employee saved = employeeRepository.save(entity);
    EmployeeDto result = new EmployeeDto();
    BeanUtils.copyProperties(saved, result);
    return result;
  }

  @Override
  public EmployeeDto update(Long id, EmployeeDto dto) {
    Employee entity = employeeRepository.findById(id).orElseThrow();
    BeanUtils.copyProperties(dto, entity, "id", "employeeId");
    Employee saved = employeeRepository.save(entity);
    EmployeeDto result = new EmployeeDto();
    BeanUtils.copyProperties(saved, result);
    return result;
  }

  @Override
  public void delete(Long id) {
    employeeRepository.deleteById(id);
  }

  @Override
  public EmployeeDto getById(Long id) {
    Employee entity = employeeRepository.findById(id).orElseThrow();
    EmployeeDto dto = new EmployeeDto();
    BeanUtils.copyProperties(entity, dto);
    return dto;
  }

  @Override
  public EmployeeDto getByEmployeeId(String employeeId) {
    Employee entity = employeeRepository.findByEmployeeId(employeeId);
    EmployeeDto dto = new EmployeeDto();
    BeanUtils.copyProperties(entity, dto);
    return dto;
  }

  @Override
  public List<EmployeeDto> getAll() {
    return employeeRepository.findAll().stream()
        .map(
            entity -> {
              EmployeeDto dto = new EmployeeDto();
              BeanUtils.copyProperties(entity, dto);
              return dto;
            })
        .collect(Collectors.toList());
  }
}
