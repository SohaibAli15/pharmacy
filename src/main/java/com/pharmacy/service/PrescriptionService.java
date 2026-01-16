/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmacy.dto.PrescriptionDto;
import com.pharmacy.dto.PrescriptionItemDto;
import com.pharmacy.entity.*;
import com.pharmacy.repository.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

  private final PrescriptionRepository prescriptionRepository;
  private final UserRepository userRepository;
  private final MedicineRepository medicineRepository;

  @Transactional
  public PrescriptionDto createPrescription(PrescriptionDto dto) {
    User customer =
        userRepository
            .findById(dto.getCustomerId())
            .orElseThrow(() -> new RuntimeException("Customer not found"));

    User pharmacist = null;
    if (dto.getPharmacistId() != null) {
      pharmacist =
          userRepository
              .findById(dto.getPharmacistId())
              .orElseThrow(() -> new RuntimeException("Pharmacist not found"));
    }

    Prescription prescription = new Prescription();
    prescription.setCustomer(customer);
    prescription.setPharmacist(pharmacist);
    prescription.setIssueDate(dto.getIssueDate());
    prescription.setStatus(dto.getStatus() != null ? dto.getStatus() : "PENDING");

    Prescription savedPrescription = prescriptionRepository.save(prescription);

    // Create prescription items
    List<PrescriptionItem> items = new ArrayList<>();
    for (PrescriptionItemDto itemDto : dto.getItems()) {
      Medicine medicine =
          medicineRepository
              .findById(itemDto.getMedicineId())
              .orElseThrow(
                  () -> new RuntimeException("Medicine not found: " + itemDto.getMedicineId()));

      PrescriptionItem item = new PrescriptionItem();
      item.setPrescription(savedPrescription);
      item.setMedicine(medicine);
      item.setQuantity(itemDto.getQuantity());
      item.setDosageInstructions(itemDto.getDosageInstructions());
      items.add(item);
    }

    savedPrescription.setItems(items);
    Prescription completedPrescription = prescriptionRepository.save(savedPrescription);

    return mapToDto(completedPrescription);
  }

  @Transactional(readOnly = true)
  public PrescriptionDto getPrescriptionById(Long id) {
    Prescription prescription =
        prescriptionRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Prescription not found"));
    return mapToDto(prescription);
  }

  @Transactional(readOnly = true)
  public List<PrescriptionDto> getAllPrescriptions() {
    return prescriptionRepository.findAll().stream()
        .map(this::mapToDto)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<PrescriptionDto> getPrescriptionsByCustomer(Long customerId) {
    User customer =
        userRepository
            .findById(customerId)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
    return prescriptionRepository.findByCustomer(customer).stream()
        .map(this::mapToDto)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<PrescriptionDto> getPrescriptionsByStatus(String status) {
    return prescriptionRepository.findByStatus(status).stream()
        .map(this::mapToDto)
        .collect(Collectors.toList());
  }

  @Transactional
  public PrescriptionDto updatePrescriptionStatus(Long id, String status) {
    Prescription prescription =
        prescriptionRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Prescription not found"));
    prescription.setStatus(status);
    Prescription updated = prescriptionRepository.save(prescription);
    return mapToDto(updated);
  }

  @Transactional
  public void deletePrescription(Long id) {
    prescriptionRepository.deleteById(id);
  }

  private PrescriptionDto mapToDto(Prescription prescription) {
    PrescriptionDto dto = new PrescriptionDto();
    dto.setId(prescription.getId());
    dto.setCustomerId(prescription.getCustomer().getId());
    dto.setCustomerName(prescription.getCustomer().getUsername());

    if (prescription.getPharmacist() != null) {
      dto.setPharmacistId(prescription.getPharmacist().getId());
      dto.setPharmacistName(prescription.getPharmacist().getUsername());
    }

    dto.setIssueDate(prescription.getIssueDate());
    dto.setStatus(prescription.getStatus());

    if (prescription.getItems() != null) {
      List<PrescriptionItemDto> itemDtos =
          prescription.getItems().stream().map(this::mapItemToDto).collect(Collectors.toList());
      dto.setItems(itemDtos);
    }

    return dto;
  }

  private PrescriptionItemDto mapItemToDto(PrescriptionItem item) {
    PrescriptionItemDto dto = new PrescriptionItemDto();
    dto.setId(item.getId());
    dto.setPrescriptionId(item.getPrescription().getId());
    dto.setMedicineId(item.getMedicine().getId());
    dto.setMedicineName(item.getMedicine().getName());
    dto.setQuantity(item.getQuantity());
    dto.setDosageInstructions(item.getDosageInstructions());
    return dto;
  }
}
