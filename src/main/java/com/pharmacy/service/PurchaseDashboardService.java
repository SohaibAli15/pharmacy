/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmacy.repository.GoodsReceiptNoteRepository;
import com.pharmacy.repository.PurchaseInvoiceRepository;
import com.pharmacy.repository.PurchaseOrderRepository;
import com.pharmacy.repository.PurchasePaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PurchaseDashboardService {
  private final PurchaseOrderRepository purchaseOrderRepository;
  private final GoodsReceiptNoteRepository grnRepository;
  private final PurchaseInvoiceRepository invoiceRepository;
  private final PurchasePaymentRepository paymentRepository;

  @Transactional(readOnly = true)
  public Map<String, Object> getSummary() {
    Map<String, Object> summary = new HashMap<>();
    long pendingGRN =
        grnRepository.findAll().stream()
            .filter(grn -> grn.getStatus() == com.pharmacy.entity.GoodsReceiptNote.Status.CREATED)
            .count();
    long received =
        purchaseOrderRepository
            .findByStatus(
                com.pharmacy.entity.PurchaseOrder.OrderStatus.RECEIVED,
                org.springframework.data.domain.Pageable.unpaged())
            .getTotalElements();
    long pendingPayments =
        paymentRepository.findAll().stream()
            .filter(p -> p.getStatus() == com.pharmacy.entity.PurchasePayment.Status.INITIATED)
            .count();
    double totalSpend =
        purchaseOrderRepository.findAll().stream()
            .mapToDouble(po -> po.getTotalAmount().doubleValue())
            .sum();
    summary.put("totalPOs", purchaseOrderRepository.count());
    summary.put("pendingGRN", pendingGRN);
    summary.put("received", received);
    summary.put("pendingPayments", pendingPayments);
    summary.put("totalSpend", totalSpend);
    return summary;
  }

  // Create a GRN from an APPROVED PO
  @Transactional
  public com.pharmacy.entity.GoodsReceiptNote createGRNFromApprovedPO(Long purchaseOrderId) {
    var po =
        purchaseOrderRepository
            .findById(purchaseOrderId)
            .orElseThrow(() -> new IllegalArgumentException("Purchase Order not found"));
    if (po.getStatus() != com.pharmacy.entity.PurchaseOrder.OrderStatus.APPROVED) {
      throw new IllegalStateException("GRN can only be created for APPROVED Purchase Orders");
    }
    // Check if GRN already exists for this PO
    boolean exists =
        grnRepository.findAll().stream()
            .anyMatch(grn -> grn.getPurchaseOrder().getId().equals(purchaseOrderId));
    if (exists) {
      throw new IllegalStateException("GRN already exists for this Purchase Order");
    }
    com.pharmacy.entity.GoodsReceiptNote grn = new com.pharmacy.entity.GoodsReceiptNote();
    grn.setPurchaseOrder(po);
    grn.setDate(java.time.LocalDate.now());
    grn.setStatus(com.pharmacy.entity.GoodsReceiptNote.Status.CREATED);
    grn.setReferenceNumber("GRN-" + po.getOrderNumber());
    grn.setVendor(po.getSupplier().getName());
    grn.setAmount(po.getTotalAmount());
    return grnRepository.save(grn);
  }
}
