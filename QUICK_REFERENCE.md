# 🚀 QUICK REFERENCE - Pharmacy Management System

## ⚡ Quick Start

```bash
# 1. Start Application
./mvnw.cmd spring-boot:run

# 2. Access Swagger UI
http://localhost:8080/swagger-ui.html

# 3. Start Testing!
```

---

## 🔗 Important URLs

|     Resource      |                  URL                   |
|-------------------|----------------------------------------|
| **Swagger UI**    | http://localhost:8080/swagger-ui.html  |
| **API Docs JSON** | http://localhost:8080/api-docs         |
| **API Base (v1)** | http://localhost:8080/api/v1           |
| **File Upload**   | http://localhost:8080/api/files/upload |

---

## 📋 All Modules (16 Total)

| #  |          Module           | Endpoints | Status |
|----|---------------------------|-----------|--------|
| 1  | Supplier Management       | 8         | ✅      |
| 2  | Store Management          | 8         | ✅      |
| 3  | Purchase Order Management | 7         | ✅      |
| 4  | Ingredient Management     | 8         | ✅      |
| 5  | Recipe Management         | 6         | ✅      |
| 6  | Production Management     | 7         | ✅      |
| 7  | Medicine Management       | 6         | ✅      |
| 8  | Inventory Management      | 6         | ✅      |
| 9  | Stock Transfer Management | 7         | ✅      |
| 10 | Customer Management       | 7         | ✅      |
| 11 | Prescription Management   | 7         | ✅      |
| 12 | Sales Management          | 10        | ✅      |
| 13 | Alert Management          | 4         | ✅      |
| 14 | User Management           | 4         | ✅      |
| 15 | **File Management**       | 5         | ✅ NEW  |
| 16 | **API Versioning**        | All       | ✅ NEW  |

**Total: 95+ API Endpoints**

---

## 🔄 Business Flow (One Line)

**SUPPLIERS** → **PURCHASE ORDERS** → **INGREDIENTS** → **MANUFACTURING** → **MEDICINES** → **STOCK TRANSFERS** → **STORES** → **SALES** → **CUSTOMERS**

---

## 🔌 Key Endpoints by Module

### Suppliers

```
POST   /api/v1/suppliers
GET    /api/v1/suppliers
GET    /api/v1/suppliers/{id}
PUT    /api/v1/suppliers/{id}
DELETE /api/v1/suppliers/{id}
```

### Sales

```
POST   /api/v1/sales
GET    /api/v1/sales
GET    /api/v1/sales/{id}
GET    /api/v1/sales/invoice/{invoiceNumber}
POST   /api/v1/sales/{id}/cancel
POST   /api/v1/sales/{id}/return
```

### Files

```
POST   /api/files/upload
POST   /api/files/upload-multiple
GET    /api/files/download/{category}/{filename}
DELETE /api/files/delete/{category}/{filename}
GET    /api/files/list/{category}
```

### Stock Transfers

```
POST   /api/v1/stock-transfers
POST   /api/v1/stock-transfers/{id}/approve
POST   /api/v1/stock-transfers/{id}/dispatch
POST   /api/v1/stock-transfers/{id}/receive
GET    /api/v1/stock-transfers
```

### Production

```
POST   /api/v1/production-batches
POST   /api/v1/production-batches/{id}/start
POST   /api/v1/production-batches/{id}/complete
GET    /api/v1/production-batches
```

---

## 📁 File Upload Examples

### Upload Prescription

```bash
curl -X POST http://localhost:8080/api/files/upload \
  -F "file=@prescription.pdf" \
  -F "category=prescription"
```

### Upload Invoice

```bash
curl -X POST http://localhost:8080/api/files/upload \
  -F "file=@invoice.pdf" \
  -F "category=invoice"
```

### Supported File Types

- **Images**: JPG, JPEG, PNG, GIF
- **Documents**: PDF, DOC, DOCX
- **Data**: XLSX, XLS, CSV

### File Categories

- `prescription`, `invoice`, `certificate`, `report`, `general`

---

## 🔐 Authentication

### JWT Bearer Token

```
Authorization: Bearer <your-jwt-token>
```

### API Key

```
X-API-KEY: <your-api-key>
```

### In Swagger UI

1. Click **"Authorize"** 🔒
2. Enter token
3. Click "Authorize"

---

## ⚙️ Configuration (application.properties)

```properties
# API Version
api.version=v1

# File Upload
spring.servlet.multipart.max-file-size=10MB
file.upload-dir=uploads

# Swagger
springdoc.swagger-ui.path=/swagger-ui.html
```

---

## 📊 System Stats

- **Modules**: 16
- **Endpoints**: 95+
- **Entities**: 22+
- **Services**: 17+
- **Controllers**: 16+
- **File Types**: 10+

---

## ✅ Key Features

- ✅ Complete business flow
- ✅ API versioning (v1)
- ✅ Swagger documentation
- ✅ File upload/download
- ✅ FIFO inventory
- ✅ Multi-store support
- ✅ Batch tracking
- ✅ Real-time stock updates
- ✅ Approval workflows
- ✅ Sale returns
- ✅ Alert system

---

## 🎯 New Features Added Today

1. ✅ **Enhanced Swagger OpenAPI**
   - Complete documentation
   - Security schemes
   - File upload schemas
   - Multiple servers
2. ✅ **API Versioning**
   - `/api/v1/*` prefix
   - Configurable version
   - Future-ready
3. ✅ **File Management**
   - Upload/download/delete
   - Multiple file types
   - Category organization
   - 10MB limit

---

## 📖 Documentation Files

1. `COMPLETE_SYSTEM_OVERVIEW.md` - Full overview
2. `API_VERSIONING_SWAGGER_GUIDE.md` - Swagger guide
3. `COMPLETE_PHARMACY_FLOW.md` - Business flow
4. `IMPLEMENTATION_COMPLETE_FINAL.md` - Implementation details
5. `QUICK_REFERENCE.md` - This file

---

## 🚀 Testing Quick Commands

```bash
# Get all suppliers
curl http://localhost:8080/api/v1/suppliers

# Get all sales
curl http://localhost:8080/api/v1/sales

# Upload file
curl -X POST http://localhost:8080/api/files/upload \
  -F "file=@test.pdf" \
  -F "category=general"

# List files
curl http://localhost:8080/api/files/list/general
```

---

## ✅ Status: PRODUCTION READY

**All features implemented and tested!**

- Code compiles successfully
- All endpoints documented
- Interactive testing available
- Complete documentation
- Ready for deployment

---

## 📞 Access Points

|    What     |                 Where                  |
|-------------|----------------------------------------|
| Swagger UI  | http://localhost:8080/swagger-ui.html  |
| API v1      | http://localhost:8080/api/v1           |
| File Upload | http://localhost:8080/api/files/upload |
| API Docs    | http://localhost:8080/api-docs         |

---

**🎉 System Complete & Ready to Use!**

*Version: 1.0.0 | Date: January 16, 2026*
