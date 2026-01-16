# 📮 Complete Postman Collection - Pharmacy Management System

## ✅ Collection Created Successfully!

**File**: `Pharmacy_Management_System_Complete_API.postman_collection.json`

---

## 📋 Collection Overview

### **15 Modules** - **80+ API Endpoints** - **Complete Coverage**

|             Module             | Endpoints |        Description         |
|--------------------------------|-----------|----------------------------|
| 🔐 Authentication              | 1         | JWT token management       |
| 🏢 Supplier Management         | 8         | Supplier CRUD operations   |
| 🏭 Store Management            | 8         | Multi-store operations     |
| 📦 Purchase Order Management   | 7         | Procurement workflow       |
| 🧪 Ingredient Management       | 2         | Raw material tracking      |
| 📋 Recipe Management           | 6         | Medicine formulations      |
| ⚗️ Production Batch Management | 7         | Manufacturing workflow     |
| 💊 Medicine Management         | 2         | Product catalog            |
| 📊 Inventory Stock Management  | 9         | Stock tracking & FIFO      |
| 🔄 Stock Transfer Management   | 7         | Inter-store transfers      |
| 👥 Customer Management         | 7         | Customer database          |
| 🩺 Prescription Management     | 7         | Medical prescriptions      |
| 💰 Sales Management            | 10        | Point of sale transactions |
| 🚨 Alert Management            | 2         | System alerts              |
| 👤 User Management             | 2         | User accounts              |
| 📁 File Upload Management      | 5         | Document management        |

**Total: 80+ API Endpoints** ✅

---

## 🚀 How to Import & Use

### Step 1: Import Collection

1. Open Postman
2. Click **"Import"** button
3. Select **"File"** tab
4. Choose: `Pharmacy_Management_System_Complete_API.postman_collection.json`
5. Click **"Import"**

### Step 2: Set Environment Variables

1. Click **"Environments"** (left sidebar)
2. Click **"Create Environment"**
3. Set variables:

   ```
   base_url = http://localhost:8080
   jwt_token = (leave empty initially)
   ```

### Step 3: Start Application

```bash
./mvnw.cmd spring-boot:run
```

### Step 4: Test Authentication (Optional)

1. Go to **"Authentication"** folder
2. Run **"Login"** request
3. Copy JWT token from response
4. Update `jwt_token` environment variable

### Step 5: Start Testing APIs!

- All requests are pre-configured with sample data
- Environment variables are automatically substituted
- JWT authentication is configured globally

---

## 📁 Collection Structure

### 🔐 Authentication

- **Login** - Get JWT token for authenticated requests

### 🏢 Supplier Management

- Create, read, update, delete suppliers
- Search by name, filter by status
- Get by code

### 🏭 Store Management

- Manage warehouses, manufacturing units, retail stores
- Filter by type and status
- Store capacity and manager info

### 📦 Purchase Order Management

- Create orders from suppliers
- Approve, receive, track deliveries
- Partial receiving support

### 🧪 Ingredient Management

- View all ingredients
- Low stock alerts

### 📋 Recipe Management

- Medicine formulations
- Ingredient quantities and wastage
- Batch size calculations

### ⚗️ Production Batch Management

- Start, complete, track production
- Quality control workflow
- Material consumption tracking

### 💊 Medicine Management

- Product catalog
- Search by name

### 📊 Inventory Stock Management

- Add/update stock with batch tracking
- FIFO inventory management
- Low stock and expiry alerts
- Stock adjustments

### 🔄 Stock Transfer Management

- Transfer between stores
- Approval workflow
- Dispatch and receive tracking

### 👥 Customer Management

- Customer database with medical history
- Search by name/phone
- Emergency contacts

### 🩺 Prescription Management

- Medical prescriptions
- Dosage instructions
- Status tracking

### 💰 Sales Management

- Point of sale transactions
- Automatic inventory updates
- Cancel/return with stock restoration
- Multiple payment methods

### 🚨 Alert Management

- Low stock alerts
- Expiry warnings
- System notifications

### 👤 User Management

- User accounts
- Role-based access

### 📁 File Upload Management

- Upload single/multiple files
- Download/delete files
- Category-based organization
- Support for prescriptions, invoices, certificates, reports

---

## 🎯 Key Features Demonstrated

### ✅ **Complete Business Flow**

```
SUPPLIERS → PURCHASE ORDERS → INGREDIENTS → RECIPES → PRODUCTION → MEDICINES → INVENTORY → SALES → CUSTOMERS
```

### ✅ **Advanced Features**

- **FIFO Inventory** - Automatic stock management
- **Batch Tracking** - Full traceability
- **Multi-Store** - Warehouse, Manufacturing, Retail
- **Approval Workflows** - Purchase orders, stock transfers
- **Quality Control** - Production batch validation
- **Medical Records** - Prescriptions and customer history
- **File Management** - Document upload/download
- **Alert System** - Automated notifications

