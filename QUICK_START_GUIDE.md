# Manufacturing Module - Quick Start Guide

## 🚀 Getting Started

This guide will help you quickly set up and test the Manufacturing Module APIs.

---

## Prerequisites

- ✅ Java 21+ installed
- ✅ PostgreSQL database running
- ✅ Maven (or use included mvnw)
- ✅ Postman or similar API testing tool

---

## Step 1: Database Setup

### Option A: PostgreSQL (Recommended)

```sql
-- Create database
CREATE DATABASE pharmacy_db;

-- Connect to database
\c pharmacy_db

-- Tables will be auto-created by Hibernate
```

### Option B: Update application.properties

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/pharmacy_db
spring.datasource.username=postgres
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## Step 2: Build and Run

### Build the application

```bash
# Windows
.\mvnw.cmd clean package -DskipTests

# Linux/Mac
./mvnw clean package -DskipTests
```

### Run the application

```bash
java -jar target/pharmacy-0.0.1-SNAPSHOT.jar
```

Or run directly:

```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

Application will start on: **http://localhost:8080**

---

## Step 3: Test the APIs

### Quick Health Check

```bash
curl http://localhost:8080/api/recipes
```

Expected: `[]` (empty array) or list of recipes

---

## Step 4: Create Your First Recipe

### 1. Create Ingredients First

```bash
POST http://localhost:8080/api/ingredients
Content-Type: application/json

{
  "name": "Bleach Card 270gm",
  "description": "Packaging material",
  "unit": "Packet",
  "currentStock": 5000,
  "thresholdLow": 100,
  "thresholdHigh": 10000,
  "costPerUnit": 4.64
}
```

### 2. Create a Recipe

```bash
POST http://localhost:8080/api/recipes
Content-Type: application/json

{
  "recipeCode": "0964",
  "name": "unprinted Unit Carton",
  "description": "Packaging material recipe",
  "category": "Packaging Material",
  "subCategory": null,
  "productId": null,
  "outputQuantity": 1237.500,
  "outputUnit": "Pc(s)",
  "fixedProductionCost": 0.00,
  "variableProductionCost": 0.00,
  "wastagePercent": 3.00,
  "instructions": "<p>Mix ingredients carefully</p>",
  "status": "ACTIVE",
  "ingredients": [
    {
      "ingredientId": 1,
      "quantityRequired": 1000,
      "unit": "Packet",
      "wastagePercent": 0.00
    }
  ]
}
```

**Response:** Recipe object with auto-calculated costs

---

## Step 5: Create a Production Batch

```bash
POST http://localhost:8080/api/production-batches
Content-Type: application/json

{
  "recipeId": 1,
  "productionDate": "2026-01-13T10:00:00",
  "businessLocation": "Butt Brothers",
  "quantityProduced": 1200,
  "unit": "Pc(s)",
  "status": "DRAFT",
  "lotNumber": "LOT-2026-001",
  "materialsConsumed": [
    {
      "ingredientId": 1,
      "quantityRequired": 1000,
      "quantityUsed": 1005,
      "unit": "Packet",
      "lotNumber": "ING-LOT-001"
    }
  ]
}
```

**Response:** Production batch with auto-generated reference number

---

## Step 6: Finalize Production Batch

```bash
POST http://localhost:8080/api/production-batches/1/finalize?finalizedBy=admin
```

**Effect:**
- ✅ Batch locked from edits
- ✅ Inventory automatically deducted
- ✅ Status changed to FINALIZED

---

## Common API Endpoints

### Recipe Management

```bash
# List all recipes
GET /api/recipes

# Search recipes
GET /api/recipes/search?q=cotton&category=Packaging Material

# Get recipe by ID
GET /api/recipes/1

# Get recipe by code
GET /api/recipes/code/0964

# Update recipe
PUT /api/recipes/1

# Delete recipe (soft delete)
DELETE /api/recipes/1

# Copy recipe
POST /api/recipes/1/copy?newRecipeCode=0965&newName=Copy of recipe

# Get statistics
GET /api/recipes/stats
```

### Production Batches

```bash
# List all batches
GET /api/production-batches

# Search with filters
GET /api/production-batches/search?businessLocation=Butt Brothers&startDate=2026-01-01T00:00:00&endDate=2026-01-31T23:59:59

# Get by ID
GET /api/production-batches/1

# Filter by recipe
GET /api/production-batches/recipe/1

# Filter by status
GET /api/production-batches/status/COMPLETED

# Get all locations
GET /api/production-batches/locations

# Unfinalize batch
POST /api/production-batches/1/unfinalize

# Get statistics
GET /api/production-batches/stats
```

### Manufacturing Reports

```bash
# Material consumption report
GET /api/manufacturing-reports/material-consumption?startDate=2026-01-01T00:00:00&endDate=2026-01-31T23:59:59

# Cost variance report
GET /api/manufacturing-reports/cost-variance?startDate=2026-01-01T00:00:00&endDate=2026-01-31T23:59:59

# Production efficiency
GET /api/manufacturing-reports/production-efficiency?startDate=2026-01-01T00:00:00&endDate=2026-01-31T23:59:59

