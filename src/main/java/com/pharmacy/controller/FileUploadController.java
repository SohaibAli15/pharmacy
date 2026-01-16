/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * File Upload Controller for handling document uploads Supports: Images, PDFs, Excel files, CSV
 * files for various modules
 */
@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "File Management", description = "APIs for file upload, download, and management")
public class FileUploadController {

  @Value("${file.upload-dir:uploads}")
  private String uploadDir;

  private static final List<String> ALLOWED_EXTENSIONS =
      List.of("jpg", "jpeg", "png", "gif", "pdf", "xlsx", "xls", "csv", "doc", "docx");

  private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

  /** Upload a file */
  @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(
      summary = "Upload a file",
      description =
          "Upload documents, images, or data files. Supports JPG, PNG, PDF, Excel, CSV formats.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "File uploaded successfully",
            content = @Content(schema = @Schema(implementation = FileUploadResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid file or file type not allowed"),
        @ApiResponse(responseCode = "413", description = "File size exceeds maximum limit")
      })
  public ResponseEntity<FileUploadResponse> uploadFile(
      @Parameter(description = "File to upload", required = true) @RequestParam("file")
          MultipartFile file,
      @Parameter(description = "File category (e.g., prescription, invoice, certificate)")
          @RequestParam(value = "category", required = false, defaultValue = "general")
          String category,
      @Parameter(description = "Optional description")
          @RequestParam(value = "description", required = false)
          String description) {

    try {
      // Validate file
      if (file.isEmpty()) {
        return ResponseEntity.badRequest()
            .body(new FileUploadResponse(false, "File is empty", null, null, null, null));
      }

      // Check file size
      if (file.getSize() > MAX_FILE_SIZE) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
            .body(
                new FileUploadResponse(
                    false, "File size exceeds maximum limit of 10MB", null, null, null, null));
      }

      // Validate file extension
      String originalFilename = file.getOriginalFilename();
      String extension = getFileExtension(originalFilename);

      if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
        return ResponseEntity.badRequest()
            .body(
                new FileUploadResponse(
                    false,
                    "File type not allowed. Allowed types: " + ALLOWED_EXTENSIONS,
                    null,
                    null,
                    null,
                    null));
      }

      // Create upload directory if it doesn't exist
      Path uploadPath = Paths.get(uploadDir, category);
      Files.createDirectories(uploadPath);

      // Generate unique filename
      String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
      String uniqueFilename =
          timestamp + "_" + UUID.randomUUID().toString().substring(0, 8) + "." + extension;
      Path filePath = uploadPath.resolve(uniqueFilename);

      // Save file
      Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

      log.info("File uploaded successfully: {} (Category: {})", uniqueFilename, category);

      FileUploadResponse response =
          new FileUploadResponse(
              true,
              "File uploaded successfully",
              uniqueFilename,
              category + "/" + uniqueFilename,
              file.getSize(),
              extension);

      return ResponseEntity.ok(response);

    } catch (IOException e) {
      log.error("Error uploading file", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(
              new FileUploadResponse(
                  false, "Error uploading file: " + e.getMessage(), null, null, null, null));
    }
  }

