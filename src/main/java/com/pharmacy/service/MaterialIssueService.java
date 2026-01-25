/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmacy.entity.MaterialIssue;
import com.pharmacy.repository.MaterialIssueRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MaterialIssueService {
  private final MaterialIssueRepository materialIssueRepository;

  public List<MaterialIssue> findAll() {
    return materialIssueRepository.findAll();
  }

  public Optional<MaterialIssue> findById(Long id) {
    return materialIssueRepository.findById(id);
  }

  public MaterialIssue save(MaterialIssue materialIssue) {
    return materialIssueRepository.save(materialIssue);
  }

  public void deleteById(Long id) {
    materialIssueRepository.deleteById(id);
  }
}
