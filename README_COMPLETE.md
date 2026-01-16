# 🏥 Pharmacy Management System - Complete Edition

[![Status](https://img.shields.io/badge/Status-Production%20Ready-success)](https://github.com)
[![API Version](https://img.shields.io/badge/API-v1-blue)](http://localhost:8080/swagger-ui.html)
[![Endpoints](https://img.shields.io/badge/Endpoints-95+-green)](http://localhost:8080/api-docs)
[![Documentation](https://img.shields.io/badge/Documentation-Complete-brightgreen)](./COMPLETE_SYSTEM_OVERVIEW.md)

A comprehensive **end-to-end pharmacy management system** built with Spring Boot, featuring complete business flow from suppliers to customers, with API versioning, Swagger documentation, and file processing capabilities.

---

## 🌟 Key Features

- ✅ **Complete Business Flow**: Suppliers → Manufacturing → Distribution → Sales
- ✅ **16 Integrated Modules**: All aspects of pharmacy operations
- ✅ **95+ REST APIs**: Fully documented and versioned
- ✅ **FIFO Inventory**: First In, First Out stock management
- ✅ **Multi-Store Support**: Warehouse, Manufacturing, Retail, Distribution
- ✅ **Batch Tracking**: Complete traceability with expiry dates
- ✅ **API Versioning**: URL path versioning (v1, v2, etc.)
- ✅ **Swagger UI**: Interactive API documentation and testing
- ✅ **File Processing**: Upload/download documents, images, data files
- ✅ **Security**: JWT Bearer token and API Key authentication
- ✅ **Alerts**: Automated low stock and expiry monitoring
- ✅ **GMP Compliant**: Manufacturing follows Good Manufacturing Practice

---

## 🚀 Quick Start

### Prerequisites

- Java 17+
- Maven 3.6+

### Run the Application

```bash
# Using Maven Wrapper (Recommended)
./mvnw.cmd spring-boot:run

# Or using Maven
mvn spring-boot:run
```

### Access the System

|       Resource        |                  URL                   |
|-----------------------|----------------------------------------|
| **Swagger UI**        | http://localhost:8080/swagger-ui.html  |
| **API Documentation** | http://localhost:8080/api-docs         |
| **API Base (v1)**     | http://localhost:8080/api/v1           |
| **File Upload**       | http://localhost:8080/api/files/upload |

---

## 📦 System Modules

### Core Business Modules (13)

1. **Supplier Management** - Manage ingredient suppliers
2. **Store Management** - Multi-store operations (Warehouse, Manufacturing, Retail, Distribution)
3. **Purchase Order Management** - Procurement with receiving workflow
4. **Ingredient Management** - Raw material inventory with batch tracking
5. **Recipe Management** - Define medicine formulations
6. **Production/Manufacturing** - Batch production with quality control
7. **Medicine Management** - Finished product catalog
8. **Inventory Management** - Stock tracking with FIFO and batch tracking
9. **Stock Transfer Management** - Inter-store transfers with approval workflow
10. **Customer Management** - Customer database with medical history
11. **Prescription Management** - Medical prescriptions
12. **Sales Management** - Point of sale with automatic inventory updates
13. **Alert Management** - Low stock and expiry alerts

### Enhanced Features (3)

14. **API Versioning** - URL path versioning for backward compatibility
15. **Swagger OpenAPI** - Complete API documentation with security schemes
16. **File Management** - Document upload/download/management

---

## 🔄 Business Flow

```
SUPPLIERS
   ↓ [Purchase Orders]
INGREDIENTS (Raw Materials)
   ↓ [Stock Transfer to Manufacturing]
MANUFACTURING UNIT
   ↓ [Production using Recipes]
FINISHED MEDICINES
   ↓ [Stock Transfer to Warehouse]
WAREHOUSE
   ↓ [Stock Transfer to Stores]
RETAIL STORES
   ↓ [Sales Transactions]
CUSTOMERS
```

---

## 🔌 API Examples

### Create a Supplier

```bash
curl -X POST http://localhost:8080/api/v1/suppliers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "ABC Pharmaceuticals",
    "code": "SUP001",
    "email": "contact@abc.com",
    "phone": "123-456-7890",
    "status": "ACTIVE"
  }'
```

### Create a Sale

```bash
curl -X POST http://localhost:8080/api/v1/sales \
  -H "Content-Type: application/json" \
  -d '{
    "storeId": 1,
    "customerId": 1,
    "pharmacistId": 1,
    "paymentMethod": "CASH",
    "items": [{
      "medicineId": 1,
      "quantity": 10,
      "unitPrice": 5.00
    }]
  }'
```

### Upload a File

```bash
curl -X POST http://localhost:8080/api/files/upload \
  -F "file=@prescription.pdf" \
  -F "category=prescription"
```

---

## 📖 Documentation

### Main Documentation Files

- **[COMPLETE_SYSTEM_OVERVIEW.md](./COMPLETE_SYSTEM_OVERVIEW.md)** - Full system overview
- **[QUICK_REFERENCE.md](./QUICK_REFERENCE.md)** - Quick reference card
- **[API_VERSIONING_SWAGGER_GUIDE.md](./API_VERSIONING_SWAGGER_GUIDE.md)** - Swagger & versioning guide
- **[SYSTEM_DIAGRAMS.md](./SYSTEM_DIAGRAMS.md)** - Architecture diagrams
- **[COMPLETE_PHARMACY_FLOW.md](./COMPLETE_PHARMACY_FLOW.md)** - Business flow details

### Additional Documentation

- **[IMPLEMENTATION_COMPLETE_FINAL.md](./IMPLEMENTATION_COMPLETE_FINAL.md)** - Implementation details
- **[FINAL_IMPLEMENTATION_SUMMARY.md](./FINAL_IMPLEMENTATION_SUMMARY.md)** - Summary document

---

## 🔐 Security

### Authentication Methods

#### 1. JWT Bearer Token

```http
Authorization: Bearer <your-jwt-token>
```

#### 2. API Key

```http
X-API-KEY: <your-api-key>
```

### Using Swagger UI

1. Open http://localhost:8080/swagger-ui.html
2. Click the **"Authorize"** 🔒 button
3. Enter your JWT token or API key
4. Click **"Authorize"**
5. All subsequent requests will include authentication

---

## 📁 File Upload Features

### Supported File Types

- **Images**: JPG, JPEG, PNG, GIF
- **Documents**: PDF, DOC, DOCX
- **Data Files**: XLSX, XLS, CSV

### File Categories

- `prescription` - Prescription documents
- `invoice` - Sales invoices
- `certificate` - Quality certificates
- `report` - Manufacturing reports
- `general` - General documents

### File Size Limits

- **Maximum file size**: 10MB per file
- **Maximum request size**: 20MB total

---

## ⚙️ Configuration

### Key Settings (application.properties)

```properties
# API Version
api.version=v1

# Database (H2 In-Memory)
spring.datasource.url=jdbc:h2:mem:pharmacydb
spring.datasource.username=sa
spring.datasource.password=sa

# File Upload
spring.servlet.multipart.max-file-size=10MB
file.upload-dir=uploads

# Swagger
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.enabled=true
```

---

## 📊 System Statistics

|      Metric       | Count |
|-------------------|-------|
| **Modules**       | 16    |
| **API Endpoints** | 95+   |
| **Entities**      | 22+   |
| **Services**      | 17+   |
| **Controllers**   | 16+   |
| **DTOs**          | 20+   |
| **Repositories**  | 18+   |

---

## 🎯 Core Features

### Stock Management

- ✅ **FIFO Inventory** - First In, First Out for sales
- ✅ **Multi-Store** - Independent stock per store
- ✅ **Batch Tracking** - Track batches with expiry dates
- ✅ **Real-time Updates** - Instant stock updates on transactions

### Sales Features

- ✅ **Stock Validation** - Prevents overselling
- ✅ **Multiple Payment Methods** - Cash, Card, Insurance, UPI, etc.
- ✅ **Invoice Generation** - Automatic invoice numbers
- ✅ **Cancel & Return** - Full cancellation and return support with inventory restoration

### Manufacturing Features

- ✅ **Recipe-based Production** - Define ingredient requirements
- ✅ **Batch Production** - Track production batches
- ✅ **Quality Control** - Quality check workflow
- ✅ **Material Consumption** - Automatic ingredient deduction
- ✅ **GMP Compliance** - Good Manufacturing Practice standards

### Transfer Features

- ✅ **Approval Workflow** - Request → Approve → Dispatch → Receive
- ✅ **Partial Receives** - Support for partial deliveries
- ✅ **Status Tracking** - Track transfer status in real-time

---

## 🧪 Testing with Swagger UI

1. **Start the Application**

   ```bash
   ./mvnw.cmd spring-boot:run
   ```
2. **Open Swagger UI**

   ```
   http://localhost:8080/swagger-ui.html
   ```
3. **Explore APIs**
   - Browse by module tags
   - View request/response schemas
   - See example values
4. **Test Endpoints**
   - Click "Try it out"
   - Fill in parameters
   - Click "Execute"
   - View results

---

## 🔧 Technology Stack

- **Framework**: Spring Boot 3.x
- **Database**: H2 (In-Memory) / PostgreSQL (Production)
- **ORM**: Spring Data JPA / Hibernate
- **API Documentation**: SpringDoc OpenAPI 3 (Swagger)
- **Security**: Spring Security with JWT
- **Build Tool**: Maven
- **Java Version**: 17+

---

## 📝 API Versioning

All endpoints are versioned using URL path versioning:

```
/api/v1/suppliers
/api/v1/sales
/api/v1/medicines
/api/v1/stock-transfers
```

Future versions can be added:

```
/api/v2/suppliers
/api/v3/suppliers
```

---

## 🎨 Swagger UI Features

- ✅ **Interactive Testing** - Test APIs directly from browser
- ✅ **Authorization** - Add token once, applies to all requests
- ✅ **Request Duration** - See API performance
- ✅ **Filter** - Search for specific endpoints
- ✅ **Organized Tags** - Browse by business module
- ✅ **Schema Models** - View DTO structures
- ✅ **Example Values** - Pre-filled request examples
- ✅ **Response Codes** - See all possible responses
- ✅ **Curl Commands** - Auto-generated curl examples
- ✅ **Persistent Auth** - Authorization persists across page reloads

---

## 📞 Support

For questions or issues:
- Check the [documentation files](./COMPLETE_SYSTEM_OVERVIEW.md)
- Review the [Swagger UI](http://localhost:8080/swagger-ui.html)
- Refer to the [Quick Reference](./QUICK_REFERENCE.md)

---

## ✅ Production Readiness

This system is **production-ready** with:

- ✅ Complete business logic
- ✅ Comprehensive API documentation
- ✅ API versioning for backward compatibility
- ✅ File upload/download capabilities
- ✅ Security implementation
- ✅ Error handling
- ✅ Transaction management
- ✅ Data validation
- ✅ Audit trails
- ✅ Interactive testing interface

---

## 📜 License

Proprietary - All rights reserved

---

## 🎉 Status: COMPLETE

**All features implemented and tested!**

- ✅ 16 Modules fully implemented
- ✅ 95+ API endpoints documented
- ✅ API versioning (v1) ready
- ✅ Swagger UI configured
- ✅ File processing functional
- ✅ Complete documentation
- ✅ Production ready

---

**Version**: 1.0.0  
**Last Updated**: January 16, 2026  
**Status**: Production Ready ✅

---

## 🚀 Get Started Now!

```bash
# 1. Clone or navigate to project directory
cd pharmacy

# 2. Start the application
./mvnw.cmd spring-boot:run

# 3. Open Swagger UI
# http://localhost:8080/swagger-ui.html

# 4. Start testing your APIs!
```

**Happy Coding! 🎉**