  /** Upload multiple files */
  @PostMapping(value = "/upload-multiple", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "Upload multiple files", description = "Upload multiple files at once")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Files uploaded successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid files")
      })
  public ResponseEntity<Map<String, Object>> uploadMultipleFiles(
      @Parameter(description = "Files to upload", required = true) @RequestParam("files")
          MultipartFile[] files,
      @Parameter(description = "File category")
          @RequestParam(value = "category", required = false, defaultValue = "general")
          String category) {

    List<FileUploadResponse> responses = new ArrayList<>();
    int successCount = 0;
    int failureCount = 0;

    for (MultipartFile file : files) {
      ResponseEntity<FileUploadResponse> response = uploadFile(file, category, null);
      FileUploadResponse uploadResponse = response.getBody();
      responses.add(uploadResponse);

      if (uploadResponse != null && uploadResponse.success()) {
        successCount++;
      } else {
        failureCount++;
      }
    }

    Map<String, Object> result = new HashMap<>();
    result.put("totalFiles", files.length);
    result.put("successCount", successCount);
    result.put("failureCount", failureCount);
    result.put("files", responses);

    return ResponseEntity.ok(result);
  }

  /** Download a file */
  @GetMapping("/download/{category}/{filename:.+}")
  @Operation(summary = "Download a file", description = "Download a previously uploaded file")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "File downloaded successfully"),
        @ApiResponse(responseCode = "404", description = "File not found")
      })
  public ResponseEntity<Resource> downloadFile(
      @Parameter(description = "File category", required = true) @PathVariable String category,
      @Parameter(description = "Filename", required = true) @PathVariable String filename) {

    try {
      Path filePath = Paths.get(uploadDir, category, filename);
      Resource resource = new UrlResource(filePath.toUri());

      if (resource.exists() && resource.isReadable()) {
        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
          contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + resource.getFilename() + "\"")
            .body(resource);
      } else {
        return ResponseEntity.notFound().build();
      }
    } catch (MalformedURLException e) {
      log.error("Error downloading file", e);
      return ResponseEntity.badRequest().build();
    } catch (IOException e) {
      log.error("Error reading file", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  /** Delete a file */
  @DeleteMapping("/delete/{category}/{filename:.+}")
  @Operation(summary = "Delete a file", description = "Delete an uploaded file")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "File deleted successfully"),
        @ApiResponse(responseCode = "404", description = "File not found")
      })
  public ResponseEntity<Map<String, String>> deleteFile(
      @Parameter(description = "File category", required = true) @PathVariable String category,
      @Parameter(description = "Filename", required = true) @PathVariable String filename) {

    try {
      Path filePath = Paths.get(uploadDir, category, filename);

      if (Files.exists(filePath)) {
        Files.delete(filePath);
        log.info("File deleted successfully: {}/{}", category, filename);

        Map<String, String> response = new HashMap<>();
        response.put("success", "true");
        response.put("message", "File deleted successfully");
        return ResponseEntity.ok(response);
      } else {
        Map<String, String> response = new HashMap<>();
        response.put("success", "false");
        response.put("message", "File not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
      }
    } catch (IOException e) {
      log.error("Error deleting file", e);
      Map<String, String> response = new HashMap<>();
      response.put("success", "false");
      response.put("message", "Error deleting file: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  /** List files in a category */
  @GetMapping("/list/{category}")
  @Operation(
      summary = "List files by category",
      description = "Get a list of all files in a specific category")
  @ApiResponse(responseCode = "200", description = "Files retrieved successfully")
  public ResponseEntity<Map<String, Object>> listFiles(
      @Parameter(description = "File category", required = true) @PathVariable String category) {

    try {
      Path categoryPath = Paths.get(uploadDir, category);

      if (!Files.exists(categoryPath)) {
        Map<String, Object> response = new HashMap<>();
        response.put("category", category);
        response.put("files", new ArrayList<>());
        response.put("count", 0);
        return ResponseEntity.ok(response);
      }

      List<Map<String, Object>> fileList = new ArrayList<>();

      Files.list(categoryPath)
          .forEach(
              path -> {
                try {
                  Map<String, Object> fileInfo = new HashMap<>();
                  fileInfo.put("filename", path.getFileName().toString());
                  fileInfo.put("size", Files.size(path));
                  fileInfo.put("lastModified", Files.getLastModifiedTime(path).toString());
                  fileInfo.put("path", category + "/" + path.getFileName().toString());
                  fileList.add(fileInfo);
                } catch (IOException e) {
                  log.error("Error reading file info", e);
                }
              });

      Map<String, Object> response = new HashMap<>();
      response.put("category", category);
      response.put("files", fileList);
      response.put("count", fileList.size());

      return ResponseEntity.ok(response);

    } catch (IOException e) {
      log.error("Error listing files", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  private String getFileExtension(String filename) {
    if (filename == null || filename.lastIndexOf(".") == -1) {
      return "";
    }
    return filename.substring(filename.lastIndexOf(".") + 1);
  }

  /** File Upload Response DTO */
  public record FileUploadResponse(
      boolean success,
      String message,
      String filename,
      String filePath,
      Long fileSize,
      String fileType) {}
}
