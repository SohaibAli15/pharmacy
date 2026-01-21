/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmacy.dto.StoreDto;
import com.pharmacy.entity.Store;
import com.pharmacy.entity.User;
import com.pharmacy.repository.StoreRepository;
import com.pharmacy.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreService {

  private final StoreRepository storeRepository;
  private final UserRepository userRepository;

  @Transactional
  public StoreDto createStore(StoreDto storeDto) {
    if (storeRepository.existsByCode(storeDto.getCode())) {
      throw new RuntimeException("Store code already exists");
    }

    Store store = mapToEntity(storeDto);
    store.setCreatedAt(LocalDateTime.now());
    store.setUpdatedAt(LocalDateTime.now());

    Store savedStore = storeRepository.save(store);
    return mapToDto(savedStore);
  }

  @Transactional
  public StoreDto updateStore(Long id, StoreDto storeDto) {
    Store store =
        storeRepository.findById(id).orElseThrow(() -> new RuntimeException("Store not found"));

    if (!store.getCode().equals(storeDto.getCode())
        && storeRepository.existsByCode(storeDto.getCode())) {
      throw new RuntimeException("Store code already exists");
    }

    updateEntityFromDto(store, storeDto);
    store.setUpdatedAt(LocalDateTime.now());

    Store updatedStore = storeRepository.save(store);
    return mapToDto(updatedStore);
  }

  @Transactional(readOnly = true)
  public StoreDto getStoreById(Long id) {
    Store store =
        storeRepository.findById(id).orElseThrow(() -> new RuntimeException("Store not found"));
    return mapToDto(store);
  }

  @Transactional(readOnly = true)
  public StoreDto getStoreByCode(String code) {
    Store store =
        storeRepository.findByCode(code).orElseThrow(() -> new RuntimeException("Store not found"));
    return mapToDto(store);
  }

  @Transactional(readOnly = true)
  public Page<StoreDto> getAllStores(Pageable pageable) {
    return storeRepository.findAll(pageable).map(this::mapToDto);
  }

  @Transactional(readOnly = true)
  public Page<StoreDto> getStoresByStatus(Store.StoreStatus status, Pageable pageable) {
    return storeRepository.findByStatus(status, pageable).map(this::mapToDto);
  }

  @Transactional(readOnly = true)
  public Page<StoreDto> getStoresByType(Store.StoreType type, Pageable pageable) {
    return storeRepository.findByType(type, pageable).map(this::mapToDto);
  }

  // Backwards compatible list methods
  @Transactional(readOnly = true)
  public List<StoreDto> getAllStores() {
    return getAllStores(org.springframework.data.domain.PageRequest.of(0, 20)).getContent();
  }

  @Transactional(readOnly = true)
  public List<StoreDto> getStoresByStatus(Store.StoreStatus status) {
    return getStoresByStatus(status, org.springframework.data.domain.PageRequest.of(0, 20))
        .getContent();
  }

  @Transactional(readOnly = true)
  public List<StoreDto> getStoresByType(Store.StoreType type) {
    return getStoresByType(type, org.springframework.data.domain.PageRequest.of(0, 20))
        .getContent();
  }

  @Transactional
  public void deleteStore(Long id) {
    if (!storeRepository.existsById(id)) {
      throw new RuntimeException("Store not found");
    }
    storeRepository.deleteById(id);
  }

  private StoreDto mapToDto(Store store) {
    StoreDto dto = new StoreDto();
    dto.setId(store.getId());
    dto.setCode(store.getCode());
    dto.setName(store.getName());
    dto.setType(store.getType());
    dto.setAddress(store.getAddress());
    dto.setCity(store.getCity());
    dto.setState(store.getState());
    dto.setCountry(store.getCountry());
    dto.setZipCode(store.getZipCode());
    dto.setPhone(store.getPhone());
    dto.setEmail(store.getEmail());
    if (store.getManager() != null) {
      dto.setManagerId(store.getManager().getId());
      dto.setManagerName(store.getManager().getUsername());
    }
    dto.setStatus(store.getStatus());
    dto.setNotes(store.getNotes());
    dto.setCreatedAt(store.getCreatedAt());
    dto.setUpdatedAt(store.getUpdatedAt());
    return dto;
  }

  private Store mapToEntity(StoreDto dto) {
    Store store = new Store();
    store.setCode(dto.getCode());
    store.setName(dto.getName());
    store.setType(dto.getType());
    store.setAddress(dto.getAddress());
    store.setCity(dto.getCity());
    store.setState(dto.getState());
    store.setCountry(dto.getCountry());
    store.setZipCode(dto.getZipCode());
    store.setPhone(dto.getPhone());
    store.setEmail(dto.getEmail());
    if (dto.getManagerId() != null) {
      User manager =
          userRepository
              .findById(dto.getManagerId())
              .orElseThrow(() -> new RuntimeException("Manager not found"));
      store.setManager(manager);
    }
    store.setStatus(dto.getStatus());
    store.setNotes(dto.getNotes());
    return store;
  }

  private void updateEntityFromDto(Store store, StoreDto dto) {
    store.setCode(dto.getCode());
    store.setName(dto.getName());
    store.setType(dto.getType());
    store.setAddress(dto.getAddress());
    store.setCity(dto.getCity());
    store.setState(dto.getState());
    store.setCountry(dto.getCountry());
    store.setZipCode(dto.getZipCode());
    store.setPhone(dto.getPhone());
    store.setEmail(dto.getEmail());
    if (dto.getManagerId() != null) {
      User manager =
          userRepository
              .findById(dto.getManagerId())
              .orElseThrow(() -> new RuntimeException("Manager not found"));
      store.setManager(manager);
    }
    store.setStatus(dto.getStatus());
    store.setNotes(dto.getNotes());
  }
}
