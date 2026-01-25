/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmacy.dto.ProductDto;
import com.pharmacy.entity.Product;
import com.pharmacy.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

  private final ProductRepository productRepository;

  public ProductDto toDto(Product p) {
    ProductDto dto = new ProductDto();
    dto.setId(p.getId());
    dto.setName(p.getName());
    dto.setDescription(p.getDescription());
    dto.setManufacturer(p.getManufacturer());
    dto.setPrice(p.getPrice());
    dto.setStockQuantity(p.getStockQuantity());
    dto.setExpiryDate(p.getExpiryDate());
    dto.setCategory(p.getCategory());
    return dto;
  }

  public Product fromDto(ProductDto dto) {
    Product p = new Product();
    p.setId(dto.getId());
    p.setName(dto.getName());
    p.setDescription(dto.getDescription());
    p.setManufacturer(dto.getManufacturer());
    p.setPrice(dto.getPrice());
    p.setStockQuantity(dto.getStockQuantity());
    p.setExpiryDate(dto.getExpiryDate());
    p.setCategory(dto.getCategory());
    return p;
  }

  public ProductDto create(ProductDto dto) {
    Product p = fromDto(dto);
    Product saved = productRepository.save(p);
    return toDto(saved);
  }

  public ProductDto update(Long id, ProductDto dto) {
    Product existing = productRepository.findById(id).orElseThrow();
    existing.setName(dto.getName());
    existing.setDescription(dto.getDescription());
    existing.setManufacturer(dto.getManufacturer());
    existing.setPrice(dto.getPrice());
    existing.setStockQuantity(dto.getStockQuantity());
    existing.setExpiryDate(dto.getExpiryDate());
    existing.setCategory(dto.getCategory());
    Product saved = productRepository.save(existing);
    return toDto(saved);
  }

  public void delete(Long id) {
    productRepository.deleteById(id);
  }

  public ProductDto getById(Long id) {
    return productRepository.findById(id).map(this::toDto).orElse(null);
  }

  public Page<ProductDto> searchByName(String name, Pageable pageable) {
    return productRepository.findByNameContainingIgnoreCase(name, pageable).map(this::toDto);
  }

  public Page<ProductDto> findByCategory(String category, Pageable pageable) {
    return productRepository.findByCategory(category, pageable).map(this::toDto);
  }

  public Page<ProductDto> listAll(Pageable pageable) {
    return productRepository.findAll(pageable).map(this::toDto);
  }
}
