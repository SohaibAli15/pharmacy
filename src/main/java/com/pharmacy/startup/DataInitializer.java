/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.startup;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.pharmacy.entity.User;
import com.pharmacy.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    if (userRepository.count() == 0) {
      User admin = new User();
      admin.setUsername("admin");
      admin.setPassword(passwordEncoder.encode("admin123"));
      admin.setEmail("admin@pharma.local");
      admin.setRole(User.Role.ADMIN);
      userRepository.save(admin);

      User pharmacist = new User();
      pharmacist.setUsername("pharma");
      pharmacist.setPassword(passwordEncoder.encode("pharma123"));
      pharmacist.setEmail("pharma@pharma.local");
      pharmacist.setRole(User.Role.PHARMACIST);
      userRepository.save(pharmacist);
    }
  }
}
