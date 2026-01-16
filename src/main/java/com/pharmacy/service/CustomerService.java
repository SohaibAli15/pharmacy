/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmacy.dto.CustomerDto;
import com.pharmacy.entity.Customer;
import com.pharmacy.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

  private final CustomerRepository customerRepository;

  @Transactional
  public CustomerDto createCustomer(CustomerDto customerDto) {
    if (customerRepository.existsByCustomerCode(customerDto.getCustomerCode())) {
      throw new RuntimeException("Customer code already exists");
    }
    if (customerRepository.existsByEmail(customerDto.getEmail())) {
      throw new RuntimeException("Email already exists");
    }

    Customer customer = mapToEntity(customerDto);
    customer.setCreatedAt(LocalDateTime.now());
    customer.setUpdatedAt(LocalDateTime.now());

    Customer savedCustomer = customerRepository.save(customer);
    return mapToDto(savedCustomer);
  }

  @Transactional
  public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
    Customer customer =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found"));

    if (!customer.getCustomerCode().equals(customerDto.getCustomerCode())
        && customerRepository.existsByCustomerCode(customerDto.getCustomerCode())) {
      throw new RuntimeException("Customer code already exists");
    }

    if (!customer.getEmail().equals(customerDto.getEmail())
        && customerRepository.existsByEmail(customerDto.getEmail())) {
      throw new RuntimeException("Email already exists");
    }

    updateEntityFromDto(customer, customerDto);
    customer.setUpdatedAt(LocalDateTime.now());

    Customer updatedCustomer = customerRepository.save(customer);
    return mapToDto(updatedCustomer);
  }

  @Transactional(readOnly = true)
  public CustomerDto getCustomerById(Long id) {
    Customer customer =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
    return mapToDto(customer);
  }

  @Transactional(readOnly = true)
  public CustomerDto getCustomerByCode(String customerCode) {
    Customer customer =
        customerRepository
            .findByCustomerCode(customerCode)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
    return mapToDto(customer);
  }

  @Transactional(readOnly = true)
  public List<CustomerDto> getAllCustomers() {
    return customerRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<CustomerDto> searchCustomers(String searchTerm) {
    return customerRepository
        .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(searchTerm, searchTerm)
        .stream()
        .map(this::mapToDto)
        .collect(Collectors.toList());
  }

  @Transactional
  public void deleteCustomer(Long id) {
    if (!customerRepository.existsById(id)) {
      throw new RuntimeException("Customer not found");
    }
    customerRepository.deleteById(id);
  }

  private CustomerDto mapToDto(Customer customer) {
    CustomerDto dto = new CustomerDto();
    dto.setId(customer.getId());
    dto.setCustomerCode(customer.getCustomerCode());
    dto.setFirstName(customer.getFirstName());
    dto.setLastName(customer.getLastName());
    dto.setEmail(customer.getEmail());
    dto.setPhone(customer.getPhone());
    dto.setAlternatePhone(customer.getAlternatePhone());
    dto.setDateOfBirth(customer.getDateOfBirth());
    dto.setGender(customer.getGender());
    dto.setAddress(customer.getAddress());
    dto.setCity(customer.getCity());
    dto.setState(customer.getState());
    dto.setCountry(customer.getCountry());
    dto.setZipCode(customer.getZipCode());
    dto.setInsuranceProvider(customer.getInsuranceProvider());
    dto.setInsuranceNumber(customer.getInsuranceNumber());
    dto.setAllergies(customer.getAllergies());
    dto.setMedicalConditions(customer.getMedicalConditions());
    dto.setType(customer.getType());
    dto.setStatus(customer.getStatus());
    dto.setNotes(customer.getNotes());
    dto.setCreatedAt(customer.getCreatedAt());
    dto.setUpdatedAt(customer.getUpdatedAt());
    return dto;
  }

  private Customer mapToEntity(CustomerDto dto) {
    Customer customer = new Customer();
    customer.setCustomerCode(dto.getCustomerCode());
    customer.setFirstName(dto.getFirstName());
    customer.setLastName(dto.getLastName());
    customer.setEmail(dto.getEmail());
    customer.setPhone(dto.getPhone());
    customer.setAlternatePhone(dto.getAlternatePhone());
    customer.setDateOfBirth(dto.getDateOfBirth());
    customer.setGender(dto.getGender());
    customer.setAddress(dto.getAddress());
    customer.setCity(dto.getCity());
    customer.setState(dto.getState());
    customer.setCountry(dto.getCountry());
    customer.setZipCode(dto.getZipCode());
    customer.setInsuranceProvider(dto.getInsuranceProvider());
    customer.setInsuranceNumber(dto.getInsuranceNumber());
    customer.setAllergies(dto.getAllergies());
    customer.setMedicalConditions(dto.getMedicalConditions());
    customer.setType(dto.getType());
    customer.setStatus(dto.getStatus());
    customer.setNotes(dto.getNotes());
    return customer;
  }

  private void updateEntityFromDto(Customer customer, CustomerDto dto) {
    customer.setCustomerCode(dto.getCustomerCode());
    customer.setFirstName(dto.getFirstName());
    customer.setLastName(dto.getLastName());
    customer.setEmail(dto.getEmail());
    customer.setPhone(dto.getPhone());
    customer.setAlternatePhone(dto.getAlternatePhone());
    customer.setDateOfBirth(dto.getDateOfBirth());
    customer.setGender(dto.getGender());
    customer.setAddress(dto.getAddress());
    customer.setCity(dto.getCity());
    customer.setState(dto.getState());
    customer.setCountry(dto.getCountry());
    customer.setZipCode(dto.getZipCode());
    customer.setInsuranceProvider(dto.getInsuranceProvider());
    customer.setInsuranceNumber(dto.getInsuranceNumber());
    customer.setAllergies(dto.getAllergies());
    customer.setMedicalConditions(dto.getMedicalConditions());
    customer.setType(dto.getType());
    customer.setStatus(dto.getStatus());
    customer.setNotes(dto.getNotes());
  }
}
