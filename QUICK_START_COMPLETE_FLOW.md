# 🚀 Quick Start Guide - Pharmacy Management System

## Overview

This guide will help you get started with the complete pharmacy management system implementation.

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.8+
- PostgreSQL 14+ (or H2 for development)
- Postman (for API testing)
- IDE (IntelliJ IDEA / Eclipse / VS Code)

## 🏗️ Project Structure

```
pharmacy/
├── Entity Layer (✅ Complete)
│   ├── Supplier, Store, Customer
│   ├── PurchaseOrder, PurchaseOrderItem
│   ├── IngredientStock, InventoryStock
│   ├── StockTransfer, StockTransferItem
│   └── Enhanced Sale with store support
│
├── Repository Layer (✅ Complete)
│   └── All JPA repositories with custom queries
│
├── DTO Layer (✅ Complete)
│   └── All data transfer objects
│
├── Service Layer (✅ Complete)
│   ├── Business logic with transactions
│   └── Automatic stock management
│
└── Controller Layer (✅ Complete)
    └── REST APIs with Swagger documentation
```

## 🔧 Setup Steps

### Step 1: Configure Database

**Option A: PostgreSQL (Production)**
Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/pharmacy_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

**Option B: H2 (Development/Testing)**
Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:h2:mem:pharmacy_db
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.hibernate.ddl-auto=create-drop
spring.h2.console.enabled=true
```

### Step 2: Build the Project

```bash
cd "C:\Java DSA\Shabaz Work\pharmacy"
mvn clean install
```

### Step 3: Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Step 4: Access Swagger UI

Open your browser and navigate to:

```
http://localhost:8080/swagger-ui.html
```

## 📝 Complete Workflow Test

### Phase 1: Initial Setup (10 minutes)

**1. Create Users**
- Create Admin user
- Create Store Managers (3)
- Create Pharmacists (2)

**2. Create Suppliers** (3-5 suppliers)

```json
POST /api/suppliers
{
  "name": "ABC Pharmaceuticals Ltd",
  "code": "SUP-001",
  "contactPerson": "John Doe",
  "email": "contact@abcpharma.com",
  "phone": "+1-555-0100",
  "address": "123 Industrial Ave",
  "city": "Mumbai",
  "state": "Maharashtra",
  "country": "India",
  "zipCode": "400001",
  "status": "ACTIVE",
  "paymentTermsDays": 30
}
```

**3. Create Stores**

```json
// Main Warehouse
POST /api/stores
{
  "code": "WH-MAIN-001",
  "name": "Main Warehouse",
  "type": "WAREHOUSE",
  "address": "Plot 45, Industrial Area",
  "city": "Mumbai",
  "status": "ACTIVE"
}

// Manufacturing Unit
POST /api/stores
{
  "code": "MFG-001",
  "name": "Manufacturing Unit 1",
  "type": "MANUFACTURING_UNIT",
  "address": "Plot 67, Industrial Area",
  "city": "Mumbai",
  "status": "ACTIVE"
}

