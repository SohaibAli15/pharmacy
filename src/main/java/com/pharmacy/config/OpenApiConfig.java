/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
public class OpenApiConfig {

  @Value("${spring.application.name:Pharmacy Management System}")
  private String applicationName;

  @Value("${api.version:v1}")
  private String apiVersion;

  @Bean
  public OpenAPI pharmacyOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title(applicationName + " - Complete API Documentation")
                .description(
                    """
                                **Complete Pharmacy Management System API - End-to-End Solution**

                                This comprehensive API provides complete pharmacy management functionality from suppliers to customers:

                                **🔄 Complete Business Flow:**
                                Suppliers → Purchase Orders → Ingredients → Manufacturing → Medicines → Stock Transfers → Retail Stores → Sales → Customers

                                **📦 Core Modules:**
                                - **Supplier Management** - Manage ingredient suppliers
                                - **Purchase Order Management** - Order raw materials
                                - **Store Management** - Multi-store support (Warehouse, Manufacturing, Retail, Distribution)
                                - **Ingredient Management** - Raw material inventory with batch tracking
                                - **Recipe Management** - Define medicine formulations
                                - **Production/Manufacturing** - Manufacture medicines from ingredients
                                - **Medicine Management** - Finished product catalog
                                - **Inventory Management** - Stock tracking per store with batch & expiry
                                - **Stock Transfer Management** - Transfer stock between stores with approval workflow
                                - **Customer Management** - Customer database with medical history
                                - **Prescription Management** - Medical prescriptions
                                - **Sales Management** - Point of sale with FIFO inventory
                                - **Alert System** - Low stock and expiry alerts

                                **✨ Key Features:**
                                - FIFO Inventory Management (First In, First Out)
                                - Multi-store operations with independent stock
                                - Batch tracking with expiry dates
                                - Approval workflows (Purchase Orders, Stock Transfers)
                                - Real-time inventory updates
                                - Automatic invoice generation
                                - Sale cancellation & returns
                                - GMP compliance for manufacturing
                                - Full audit trail
                                - File upload support for documents

                                **🔒 Security:**
                                - JWT Bearer token authentication
                                - Role-based access control
                                - Secure API endpoints

                                **📊 Standards Compliance:**
                                - Good Manufacturing Practice (GMP)
                                - Full product traceability
                                - Data integrity controls
                                - Regulatory compliance ready

                                **API Version:** %s
                                """
                        .formatted(apiVersion))
                .version(apiVersion)
                .contact(
                    new Contact()
                        .name("Pharmacy System Support")
                        .email("support@pharmacy.com")
                        .url("https://pharmacy.com"))
                .license(
                    new License().name("Proprietary License").url("https://pharmacy.com/license")))
        .servers(
            List.of(
                new Server().url("http://localhost:8080").description("Local Development Server"),
                new Server().url("https://api.pharmacy.com").description("Production Server")))
        .components(
            new Components()
                // Security Schemes
                .addSecuritySchemes(
                    "bearerAuth",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("JWT authentication token"))
                .addSecuritySchemes(
                    "apiKey",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.HEADER)
                        .name("X-API-KEY")
                        .description("API Key for service-to-service authentication"))

                // File Upload Request Body
                .addRequestBodies(
                    "FileUpload",
                    new RequestBody()
                        .description("File upload request")
                        .required(true)
                        .content(
                            new Content()
                                .addMediaType(
                                    "multipart/form-data",
                                    new MediaType()
                                        .schema(
                                            new Schema<>()
                                                .type("object")
                                                .addProperty(
                                                    "file",
                                                    new Schema<>()
                                                        .type("string")
                                                        .format("binary")
                                                        .description("File to upload"))
                                                .addProperty(
                                                    "description",
                                                    new Schema<>()
                                                        .type("string")
                                                        .description(
                                                            "Optional file description"))))))

                // Common Error Response Schema
                .addSchemas(
                    "ErrorResponse",
                    new Schema<>()
                        .type("object")
                        .addProperty("timestamp", new Schema<>().type("string").format("date-time"))
                        .addProperty("status", new Schema<>().type("integer"))
                        .addProperty("error", new Schema<>().type("string"))
                        .addProperty("message", new Schema<>().type("string"))
                        .addProperty("path", new Schema<>().type("string")))

                // Success Response Schema
                .addSchemas(
                    "SuccessResponse",
                    new Schema<>()
                        .type("object")
                        .addProperty("success", new Schema<>().type("boolean"))
                        .addProperty("message", new Schema<>().type("string"))
                        .addProperty("data", new Schema<>().type("object"))))

        // Global Security Requirement (optional - can be overridden per endpoint)
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
        .tags(
            List.of(
                new Tag()
                    .name("Supplier Management")
                    .description("Manage suppliers who provide raw ingredients"),
                new Tag()
                    .name("Store Management")
                    .description(
                        "Manage stores (Warehouse, Manufacturing Unit, Retail Store, Distribution Center)"),
                new Tag()
                    .name("Purchase Order Management")
                    .description("Order raw ingredients from suppliers with receiving workflow"),
                new Tag()
                    .name("Ingredient Management")
                    .description("Manage raw materials and ingredient inventory"),
                new Tag()
                    .name("Recipe Management")
                    .description("Define pharmaceutical recipes/formulations for manufacturing"),
                new Tag()
                    .name("Production Management")
                    .description("Track production batches and material consumption"),
                new Tag()
                    .name("Medicine Management")
                    .description("Manage finished medicine products catalog"),
                new Tag()
                    .name("Inventory Management")
                    .description("Track medicine stock per store with batch and expiry tracking"),
                new Tag()
                    .name("Stock Transfer Management")
                    .description("Transfer stock between stores with approval workflow"),
                new Tag()
                    .name("Customer Management")
                    .description("Manage customer database with medical history"),
                new Tag()
                    .name("Prescription Management")
                    .description("Manage medical prescriptions and prescription items"),
                new Tag()
                    .name("Sales Management")
                    .description("Point of sale transactions with FIFO inventory management"),
                new Tag()
                    .name("Alert Management")
                    .description("Monitor low stock levels and expiry date alerts"),
                new Tag()
                    .name("User Management")
                    .description("Manage system users and authentication"),
                new Tag()
                    .name("Reports & Analytics")
                    .description("Manufacturing reports, sales reports, and analytics")));
  }
}
