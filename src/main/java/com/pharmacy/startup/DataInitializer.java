/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.startup;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.pharmacy.entity.AlertTypeEntity;
import com.pharmacy.entity.CustomerStatusEntity;
import com.pharmacy.entity.CustomerTypeEntity;
import com.pharmacy.entity.StoreStatusEntity;
import com.pharmacy.entity.StoreTypeEntity;
import com.pharmacy.entity.User;
import com.pharmacy.repository.AlertTypeRepository;
import com.pharmacy.repository.CustomerStatusRepository;
import com.pharmacy.repository.CustomerTypeRepository;
import com.pharmacy.repository.StoreStatusRepository;
import com.pharmacy.repository.StoreTypeRepository;
import com.pharmacy.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final CustomerTypeRepository customerTypeRepository;
  private final CustomerStatusRepository customerStatusRepository;
  private final StoreTypeRepository storeTypeRepository;
  private final StoreStatusRepository storeStatusRepository;
  private final AlertTypeRepository alertTypeRepository;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    if (userRepository.count() == 0) {
      User admin = new User();
      admin.setUsername("admin");
      admin.setPassword(passwordEncoder.encode("admin123"));
      admin.setEmail("admin@pharma.local");
      // admin.setRole(User.Role.ADMIN);
      userRepository.save(admin);

      User pharmacist = new User();
      pharmacist.setUsername("pharma");
      pharmacist.setPassword(passwordEncoder.encode("pharma123"));
      pharmacist.setEmail("pharma@pharma.local");
      // pharmacist.setRole(User.Role.PHARMACIST);
      userRepository.save(pharmacist);
    }

    // Initialize Customer Types
    if (customerTypeRepository.count() == 0) {
      customerTypeRepository.save(new CustomerTypeEntity(null, "REGULAR", true));
      customerTypeRepository.save(new CustomerTypeEntity(null, "VIP", true));
      customerTypeRepository.save(new CustomerTypeEntity(null, "WHOLESALE", true));
      customerTypeRepository.save(new CustomerTypeEntity(null, "INSTITUTIONAL", true));
    }
    // Initialize Customer Statuses
    if (customerStatusRepository.count() == 0) {
      customerStatusRepository.save(new CustomerStatusEntity(null, "ACTIVE", true));
      customerStatusRepository.save(new CustomerStatusEntity(null, "INACTIVE", true));
      customerStatusRepository.save(new CustomerStatusEntity(null, "BLOCKED", true));
    }
    // Initialize Store Types
    if (storeTypeRepository.count() == 0) {
      storeTypeRepository.save(new StoreTypeEntity(null, "WAREHOUSE", true));
      storeTypeRepository.save(new StoreTypeEntity(null, "RETAIL_STORE", true));
      storeTypeRepository.save(new StoreTypeEntity(null, "DISTRIBUTION_CENTER", true));
      storeTypeRepository.save(new StoreTypeEntity(null, "MANUFACTURING_UNIT", true));
    }
    // Initialize Store Statuses
    if (storeStatusRepository.count() == 0) {
      storeStatusRepository.save(new StoreStatusEntity(null, "ACTIVE", true));
      storeStatusRepository.save(new StoreStatusEntity(null, "INACTIVE", true));
      storeStatusRepository.save(new StoreStatusEntity(null, "MAINTENANCE", true));
    }
    // Initialize Alert Types
    if (alertTypeRepository.count() == 0) {
      alertTypeRepository.save(new AlertTypeEntity(null, "LOW_STOCK", true));
      alertTypeRepository.save(new AlertTypeEntity(null, "HIGH_STOCK", true));
    }
  }
}
