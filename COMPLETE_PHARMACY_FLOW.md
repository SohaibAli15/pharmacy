# Complete Pharmacy Management System Flow

## System Overview

A comprehensive pharmacy management system covering the entire lifecycle from suppliers to customers.

## Complete Business Flow

### 1. SUPPLIER MANAGEMENT (Entry Point)

**Purpose**: Manage suppliers who provide raw ingredients

**Entities**: `Supplier`

**Flow**:
- Create/Update suppliers with contact details, payment terms, status
- Search and filter suppliers by name, status
- Track supplier performance and payment terms

**Endpoints**:
- `POST /api/suppliers` - Create supplier
- `PUT /api/suppliers/{id}` - Update supplier
- `GET /api/suppliers` - Get all suppliers
- `GET /api/suppliers/{id}` - Get supplier by ID
- `GET /api/suppliers/code/{code}` - Get supplier by code
- `GET /api/suppliers/status/{status}` - Get by status
- `GET /api/suppliers/search?name=` - Search by name
- `DELETE /api/suppliers/{id}` - Delete supplier

---

### 2. STORE MANAGEMENT

**Purpose**: Manage different types of stores (warehouses, retail stores, manufacturing units, distribution centers)

**Entities**: `Store`

**Store Types**:
- WAREHOUSE - Main storage facility
- RETAIL_STORE - Customer-facing stores
- DISTRIBUTION_CENTER - Distribution hubs
- MANUFACTURING_UNIT - Production facilities

**Flow**:
- Create stores with location details and type
- Assign managers to stores
- Track store status (ACTIVE, INACTIVE, MAINTENANCE)

**Endpoints**:
- `POST /api/stores` - Create store
- `PUT /api/stores/{id}` - Update store
- `GET /api/stores` - Get all stores
- `GET /api/stores/{id}` - Get store by ID
- `GET /api/stores/code/{code}` - Get store by code
- `GET /api/stores/type/{type}` - Get by type
- `GET /api/stores/status/{status}` - Get by status
- `DELETE /api/stores/{id}` - Delete store

---

### 3. PURCHASE ORDER MANAGEMENT (Procurement)

**Purpose**: Order raw ingredients from suppliers

**Entities**: `PurchaseOrder`, `PurchaseOrderItem`

**Flow**:
1. **Create Purchase Order**:
- Select supplier
- Select destination store (usually WAREHOUSE or MANUFACTURING_UNIT)
- Add ingredients with quantities and unit prices
- System calculates subtotal, tax, shipping, total
- Status: DRAFT

2. **Approve Purchase Order**:
   - Review order details
   - Update status: DRAFT → PENDING → APPROVED → ORDERED
3. **Receive Items**:
   - Supplier delivers items
   - Record received quantities (may be partial)
   - System updates ingredient stock in the receiving store
   - Status: ORDERED → PARTIALLY_RECEIVED → RECEIVED

**Endpoints**:
- `POST /api/purchase-orders` - Create purchase order
- `PUT /api/purchase-orders/{id}/status?status=` - Update status
- `POST /api/purchase-orders/{id}/receive` - Receive items
- `GET /api/purchase-orders` - Get all orders
- `GET /api/purchase-orders/{id}` - Get order by ID
- `GET /api/purchase-orders/status/{status}` - Get by status
- `GET /api/purchase-orders/supplier/{supplierId}` - Get by supplier

**Stock Impact**: Increases `IngredientStock` in the receiving store

---

### 4. MANUFACTURING/PRODUCTION (Create Products)

**Purpose**: Manufacture medicines from raw ingredients using recipes

**Entities**: `Recipe`, `RecipeIngredient`, `ProductionBatch`, `ProductionBatchMaterial`

**Flow**:
1. **Create Recipes**:
- Define medicine to be produced
- List required ingredients with quantities
- Set batch size and production time

2. **Plan Production Batch**:
   - Select recipe
   - Select manufacturing store (type: MANUFACTURING_UNIT)
   - Define quantity to produce
   - System checks ingredient availability
   - Status: PLANNED
3. **Start Production**:
   - Reserve ingredients from ingredient stock
   - Status: PLANNED → IN_PROGRESS
4. **Complete Production**:
   - Finished medicines are added to inventory stock
   - Ingredient stock is deducted
   - Generate batch number, manufacturing date, expiry date
   - Status: IN_PROGRESS → COMPLETED
   - Add produced quantity to `InventoryStock`
5. **Quality Check**:
   - Inspect produced medicines
   - Status: COMPLETED → QUALITY_CHECKED or REJECTED

