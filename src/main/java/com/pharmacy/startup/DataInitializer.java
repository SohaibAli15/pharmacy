/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.startup;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.pharmacy.entity.AlertTypeEntity;
import com.pharmacy.entity.CustomerStatusEntity;
import com.pharmacy.entity.CustomerTypeEntity;
import com.pharmacy.entity.Role;
import com.pharmacy.entity.StoreStatusEntity;
import com.pharmacy.entity.StoreTypeEntity;
import com.pharmacy.entity.User;
import com.pharmacy.repository.AlertTypeRepository;
import com.pharmacy.repository.AuditLogRepository;
import com.pharmacy.repository.CustomerStatusRepository;
import com.pharmacy.repository.CustomerTypeRepository;
import com.pharmacy.repository.DispatchRepository;
import com.pharmacy.repository.MaterialIssueRepository;
import com.pharmacy.repository.RoleRepository;
import com.pharmacy.repository.StoreStatusRepository;
import com.pharmacy.repository.StoreTypeRepository;
import com.pharmacy.repository.UserRepository;
import com.pharmacy.repository.WorkOrderRepository;

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
  private final RoleRepository roleRepository;
  private final MaterialIssueRepository materialIssueRepository;
  private final WorkOrderRepository workOrderRepository;
  private final DispatchRepository dispatchRepository;
  private final AuditLogRepository auditLogRepository;

  @PersistenceContext private EntityManager entityManager;

  @Transactional
  @Override
  public void run(ApplicationArguments args) throws Exception {
    // Initialize Roles FIRST, before truncating users
    boolean rolesSeeded = false;
    if (roleRepository.count() == 0) {
      roleRepository.save(
          new Role(
              null, "ADMIN", true, java.time.LocalDateTime.now(), java.time.LocalDateTime.now()));
      roleRepository.save(
          new Role(
              null,
              "PHARMACIST",
              true,
              java.time.LocalDateTime.now(),
              java.time.LocalDateTime.now()));
      rolesSeeded = true;
    }

    // Assign roles to default users
    Role adminRole = roleRepository.findByName("ADMIN");
    Role pharmacistRole = roleRepository.findByName("PHARMACIST");
    if (adminRole == null || pharmacistRole == null) {
      throw new IllegalStateException("Roles not found in database. Check role seeding logic.");
    }
    if (userRepository.count() == 0) {
      User admin = new User();
      admin.setUsername("admin");
      admin.setPassword(passwordEncoder.encode("admin123"));
      admin.setEmail("admin@pharma.local");
      admin.setRole(adminRole);
      userRepository.save(admin);

      User pharmacist = new User();
      pharmacist.setUsername("pharma");
      pharmacist.setPassword(passwordEncoder.encode("pharma123"));
      pharmacist.setEmail("pharma@pharma.local");
      pharmacist.setRole(pharmacistRole);
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

    // Initialize Order Statuses (Full/Partial/Etc.)
    // This can be done via enums, but if you want dynamic DB-driven statuses, add here
    // Example: Seed statuses for SalesOrder, PurchaseOrder, etc. if using status tables
    // (Optional) Initialize MaterialIssue, WorkOrder, Dispatch, and AuditLog demo data
    // Example for MaterialIssue:
    // materialIssueRepository.save(MaterialIssue.builder()
    //     .productionBatch(productionBatch)
    //     .salesOrder(salesOrder)
    //     .issueDate(LocalDateTime.now())
    //     .totalQuantityIssued(BigDecimal.ZERO)
    //     .status(MaterialIssue.Status.PENDING)
    //     .build());
    // Similarly, seed WorkOrder, Dispatch, and AuditLog if needed
    // Example: Seed demo MaterialIssue
    // if (materialIssueRepository.count() == 0) {
    //   materialIssueRepository.save(MaterialIssue.builder()
    //       .issueDate(LocalDateTime.now())
    //       .totalQuantityIssued(BigDecimal.ZERO)
    //       .status(MaterialIssue.Status.PENDING)
    //       .build());
    // }
    // Example: Seed demo WorkOrder
    // if (workOrderRepository.count() == 0) {
    //   workOrderRepository.save(WorkOrder.builder()
    //       .workOrderNumber("WO-001")
    //       .createdAt(LocalDateTime.now())
    //       .status(WorkOrder.Status.CREATED)
    //       .build());
    // }
    // Example: Seed demo Dispatch
    // if (dispatchRepository.count() == 0) {
    //   dispatchRepository.save(Dispatch.builder()
    //       .dispatchDate(LocalDateTime.now())
    //       .quantityDispatched(BigDecimal.ZERO)
    //       .status(Dispatch.Status.PENDING)
    //       .build());
    // }
    // Example: Seed demo AuditLog
    // if (auditLogRepository.count() == 0) {
    //   auditLogRepository.save(AuditLog.builder()
    //       .entityName("User")
    //       .entityId(1L)
    //       .action("CREATE")
    //       .performedBy("admin")
    //       .performedAt(LocalDateTime.now())
    //       .details("Initial admin user created.")
    //       .build());
    // }
    // Seed demo MaterialIssue if none exists
    // NOTE: Do not seed MaterialIssue with null productionBatch or salesOrder to avoid DB
    // constraint errors
    // if (materialIssueRepository.count() == 0) {
    //   materialIssueRepository.save(
    //       com.pharmacy.entity.MaterialIssue.builder()
    //           .issueDate(java.time.LocalDateTime.now())
    //           .totalQuantityIssued(java.math.BigDecimal.ZERO)
    //           .status(com.pharmacy.entity.MaterialIssue.Status.PENDING)
    //           .build());
    // }
    // Seed demo WorkOrder if none exists
    // NOTE: Do not seed WorkOrder with null salesOrder to avoid DB constraint errors
    // if (workOrderRepository.count() == 0) {
    //   workOrderRepository.save(
    //       com.pharmacy.entity.WorkOrder.builder()
    //           .workOrderNumber("WO-001")
    //           .createdAt(java.time.LocalDateTime.now())
    //           .status(com.pharmacy.entity.WorkOrder.Status.CREATED)
    //           .build());
    // }
    // Seed demo Dispatch if none exists
    //    if (dispatchRepository.count() == 0) {
    //      dispatchRepository.save(
    //          com.pharmacy.entity.Dispatch.builder()
    //              .dispatchDate(java.time.LocalDateTime.now())
    //              .quantityDispatched(java.math.BigDecimal.ZERO)
    //              .status(com.pharmacy.entity.Dispatch.Status.PENDING)
    //              .build());
    //    }
    //    // Seed demo AuditLog if none exists
    //    if (auditLogRepository.count() == 0) {
    //      auditLogRepository.save(
    //          com.pharmacy.entity.AuditLog.builder()
    //              .entityName("User")
    //              .entityId(1L)
    //              .action("CREATE")
    //              .performedBy("admin")
    //              .performedAt(java.time.LocalDateTime.now())
    //              .details("Initial admin user created.")
    //              .build());
    //    }
  }
}
