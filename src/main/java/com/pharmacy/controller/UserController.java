/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.UserDto;
import com.pharmacy.entity.User;
import com.pharmacy.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
    name = "User Management",
    description = "APIs for managing system users and authentication (v1)")
public class UserController {

  private final UserService userService;

  @PostMapping
  @Operation(
      summary = "Create a new user",
      description =
          "Register a new system user with authentication credentials and role assignment")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "User created successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or duplicate username")
      })
  public ResponseEntity<User> createUser(@RequestBody UserDto userDto) {
    User savedUser = userService.saveUser(userDto);
    return ResponseEntity.ok(savedUser);
  }

  @GetMapping
  @Operation(summary = "Get all users", description = "Retrieve all registered system users")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved all users",
      content =
          @Content(
              mediaType = "application/json",
              array =
                  @io.swagger.v3.oas.annotations.media.ArraySchema(
                      schema = @Schema(implementation = User.class))))
  public ResponseEntity<List<User>> getAllUsers() {
    List<User> users = userService.findAllUsers();
    return ResponseEntity.ok(users);
  }

  @GetMapping("/{username}")
  @Operation(
      summary = "Get user by username",
      description = "Retrieve a specific user by their username")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "User found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "404", description = "User not found")
      })
  public ResponseEntity<User> getUserByUsername(
      @Parameter(description = "Username", required = true) @PathVariable String username) {
    return userService
        .findByUsername(username)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a user", description = "Remove a user from the system")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
      })
  public ResponseEntity<Void> deleteUser(
      @Parameter(description = "User ID", required = true) @PathVariable Long id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }
}
