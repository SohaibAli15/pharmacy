/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "KeyCount", description = "Simple key/count pair used in breakdowns")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyCountDto {
  @Schema(description = "Key (e.g. status or location)")
  private String key;

  @Schema(description = "Count for the key", type = "integer", format = "int64")
  private Long count;
}
