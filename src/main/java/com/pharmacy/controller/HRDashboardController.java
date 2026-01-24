/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pharmacy.service.AttendanceService;
import com.pharmacy.service.DepartmentService;
import com.pharmacy.service.EmployeeService;
import com.pharmacy.service.PayrollService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "HR Dashboard", description = "Endpoints for HR dashboard summary metrics")
@RestController
@RequestMapping("/api/v1/hr-dashboard")
@RequiredArgsConstructor
public class HRDashboardController {
  private final EmployeeService employeeService;
  private final PayrollService payrollService;
  private final AttendanceService attendanceService;
  private final DepartmentService departmentService;

  @Operation(
      summary = "Get HR dashboard summary",
      description =
          "Returns summary metrics for the HR dashboard (total employees, monthly payroll, avg attendance, departments)",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Summary metrics",
            content =
                @Content(
                    mediaType = "application/json",
                    schema =
                        @Schema(
                            example =
                                "{\n  'totalEmployees': 156,\n  'monthlyPayroll': 562000,\n  'avgAttendance': 96.5,\n  'departments': 8\n}")))
      })
  @GetMapping("/summary")
  public ResponseEntity<Map<String, Object>> getSummary() {
    Map<String, Object> summary = new HashMap<>();
    summary.put("totalEmployees", employeeService.getAll().size());
    summary.put("monthlyPayroll", payrollService.getCurrentMonthPayrollTotal());
    summary.put("avgAttendance", attendanceService.getCurrentMonthAverageAttendance());
    summary.put("departments", departmentService.getActiveDepartmentCount());
    return ResponseEntity.ok(summary);
  }
}
