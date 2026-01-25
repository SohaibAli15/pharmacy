/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProductPageResponse {
  private List<ProductDto> content;
  private long totalElements;
  private int totalPages;
  private int number; // current page number (0-based)
  private int size;
  private boolean first;
  private boolean last;
  private boolean empty;
}