**Endpoints**:
- `POST /api/recipes` - Create recipe
- `GET /api/recipes` - Get all recipes
- `GET /api/recipes/{id}` - Get recipe by ID
- `POST /api/production-batches` - Plan production batch
- `POST /api/production-batches/{id}/start` - Start production
- `POST /api/production-batches/{id}/complete` - Complete production
- `GET /api/production-batches` - Get all batches
- `GET /api/production-batches/status/{status}` - Get by status

**Stock Impact**:
- Decreases `IngredientStock`
- Increases `InventoryStock` (medicine stock)

---

### 5. STOCK TRANSFER (Inter-Store Movement)

**Purpose**: Transfer medicines/ingredients between stores

**Entities**: `StockTransfer`, `StockTransferItem`

**Transfer Scenarios**:
- Warehouse → Retail Store (replenishment)
- Manufacturing Unit → Warehouse (after production)
- Warehouse → Distribution Center
- Distribution Center → Multiple Retail Stores

**Flow**:
1. **Create Transfer Request**:
- Select source store (fromStore)
- Select destination store (toStore)
- Add items (medicines or ingredients)
- Specify requested quantities
- Status: DRAFT

2. **Submit for Approval**:
   - Status: DRAFT → PENDING_APPROVAL
3. **Approve Transfer**:
   - Approver reviews request
   - Can modify approved quantities
   - Status: PENDING_APPROVAL → APPROVED or REJECTED
4. **Dispatch Transfer**:
   - Items are packed and shipped
   - Stock is deducted from source store
   - Status: APPROVED → IN_TRANSIT
5. **Receive Transfer**:
   - Destination store receives items
   - Record received quantities (may differ from approved)
   - Stock is added to destination store
   - Status: IN_TRANSIT → RECEIVED

**Endpoints**:
- `POST /api/stock-transfers` - Create transfer request
- `POST /api/stock-transfers/{id}/approve?approvedById=` - Approve transfer
- `POST /api/stock-transfers/{id}/dispatch` - Dispatch transfer
- `POST /api/stock-transfers/{id}/receive?receivedById=` - Receive transfer
- `GET /api/stock-transfers` - Get all transfers
- `GET /api/stock-transfers/{id}` - Get transfer by ID
- `GET /api/stock-transfers/store/{storeId}` - Get by store

**Stock Impact**:
- Decreases stock in source store
- Increases stock in destination store

---

### 6. CUSTOMER MANAGEMENT

**Purpose**: Manage customer information

**Entities**: `Customer`

**Customer Types**:
- REGULAR - Walk-in customers
- MEMBER - Registered members
- CORPORATE - Corporate clients
- INSURANCE - Insurance-linked customers

**Flow**:
- Create customer profiles with contact details
- Record medical history, allergies, conditions
- Store insurance information
- Track customer purchases

**Endpoints**:
- `POST /api/customers` - Create customer
- `PUT /api/customers/{id}` - Update customer
- `GET /api/customers` - Get all customers
- `GET /api/customers/{id}` - Get customer by ID
- `GET /api/customers/code/{code}` - Get customer by code
- `GET /api/customers/search?searchTerm=` - Search customers
- `DELETE /api/customers/{id}` - Delete customer

---

### 7. SALES MANAGEMENT (Final Step)

**Purpose**: Sell medicines to customers

**Entities**: `Sale`, `SaleItem`

**Flow**:
1. **Create Sale**:
- Select retail store
- Select customer (optional for walk-in)
- Select pharmacist (logged-in user)
- Add medicines with quantities
- System checks stock availability in the store
- Calculate subtotal, discount, tax, total
- Select payment method

2. **Process Payment**:
   - Accept payment (CASH, CARD, INSURANCE, UPI, etc.)
   - Generate invoice number
   - Status: COMPLETED
3. **Update Inventory**:
   - Deduct sold quantities from `InventoryStock` of the store
   - Generate alerts if stock falls below threshold
4. **Prescription Management** (Optional):
   - Link sale to prescription if required
   - Validate prescription before sale

**Endpoints**:
- `POST /api/sales` - Create sale
- `GET /api/sales` - Get all sales
- `GET /api/sales/{id}` - Get sale by ID
- `GET /api/sales/invoice/{invoiceNumber}` - Get by invoice
- `GET /api/sales/store/{storeId}` - Get sales by store
- `GET /api/sales/customer/{customerId}` - Get customer sales
- `GET /api/sales/date-range?startDate=&endDate=` - Get sales by date range
- `POST /api/sales/{id}/cancel` - Cancel sale
- `POST /api/sales/{id}/return` - Return sale

