/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import lombok.Data;

@Data
public class UserDto {
  private Long id;
  private String username;
  private String password;
  private String email;
  private Long roleId;
}
