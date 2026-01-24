/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmacy.dto.SalesOrderDto;
import com.pharmacy.entity.SalesOrder;
import com.pharmacy.repository.SalesOrderRepository;
import com.pharmacy.service.SalesOrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SalesOrderServiceImpl implements SalesOrderService {
  private final SalesOrderRepository orderRepository;

  @Override
  public SalesOrderDto create(SalesOrderDto dto) {
    SalesOrder order = toEntity(dto);
    order.setId(null); // Ensure new entity
    SalesOrder saved = orderRepository.save(order);
    return toDto(saved);
  }

  @Override
  public SalesOrderDto update(Long id, SalesOrderDto dto) {
    SalesOrder order =
        orderRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
    order.setSoNumber(dto.getSoNumber());
    order.setCustomer(dto.getCustomer());
    order.setProduct(dto.getProduct());
    order.setQuantity(dto.getQuantity());
    order.setDispatched(dto.getDispatched());
    order.setAmount(dto.getAmount());
    order.setWorkOrder(dto.getWorkOrder());
    order.setStatus(dto.getStatus() != null ? SalesOrder.Status.valueOf(dto.getStatus()) : null);
    order.setOrderDate(dto.getOrderDate());
    SalesOrder saved = orderRepository.save(order);
    return toDto(saved);
  }

  @Override
  public void delete(Long id) {
    orderRepository.deleteById(id);
  }

  @Override
  public SalesOrderDto getById(Long id) {
    SalesOrder order =
        orderRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
    return toDto(order);
  }

  @Override
  public List<SalesOrderDto> getAll() {
    return orderRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
  }

  @Override
  public SalesOrderDto changeStatus(Long id, String status) {
    SalesOrder order =
        orderRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
    order.setStatus(SalesOrder.Status.valueOf(status));
    SalesOrder saved = orderRepository.save(order);
    return toDto(saved);
  }

  private SalesOrderDto toDto(SalesOrder order) {
    SalesOrderDto dto = new SalesOrderDto();
    dto.setId(order.getId());
    dto.setSoNumber(order.getSoNumber());
    dto.setCustomer(order.getCustomer());
    dto.setProduct(order.getProduct());
    dto.setQuantity(order.getQuantity());
    dto.setDispatched(order.getDispatched());
    dto.setAmount(order.getAmount());
    dto.setWorkOrder(order.getWorkOrder());
    dto.setStatus(order.getStatus() != null ? order.getStatus().name() : null);
    dto.setOrderDate(order.getOrderDate());
    return dto;
  }

  private SalesOrder toEntity(SalesOrderDto dto) {
    SalesOrder order = new SalesOrder();
    order.setId(dto.getId());
    order.setSoNumber(dto.getSoNumber());
    order.setCustomer(dto.getCustomer());
    order.setProduct(dto.getProduct());
    order.setQuantity(dto.getQuantity());
    order.setDispatched(dto.getDispatched());
    order.setAmount(dto.getAmount());
    order.setWorkOrder(dto.getWorkOrder());
    order.setStatus(dto.getStatus() != null ? SalesOrder.Status.valueOf(dto.getStatus()) : null);
    order.setOrderDate(dto.getOrderDate());
    return order;
  }
}
