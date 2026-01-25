/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmacy.entity.Dispatch;
import com.pharmacy.repository.DispatchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DispatchService {
  private final DispatchRepository dispatchRepository;

  public List<Dispatch> findAll() {
    return dispatchRepository.findAll();
  }

  public Optional<Dispatch> findById(Long id) {
    return dispatchRepository.findById(id);
  }

  public Dispatch save(Dispatch dispatch) {
    return dispatchRepository.save(dispatch);
  }

  public void deleteById(Long id) {
    dispatchRepository.deleteById(id);
  }
}