**Stock Impact**: Decreases `InventoryStock` in the selling store

---

## Stock Management

### Ingredient Stock

- Managed per store
- Tracks raw ingredients for manufacturing
- Updated by: Purchase Orders (increase), Production Batches (decrease)

### Inventory Stock (Medicine Stock)

- Managed per store
- Tracks finished medicines ready for sale
- Updated by: Production Batches (increase), Stock Transfers (increase/decrease), Sales (decrease)

---

## Alert System

**Purpose**: Notify when stock levels are low

**Entities**: `Alert`

**Alert Types**:
- LOW_STOCK - Stock below minimum threshold
- EXPIRED - Items past expiry date
- EXPIRING_SOON - Items expiring within 30 days

**Flow**:
- System automatically generates alerts
- Threshold alerts trigger when stock falls below minimum level
- Expiry alerts check dates daily

**Endpoints**:
- `GET /api/alerts` - Get all alerts
- `GET /api/alerts/type/{type}` - Get alerts by type
- `GET /api/alerts/store/{storeId}` - Get alerts by store
- `POST /api/alerts/{id}/resolve` - Resolve alert

---

## Complete Workflow Example

### Scenario: From Supplier to Customer Sale

1. **Setup Phase**:
   - Create Supplier: "ABC Pharmaceuticals"
   - Create Stores:
     - Warehouse: "Main Warehouse"
     - Manufacturing: "Production Unit 1"
     - Retail Store: "City Pharmacy"
2. **Procurement Phase**:
   - Create Purchase Order from "ABC Pharmaceuticals" to "Main Warehouse"
   - Order ingredients: Paracetamol Powder, Binding Agent, etc.
   - Receive items → Ingredient stock updated in Main Warehouse
3. **Manufacturing Phase**:
   - Transfer ingredients from Warehouse to Production Unit
   - Create Recipe for "Paracetamol 500mg Tablet"
   - Create Production Batch in Production Unit
   - Start Production → Ingredients consumed
   - Complete Production → Medicine stock created in Production Unit
4. **Distribution Phase**:
   - Transfer finished medicines from Production Unit to Main Warehouse
   - Transfer medicines from Main Warehouse to City Pharmacy (retail store)
5. **Sales Phase**:
   - Customer walks into City Pharmacy
   - Create customer record
   - Create sale transaction
   - Select medicines and quantities
   - Process payment
   - Generate invoice
   - Inventory stock reduced in City Pharmacy
6. **Monitoring Phase**:
   - System generates alert if stock in City Pharmacy falls below threshold
   - Create new stock transfer request from Warehouse to City Pharmacy
   - Or create new production batch if warehouse stock is low
   - Or create new purchase order if ingredient stock is low

---

## Key Reports

1. **Inventory Reports**:
   - Current stock by store
   - Stock valuation
   - Low stock items
2. **Sales Reports**:
   - Daily/Monthly sales by store
   - Top-selling medicines
   - Customer purchase history
3. **Manufacturing Reports**:
   - Production efficiency
   - Ingredient consumption
   - Batch quality metrics
4. **Financial Reports**:
   - Purchase order summary
   - Sales revenue
   - Profit margins

---

## Implementation Status

### ✅ Completed Modules:

1. Supplier Management (Entity, DTO, Service, Controller)
2. Store Management (Entity, DTO, Service, Controller)
3. Purchase Order Management (Entity, DTO, Service, Controller)
4. Stock Transfer Management (Entity, DTO, Service, Controller)
5. Customer Management (Entity, DTO, Service, Controller)
6. Production/Manufacturing (Entity, DTO, Service, Controller)
7. Recipe Management (Entity, DTO, Service, Controller)
8. Sales Management (Entity, Service, Controller)
9. Alert System (Entity, DTO, Service, Controller)

### 🔨 To Be Enhanced:

1. SaleDto creation
2. SaleItemDto creation
3. PrescriptionDto creation
4. Complete Sale Service implementation
5. Complete Sale Controller implementation
6. Advanced reporting endpoints
7. Dashboard APIs

---

## Next Steps

1. Create missing DTOs (SaleDto, SaleItemDto, PrescriptionDto)
2. Complete Sale Service and Controller
3. Add advanced search and filtering
4. Implement reporting APIs
5. Add validation and error handling
6. Implement security and role-based access
7. Add audit logging

