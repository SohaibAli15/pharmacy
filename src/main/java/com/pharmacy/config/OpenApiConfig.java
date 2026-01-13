package com.pharmacy.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI pharmacyOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Pharmacy Management System - Manufacturing Module API")
                        .description("""
                                **Comprehensive API for Pharmaceutical Manufacturing Management**
                                
                                This API provides complete manufacturing module functionality including:
                                - Recipe/Formulation Management
                                - Production Batch Tracking
                                - Material Consumption Analysis
                                - Manufacturing Reports & Analytics
                                - GMP Compliance Features
                                
                                **Key Features:**
                                - Multi-ingredient recipe support with wastage tracking
                                - Automatic cost calculations
                                - Production finalization workflow
                                - Inventory integration
                                - Comprehensive reporting
                                - Full audit trail
                                - Lot number traceability
                                
                                **Standards Compliance:**
                                - Good Manufacturing Practice (GMP)
                                - Full traceability
                                - Data integrity controls
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Butt Brothers Pharmacy")
                                .email("support@buttbrothers.com")
                                .url("https://buttbrothers.com"))
                        .license(new License()
                                .name("Proprietary")
                                .url("https://buttbrothers.com/license")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development Server"),
                        new Server()
                                .url("https://api.buttbrothers.com")
                                .description("Production Server")
                ))
                .tags(List.of(
                        new Tag()
                                .name("Recipe Management")
                                .description("APIs for managing pharmaceutical recipes/formulations"),
                        new Tag()
                                .name("Production Batches")
                                .description("APIs for tracking production runs and material consumption"),
                        new Tag()
                                .name("Manufacturing Reports")
                                .description("Analytics and reporting endpoints for manufacturing operations"),
                        new Tag()
                                .name("Ingredients")
                                .description("APIs for ingredient/raw material management"),
                        new Tag()
                                .name("Medicines")
                                .description("APIs for finished product management"),
                        new Tag()
                                .name("Alerts")
                                .description("APIs for stock level alerts and notifications")
                ));
    }
}