### ✅ **API Best Practices**

- **RESTful Design** - Proper HTTP methods
- **API Versioning** - `/api/v1/` prefix
- **Consistent Responses** - Standardized format
- **Error Handling** - Proper status codes
- **Authentication** - JWT Bearer tokens
- **Documentation** - Complete Swagger integration

---

## 📝 Sample Request Bodies

### Create Supplier

```json
{
  "name": "ABC Pharmaceuticals",
  "code": "SUP001",
  "email": "contact@abc.com",
  "phone": "123-456-7890",
  "address": "123 Main St, City, State",
  "contactPerson": "John Doe",
  "paymentTerms": "Net 30",
  "status": "ACTIVE"
}
```

### Create Sale

```json
{
  "storeId": 2,
  "customerId": 1,
  "pharmacistId": 1,
  "paymentMethod": "CASH",
  "items": [
    {
      "medicineId": 1,
      "quantity": 5,
      "unitPrice": 8.00,
      "totalPrice": 40.00
    }
  ],
  "subtotal": 40.00,
  "taxAmount": 4.00,
  "totalAmount": 44.00
}
```

### Create Production Batch

```json
{
  "recipeId": 1,
  "batchNumber": "PB001",
  "plannedQuantity": 1000,
  "productionDate": "2026-01-16",
  "expiryDate": "2028-01-16",
  "storeId": 1,
  "notes": "First production batch"
}
```

---

## 🔧 Environment Variables

|  Variable   |      Default Value      |       Description        |
|-------------|-------------------------|--------------------------|
| `base_url`  | `http://localhost:8080` | API base URL             |
| `jwt_token` | *(empty)*               | JWT authentication token |

### How to Set Variables:

1. In Postman, click **"Environments"**
2. Select your environment
3. Set `base_url` to your server URL
4. Set `jwt_token` after login (if authentication required)

---

## 🚨 Important Notes

### Authentication

- Some endpoints may require JWT authentication
- Set the `jwt_token` environment variable after login
- Bearer token is automatically included in requests

### File Upload

- Use **"File Upload Management"** folder
- Select actual files for upload requests
- Supported: PDF, DOC, XLS, images, etc.

### Sample Data

- All requests include realistic sample data
- Modify IDs and values as needed for your testing
- Create dependent resources first (suppliers before purchase orders, etc.)

### Response Codes

- **200**: Success
- **201**: Created
- **204**: No Content (delete operations)
- **400**: Bad Request (validation errors)
- **404**: Not Found
- **413**: Payload Too Large (file uploads)

---

## 📊 Testing Workflow

### 1. **Setup Phase**

```
Authentication → Login (optional)
Store Management → Create Stores
Supplier Management → Create Suppliers
User Management → Create Users
```

### 2. **Procurement Phase**

```
Purchase Order Management → Create Orders
Purchase Order Management → Approve Orders
Purchase Order Management → Receive Orders
```

### 3. **Production Phase**

```
Recipe Management → Create Recipes
Ingredient Management → Check Stock
Production Batch Management → Start Production
Production Batch Management → Complete Production
```

### 4. **Inventory Phase**

```
Inventory Stock Management → Add Stock
Stock Transfer Management → Transfer Stock
Alert Management → Check Alerts
```

### 5. **Sales Phase**

```
Customer Management → Create Customers
Prescription Management → Create Prescriptions
Sales Management → Create Sales
Sales Management → Process Returns (if needed)
```

### 6. **File Management**

```
File Upload Management → Upload Documents
File Upload Management → Download Files
```

---

## 🎉 Ready to Use!

### Import the Collection:

1. **Download**: `Pharmacy_Management_System_Complete_API.postman_collection.json`
2. **Import** into Postman
3. **Set Environment** variables
4. **Start Testing** your APIs!

### Features Included:

- ✅ **80+ API Endpoints** - Complete coverage
- ✅ **Sample Data** - Ready to run
- ✅ **Authentication** - JWT configured
- ✅ **File Uploads** - Form data configured
- ✅ **Environment Variables** - Dynamic URLs
- ✅ **Documentation** - Detailed descriptions
- ✅ **Error Handling** - Status codes covered

---

## 📞 Support

### Quick Start:

```bash
# 1. Import collection into Postman
# 2. Set base_url = http://localhost:8080
# 3. Start your Spring Boot app
# 4. Run requests in order!
```

### Common Issues:

- **404 errors**: Check if app is running on correct port
- **401 errors**: Set JWT token in environment variables
- **400 errors**: Check request body format
- **File uploads**: Select actual files in form data

### API Documentation:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI Spec**: http://localhost:8080/api-docs

---

**🎯 Your complete API testing suite is ready!**

*Test all features of your Pharmacy Management System with this comprehensive Postman collection.*

---

**Created**: January 16, 2026  
**Version**: API v1.0  
**Endpoints**: 80+  
**Modules**: 15  
**Status**: ✅ **COMPLETE & READY** ✅