# Profitability analysis
GET /api/manufacturing-reports/profitability?startDate=2026-01-01T00:00:00&endDate=2026-01-31T23:59:59

# Recipe performance
GET /api/manufacturing-reports/recipe-performance?startDate=2026-01-01T00:00:00&endDate=2026-01-31T23:59:59
```

---

## Postman Collection

### Import this JSON into Postman:

```json
{
  "info": {
    "name": "Pharmacy Manufacturing API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Recipes",
      "item": [
        {
          "name": "List All Recipes",
          "request": {
            "method": "GET",
            "url": "http://localhost:8080/api/recipes"
          }
        },
        {
          "name": "Create Recipe",
          "request": {
            "method": "POST",
            "header": [{"key": "Content-Type", "value": "application/json"}],
            "url": "http://localhost:8080/api/recipes",
            "body": {
              "mode": "raw",
              "raw": "{\n  \"recipeCode\": \"0964\",\n  \"name\": \"Test Recipe\",\n  \"category\": \"Packaging Material\",\n  \"outputQuantity\": 1000,\n  \"outputUnit\": \"Pc(s)\",\n  \"wastagePercent\": 3.00,\n  \"ingredients\": []\n}"
            }
          }
        }
      ]
    },
    {
      "name": "Production",
      "item": [
        {
          "name": "List Production Batches",
          "request": {
            "method": "GET",
            "url": "http://localhost:8080/api/production-batches"
          }
        },
        {
          "name": "Create Production Batch",
          "request": {
            "method": "POST",
            "header": [{"key": "Content-Type", "value": "application/json"}],
            "url": "http://localhost:8080/api/production-batches",
            "body": {
              "mode": "raw",
              "raw": "{\n  \"recipeId\": 1,\n  \"productionDate\": \"2026-01-13T10:00:00\",\n  \"businessLocation\": \"Butt Brothers\",\n  \"quantityProduced\": 1000,\n  \"unit\": \"Pc(s)\"\n}"
            }
          }
        }
      ]
    }
  ]
}
```

---

## Troubleshooting

### Issue: Port 8080 already in use

**Solution:** Change port in `application.properties`:

```properties
server.port=8081
```

### Issue: Database connection failed

**Solution:** Verify PostgreSQL is running:

```bash
# Check if PostgreSQL is running
sudo systemctl status postgresql

# Or on Windows, check Services
```

### Issue: Compilation errors

**Solution:** Ensure Lombok is installed in your IDE:
- IntelliJ IDEA: Install Lombok plugin
- Eclipse: Run `java -jar lombok.jar`
- VSCode: Install Lombok extension

### Issue: 404 Not Found

**Solution:** Verify the endpoint starts with `/api/`:

```
✅ Correct: http://localhost:8080/api/recipes
❌ Wrong:   http://localhost:8080/recipes
```

---

## Sample Data Script

Run this to populate sample data:

```bash
# Create ingredients
curl -X POST http://localhost:8080/api/ingredients \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Bleach Card 270gm",
    "unit": "Packet",
    "currentStock": 5000,
    "thresholdLow": 100,
    "thresholdHigh": 10000,
    "costPerUnit": 4.64
  }'

# Create recipe
curl -X POST http://localhost:8080/api/recipes \
  -H "Content-Type: application/json" \
  -d '{
    "recipeCode": "TEST01",
    "name": "Test Recipe",
    "category": "Test",
    "outputQuantity": 1000,
    "outputUnit": "Pc(s)",
    "ingredients": [
      {
        "ingredientId": 1,
        "quantityRequired": 100,
        "unit": "Packet"
      }
    ]
  }'

# Create production batch
curl -X POST http://localhost:8080/api/production-batches \
  -H "Content-Type: application/json" \
  -d '{
    "recipeId": 1,
    "productionDate": "2026-01-13T10:00:00",
    "businessLocation": "Main Factory",
    "quantityProduced": 950,
    "unit": "Pc(s)"
  }'
```

---

## Next Steps

1. ✅ **Explore the API Documentation**: See `MANUFACTURING_MODULE_API_DOCUMENTATION.md`
2. ✅ **Test all endpoints**: Use Postman collection
3. ✅ **Generate reports**: Try the reporting endpoints
4. ✅ **Integrate with frontend**: Use the provided API endpoints
5. ✅ **Add authentication**: Implement JWT/Spring Security

---

## Useful Commands

```bash
# View logs
tail -f logs/spring.log

# Check application health (add Spring Actuator)
curl http://localhost:8080/actuator/health

# Build without tests
./mvnw clean package -DskipTests

# Run with specific profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod

# View all API endpoints (add Spring Boot Actuator)
curl http://localhost:8080/actuator/mappings
```

---

## Support

For issues or questions:
- 📧 Check `IMPLEMENTATION_SUMMARY.md` for architecture details
- 📧 See `MANUFACTURING_MODULE_API_DOCUMENTATION.md` for complete API reference
- 📧 Review error logs in console output

---

**Happy Manufacturing! 🎉**

Version: 1.0.0  
Last Updated: January 13, 2026

