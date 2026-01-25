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
import com.pharmacy.entity.StoreStatusEntity;
import com.pharmacy.entity.StoreTypeEntity;
import com.pharmacy.entity.User;
import com.pharmacy.repository.StoreRepository;
import com.pharmacy.repository.StoreStatusRepository;
import com.pharmacy.repository.StoreTypeRepository;
import com.pharmacy.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreService {
  private final StoreRepository storeRepository;
  private final UserRepository userRepository;
  private final StoreTypeRepository storeTypeRepository;
  private final StoreStatusRepository storeStatusRepository;

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
  public Page<StoreDto> getStoresByStatus(StoreStatusEntity status, Pageable pageable) {
    return storeRepository.findByStatus(status, pageable).map(this::mapToDto);
  }

  @Transactional(readOnly = true)
  public Page<StoreDto> getStoresByType(StoreTypeEntity type, Pageable pageable) {
    return storeRepository.findByType(type, pageable).map(this::mapToDto);
  }

  // Backwards compatible list methods
  @Transactional(readOnly = true)
  public List<StoreDto> getAllStores() {
    return getAllStores(org.springframework.data.domain.PageRequest.of(0, 20)).getContent();
  }

  @Transactional(readOnly = true)
  public List<StoreDto> getStoresByStatus(StoreStatusEntity status) {
    return getStoresByStatus(status, org.springframework.data.domain.PageRequest.of(0, 20))
        .getContent();
  }

  @Transactional(readOnly = true)
  public List<StoreDto> getStoresByType(StoreTypeEntity type) {
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
    dto.setStoreTypeId(store.getType() != null ? store.getType().getId() : null);
    dto.setAddress(store.getAddress());
    dto.setCity(store.getCity());
    dto.setState(store.getState());
    dto.setCountry(store.getCountry());
    dto.setZipCode(store.getZipCode());
    dto.setPhone(store.getPhone());
    dto.setEmail(store.getEmail());
    dto.setManagerId(store.getManager() != null ? store.getManager().getId() : null);
    dto.setStoreStatusId(store.getStatus() != null ? store.getStatus().getId() : null);
    dto.setNotes(store.getNotes());
    dto.setCreatedAt(store.getCreatedAt());
    dto.setUpdatedAt(store.getUpdatedAt());
    return dto;
  }

  private Store mapToEntity(StoreDto dto) {
    Store store = new Store();
    store.setCode(dto.getCode());
    store.setName(dto.getName());
    if (dto.getStoreTypeId() != null) {
      StoreTypeEntity type =
          storeTypeRepository
              .findById(dto.getStoreTypeId())
              .orElseThrow(() -> new RuntimeException("Store type not found"));
      store.setType(type);
    }
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
    if (dto.getStoreStatusId() != null) {
      StoreStatusEntity status =
          storeStatusRepository
              .findById(dto.getStoreStatusId())
              .orElseThrow(() -> new RuntimeException("Store status not found"));
      store.setStatus(status);
    }
    store.setNotes(dto.getNotes());
    return store;
  }

  private void updateEntityFromDto(Store store, StoreDto dto) {
    store.setCode(dto.getCode());
    store.setName(dto.getName());
    if (dto.getStoreTypeId() != null) {
      StoreTypeEntity type =
          storeTypeRepository
              .findById(dto.getStoreTypeId())
              .orElseThrow(() -> new RuntimeException("Store type not found"));
      store.setType(type);
    }
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
    if (dto.getStoreStatusId() != null) {
      StoreStatusEntity status =
          storeStatusRepository
              .findById(dto.getStoreStatusId())
              .orElseThrow(() -> new RuntimeException("Store status not found"));
      store.setStatus(status);
    }
    store.setNotes(dto.getNotes());
  }
}
