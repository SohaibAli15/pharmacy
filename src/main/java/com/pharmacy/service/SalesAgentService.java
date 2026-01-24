/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pharmacy.entity.SalesAgent;
import com.pharmacy.repository.SalesAgentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalesAgentService {
  private final SalesAgentRepository salesAgentRepository;

  public List<SalesAgent> getAllAgents() {
    return salesAgentRepository.findAll();
  }

  public Optional<SalesAgent> getAgentById(Long id) {
    return salesAgentRepository.findById(id);
  }

  public SalesAgent createAgent(SalesAgent agent) {
    return salesAgentRepository.save(agent);
  }

  public SalesAgent updateAgent(Long id, SalesAgent agent) {
    agent.setId(id);
    return salesAgentRepository.save(agent);
  }

  public void deleteAgent(Long id) {
    salesAgentRepository.deleteById(id);
  }
}