// Retail Store
POST /api/stores
{
  "code": "RET-001",
  "name": "Pharmacy Retail - Downtown",
  "type": "RETAIL_STORE",
  "address": "123 Main Street",
  "city": "Mumbai",
  "status": "ACTIVE"
}
```

**4. Create Ingredients** (Use existing Ingredient endpoints)

**5. Create Medicines** (Use existing Medicine endpoints)

**6. Create Recipes** (Use existing Recipe endpoints)

**7. Create Customers**

```json
POST /api/customers
{
  "customerCode": "CUST-001",
  "firstName": "Rajesh",
  "lastName": "Kumar",
  "email": "rajesh.kumar@email.com",
  "phone": "+91-9876543210",
  "type": "REGULAR",
  "status": "ACTIVE"
}
```

### Phase 2: Procurement Workflow (15 minutes)

**Step 1: Create Purchase Order**

```json
POST /api/purchase-orders
{
  "supplierId": 1,
  "storeId": 1,  // Main Warehouse
  "createdById": 1,
  "orderDate": "2026-01-16",
  "expectedDeliveryDate": "2026-01-23",
  "subtotal": 50000.00,
  "taxAmount": 9000.00,
  "shippingCost": 500.00,
  "totalAmount": 59500.00,
  "items": [
    {
      "ingredientId": 1,
      "quantity": 500,
      "unitPrice": 50.00,
      "totalPrice": 25000.00
    }
  ]
}
```

**Step 2: Approve Purchase Order**

```
PUT /api/purchase-orders/1/status?status=APPROVED
PUT /api/purchase-orders/1/status?status=ORDERED
```

**Step 3: Receive Items**

```json
POST /api/purchase-orders/1/receive
[
  {
    "id": 1,
    "receivedQuantity": 500
  }
]
```

**Step 4: Verify Stock Created**

```
GET /api/ingredient-stock/store/1
```

✅ You should see ingredient stock with batch number automatically created

### Phase 3: Stock Transfer to Manufacturing (10 minutes)

**Step 1: Create Transfer Request**

```json
POST /api/stock-transfers
{
  "fromStoreId": 1,  // From Warehouse
  "toStoreId": 2,    // To Manufacturing
  "requestedById": 2,
  "transferDate": "2026-01-17",
  "expectedArrivalDate": "2026-01-18",
  "type": "INGREDIENT",
  "items": [
    {
      "ingredientId": 1,
      "batchNumber": "BATCH-xxxx",  // Use actual batch from previous step
      "requestedQuantity": 200
    }
  ]
}
```

**Step 2: Approve Transfer**

```
POST /api/stock-transfers/1/approve?approvedById=1
```

**Step 3: Dispatch Transfer** (Deducts from warehouse)

```
POST /api/stock-transfers/1/dispatch
```

**Step 4: Receive Transfer** (Adds to manufacturing)

```json
POST /api/stock-transfers/1/receive?receivedById=2
[
  {
    "id": 1,
    "receivedQuantity": 200
  }
]
```

**Step 5: Verify Stock in Both Stores**

```
GET /api/ingredient-stock/store/1  // Should show reduced quantity
GET /api/ingredient-stock/store/2  // Should show new stock
```

### Phase 4: Manufacturing (10 minutes)

**Step 1: Create Production Batch**

```json
POST /api/production-batches
{
  "recipeId": 1,
  "batchNumber": "MB-2026-001",
  "quantityProduced": 10000,
  "startDate": "2026-01-18",
  "supervisorId": 2,
  "materials": [
    {
      "ingredientId": 1,
      "quantityUsed": 100
    }
  ]
}
```

**Step 2: Complete Production**

```
PUT /api/production-batches/1/complete
```

**Step 3: Verify Inventory Stock Created**

```
GET /api/inventory-stock/store/2
```

✅ Medicine stock should be automatically created

### Phase 5: Stock Transfer to Retail (10 minutes)

**Step 1: Transfer Medicines to Retail**

```json
POST /api/stock-transfers
{
  "fromStoreId": 2,  // From Manufacturing
  "toStoreId": 3,    // To Retail Store
  "requestedById": 3,
  "transferDate": "2026-01-19",
  "expectedArrivalDate": "2026-01-20",
  "type": "MEDICINE",
  "items": [
    {
      "medicineId": 1,
      "batchNumber": "MB-2026-001",
      "requestedQuantity": 5000
    }
  ]
}
```

**Step 2: Follow Approve → Dispatch → Receive Workflow**

### Phase 6: Sales (5 minutes)

**Step 1: Create Sale**

```json
POST /api/sales
{
  "storeId": 3,  // Retail Store
  "customerId": 1,
  "pharmacistId": 4,
  "saleDate": "2026-01-20T10:30:00",
  "subtotal": 600.00,
  "discount": 60.00,
  "taxAmount": 97.20,
  "totalAmount": 637.20,
  "paymentMethod": "CASH",
  "status": "COMPLETED",
  "items": [
    {
      "medicineId": 1,
      "quantity": 50,
      "unitPrice": 12.00,
      "totalPrice": 600.00
    }
  ]
}
```

**Step 2: Verify Stock Deduction**

```
GET /api/inventory-stock/store/3
```

✅ Medicine quantity should be reduced by 50

## 📊 Testing Checklist

- [ ] Suppliers created and searchable
- [ ] All stores created (Warehouse, Manufacturing, Retail)
- [ ] Customers created
- [ ] Purchase order workflow (Create → Approve → Receive)
- [ ] Ingredient stock automatically created on PO receipt
- [ ] Stock transfer workflow (Request → Approve → Dispatch → Receive)
- [ ] Stock correctly deducted from source and added to destination
- [ ] Production batch creates inventory stock
- [ ] Sales correctly deduct inventory from retail store
- [ ] Low stock alerts working
- [ ] Expiring stock alerts working

## 🐛 Common Issues & Solutions

### Issue 1: Compilation Errors

**Solution:** Make sure all imports are correct. Run:

```bash
mvn clean compile
```

### Issue 2: Table Not Found

**Solution:** Ensure `spring.jpa.hibernate.ddl-auto=update` in application.properties

### Issue 3: Batch Number Not Found

**Solution:** When creating stock transfers, use actual batch numbers from:

```
GET /api/ingredient-stock/store/{storeId}
GET /api/inventory-stock/store/{storeId}
```

### Issue 4: Insufficient Stock Error

**Solution:** Check available stock before creating transfers:

```
GET /api/ingredient-stock/store/{storeId}/ingredient/{ingredientId}/total
```

## 📈 Next Steps

1. **Add Reporting:**
   - Sales by store
   - Top-selling medicines
   - Supplier performance
   - Stock movement reports
2. **Add Notifications:**
   - Low stock alerts
   - Expiring stock alerts
   - Purchase order approvals
   - Transfer approvals
3. **Add Dashboards:**
   - Store-wise inventory summary
   - Sales analytics
   - Purchase trends
   - Manufacturing efficiency
4. **Add Audit Trail:**
   - Track all stock movements
   - User action logs
   - Price change history

## 🎯 Success Indicators

After completing this guide, you should be able to:
- ✅ Order raw materials from suppliers
- ✅ Receive materials into warehouse
- ✅ Transfer materials between stores
- ✅ Manufacture medicines from raw materials
- ✅ Distribute medicines to retail stores
- ✅ Sell medicines to customers
- ✅ Track inventory across all locations
- ✅ Get alerts for low stock and expiring items

## 📚 Documentation

- **Complete Flow:** See `PHARMACY_MANAGEMENT_SYSTEM_FLOW.md`
- **API Collection:** Import `Pharmacy_Complete_System_Flow.postman_collection.json` into Postman
- **Swagger UI:** http://localhost:8080/swagger-ui.html

## 🆘 Support

If you encounter any issues:
1. Check the complete flow documentation
2. Verify all prerequisites are met
3. Review the error messages carefully
4. Check database connectivity
5. Ensure all foreign key references are correct

---

**Happy Testing! 🎉**

*Last Updated: January 16, 2026*

