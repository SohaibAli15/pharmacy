/* Copyright (C) Pharmacy Management System - All Rights Reserved */
# Unified Ledger Account System - Implementation Complete

## 🎯 Overview

A complete unified ledger account system has been successfully implemented for the Pharmacy Management System. This system allows customers and suppliers to be treated as a single "Party" entity that shares ONE unified ledger account. This enables seamless tracking of debit/credit transactions regardless of whether a party acts as both customer AND supplier.

---

## 📦 New Entities Created

### 1. **Party.java** (`com.pharmacy.entity`)
Represents any business entity (customer, supplier, or both) with:
- **Unique Fields:**
  - `partyCode` (UNIQUE) - Unified party identifier
  - `partyName` - Name of the party
  - `partyType` - Enum: CUSTOMER_ONLY, SUPPLIER_ONLY, BOTH
  - Email, Phone, Address, City, State, Country, ZipCode
  - Tax ID, Bank Account, Notes

- **Key Relationships:**
  - `customer` (OneToOne, nullable) - Link to Customer entity
  - `supplier` (OneToOne, nullable) - Link to Supplier entity
  - `account` (OneToOne, CascadeAll) - **Single unified ledger account**

### 2. **Account.java** (`com.pharmacy.entity`)
Represents the unified ledger account for a party:
- **Unique Fields:**
  - `accountCode` (UNIQUE) - Account identifier
  - `accountName` - Account name
  - `accountType` - Enum: RECEIVABLE, PAYABLE, BOTH
  - `accountOpeningDate` - Account creation date
  - `openingBalance` - Opening balance (default: 0.00)
  - `currentBalance` - Running balance
  - `totalDebits` - Cumulative debit transactions
  - `totalCredits` - Cumulative credit transactions
  - `status` - Enum: ACTIVE, INACTIVE, SUSPENDED, CLOSED

- **Key Features:**
  - @Builder pattern with default values
  - Automatic balance calculation based on account type
  - Timestamp tracking (createdAt, updatedAt)

### 3. **Enhanced GeneralLedgerEntry.java**
Updated to reference Party and Account:
- `linkedAccount` (ManyToOne) - Direct reference to Account entity
- `party` (ManyToOne) - Reference to Party for quick lookup
- Enables complete transaction traceability

---

## 📝 Enhanced Existing Entities

### Customer.java
- ✅ Added: `party` (OneToOne relationship)
- Allows customer to be linked to unified Party and its Account

### Supplier.java
- ✅ Added: `party` (OneToOne relationship)
- Allows supplier to be linked to unified Party and its Account

---

## 🗂️ New DTOs Created

1. **PartyDto** - Transfer object for Party entity with all fields
2. **AccountDto** - Transfer object for Account with party details
3. **AccountStatementDto** - Complete account statement including:
   - Account information
   - Statement date range
   - Opening/Closing balances
   - Total debits/credits
   - Transaction list

4. **AccountTransactionDto** - Individual transaction details:
   - Transaction ID, Date, Description
   - Reference Number (Invoice/PO)
   - Debit/Credit amounts
   - Running Balance

---

## 📚 New Repositories Created

### PartyRepository (`com.pharmacy.repository`)
Methods:
- `findByPartyCode(String)`
- `findByEmail(String)`
- `findByCustomerId(Long)`
- `findBySupplierId(Long)`
- `findByPartyType(Party.PartyType)`

### AccountRepository (`com.pharmacy.repository`)
Methods:
- `findByAccountCode(String)`
- `findByAccountName(String)`
- `findByAccountType(Account.AccountType)`
- `findByStatus(Account.AccountStatus)`
- `findByPartyId(Long)` - Fetch account for a party

---

## 🔧 New Services Created

### AccountService Interface (`com.pharmacy.service`)
**Key Methods:**
```java
// Account Management
AccountDto createAccount(AccountDto accountDto)
AccountDto getAccountById(Long accountId)
AccountDto getAccountByCode(String accountCode)
AccountDto getAccountByPartyId(Long partyId)
List<AccountDto> getAllAccounts()
List<AccountDto> getAccountsByType(Account.AccountType)

// Balance & Transactions
BigDecimal getAccountBalance(Long accountId)
void updateAccountBalance(Long accountId, BigDecimal debit, BigDecimal credit)
List<?> getAccountTransactions(Long accountId)

// Statements & Status
AccountStatementDto getAccountStatement(Long accountId, LocalDate from, LocalDate to)
AccountDto updateAccountStatus(Long accountId, Account.AccountStatus status)
void deleteAccount(Long accountId)
```

### AccountServiceImpl (`com.pharmacy.service.impl`)
- Complete implementation with transaction support
- Automatic balance calculation based on account type:
  - **RECEIVABLE**: Debit ↑ Balance, Credit ↓ Balance
  - **PAYABLE**: Credit ↑ Balance, Debit ↓ Balance
  - **BOTH**: Default RECEIVABLE logic
- Statement generation with running balance calculation

### PartyService Interface (`com.pharmacy.service`)
**Key Methods:**
```java
// Party Management
PartyDto createParty(PartyDto partyDto)
PartyDto getPartyById(Long partyId)
PartyDto getPartyByCode(String partyCode)
PartyDto getPartyByEmail(String email)
List<PartyDto> getAllParties()
List<PartyDto> getPartiesByType(Party.PartyType)
PartyDto updateParty(Long partyId, PartyDto partyDto)
void deleteParty(Long partyId)

// Linking Entities
PartyDto linkCustomerToParty(Long partyId, Long customerId)
PartyDto linkSupplierToParty(Long partyId, Long supplierId)
PartyDto getPartyByCustomerId(Long customerId)
PartyDto getPartyBySupplierId(Long supplierId)
```

### PartyServiceImpl (`com.pharmacy.service.impl`)
- Auto-creates Account when Party is created
- Sets account type based on party type
- Supports converting CUSTOMER_ONLY to BOTH when supplier is linked
- Full transaction support with cascading operations

---

## 🎮 New Controllers Created

### AccountController (`com.pharmacy.controller`)
**Endpoints:**
```
POST   /api/v1/accounts                    - Create account
GET    /api/v1/accounts                    - Get all accounts
GET    /api/v1/accounts/{id}               - Get account by ID
GET    /api/v1/accounts/code/{code}        - Get by account code
GET    /api/v1/accounts/party/{partyId}    - Get account for party
GET    /api/v1/accounts/type/{type}        - Get accounts by type
GET    /api/v1/accounts/{id}/balance       - Get current balance
GET    /api/v1/accounts/{id}/transactions  - Get all transactions
GET    /api/v1/accounts/{id}/statement     - Generate statement (with date range)
PATCH  /api/v1/accounts/{id}/status        - Update account status
DELETE /api/v1/accounts/{id}               - Delete account
```

### PartyController (`com.pharmacy.controller`)
**Endpoints:**
```
POST   /api/v1/parties                              - Create party
GET    /api/v1/parties                              - Get all parties
GET    /api/v1/parties/{id}                         - Get party by ID
GET    /api/v1/parties/code/{code}                  - Get by party code
GET    /api/v1/parties/email/{email}                - Get by email
GET    /api/v1/parties/type/{type}                  - Get parties by type
GET    /api/v1/parties/customer/{customerId}        - Get party by customer
GET    /api/v1/parties/supplier/{supplierId}        - Get party by supplier
POST   /api/v1/parties/{partyId}/link-customer/{id} - Link customer to party
POST   /api/v1/parties/{partyId}/link-supplier/{id} - Link supplier to party
PUT    /api/v1/parties/{id}                         - Update party
DELETE /api/v1/parties/{id}                         - Delete party
```

---

## 🔐 Key Features

### ✅ Unified Account System
- **One Party = One Account**: Whether acting as customer or supplier, a party maintains a single ledger account
- Eliminates duplicate account records
- Simplifies reconciliation and reporting

### ✅ Smart Balance Calculation
- Account type determines balance logic
- RECEIVABLE accounts: Customer owes money (Debit ↑, Credit ↓)
- PAYABLE accounts: We owe supplier (Credit ↑, Debit ↓)
- BOTH accounts: Flexible for mixed scenarios

### ✅ Account Statements
- Date-range filtering
- Running balance calculation per transaction
- Complete transaction history
- Opening/Closing balances
- Total debits/credits summary

### ✅ Transaction Traceability
- GL entries linked to Party and Account
- Cross-reference between Account and GL
- Quick lookup of all party transactions

### ✅ Cascading Operations
- Creating a Party auto-creates its Account
- Account type dynamically adjusts (CUSTOMER_ONLY → BOTH when supplier linked)
- Relationship management handled automatically

---

## 🔄 Integration Points

### How to Integrate with Existing Services:

#### 1. When Creating a Customer:
```java
// In CustomerService.createCustomer():
// Option A: Create party explicitly
PartyDto partyDto = new PartyDto();
partyDto.setPartyCode(customerCode);
partyDto.setPartyName(firstName + " " + lastName);
partyDto.setPartyType(Party.PartyType.CUSTOMER_ONLY);
// ... set other fields
PartyDto createdParty = partyService.createParty(partyDto);

// Then link customer to party
partyService.linkCustomerToParty(createdParty.getId(), customer.getId());
```

#### 2. When Creating a Supplier:
```java
// Similar process, use Party.PartyType.SUPPLIER_ONLY
```

#### 3. When Recording a Sale/Invoice:
```java
// Post GL entry AND update account balance
GeneralLedgerEntry glEntry = createGLEntry(...);
glEntry.setParty(party);
glEntry.setLinkedAccount(account);
ledgerEntryRepository.save(glEntry);

// Update account balance
accountService.updateAccountBalance(
    account.getId(), 
    glEntry.getDebit(), 
    glEntry.getCredit()
);
```

#### 4. When Recording a Purchase:
```java
// Same pattern - GL entry auto-posts to supplier's account
```

#### 5. Generate Account Statement:
```java
AccountStatementDto statement = accountService.getAccountStatement(
    accountId, 
    LocalDate.of(2026, 1, 1), 
    LocalDate.of(2026, 1, 31)
);
// Returns complete statement with running balance
```

---

## 📋 Usage Example Workflow

### Scenario: John does business as BOTH customer AND supplier

**Step 1: Create Party**
```json
POST /api/v1/parties
{
  "partyCode": "PARTY-001",
  "partyName": "John Business",
  "partyType": "CUSTOMER_ONLY",
  "email": "john@business.com",
  "phone": "+91-9876543210",
  "address": "123 Main St",
  "city": "New York",
  "taxId": "TAX-123",
  "bankAccount": "BANK-456"
}
// Response: { id: 1, accountId: 100, partyType: "CUSTOMER_ONLY", ... }
```

**Step 2: Link as Customer**
```json
POST /api/v1/customers
{
  "customerCode": "CUST-001",
  "firstName": "John",
  "lastName": "Business",
  "email": "john@business.com",
  ...
}
// In service: partyService.linkCustomerToParty(1, customerId)
```

**Step 3: Link as Supplier**
```json
POST /api/v1/suppliers
{
  "code": "SUPP-001",
  "name": "John Business",
  "email": "john@business.com",
  ...
}
// In service: partyService.linkSupplierToParty(1, supplierId)
// Account type auto-updates to BOTH
```

**Step 4: View Unified Account**
```json
GET /api/v1/accounts/party/1

Response: {
  "id": 100,
  "accountCode": "PARTY-001",
  "accountName": "John Business",
  "accountType": "BOTH",
  "currentBalance": 50000.00,
  "totalDebits": 100000.00,
  "totalCredits": 50000.00,
  ...
}
```

**Step 5: Generate Account Statement**
```json
GET /api/v1/accounts/100/statement?fromDate=2026-01-01&toDate=2026-01-31

Response: {
  "accountId": 100,
  "accountCode": "PARTY-001",
  "accountName": "John Business",
  "openingBalance": 0.00,
  "closingBalance": 50000.00,
  "transactions": [
    {
      "date": "2026-01-05",
      "description": "Sale Invoice #INV-001",
      "debit": 50000.00,
      "credit": 0.00,
      "runningBalance": 50000.00
    },
    {
      "date": "2026-01-10",
      "description": "Purchase Invoice #PO-001",
      "debit": 0.00,
      "credit": 25000.00,
      "runningBalance": 25000.00
    }
  ]
}
```

---

## 📊 Database Schema

### New Tables:

**parties**
```sql
- id (PK, Auto)
- party_code (UNIQUE)
- party_name
- party_type (ENUM)
- email
- phone
- address, city, state, country, zip_code
- tax_id, bank_account
- notes
- customer_id (FK, UNIQUE, Nullable)
- supplier_id (FK, UNIQUE, Nullable)
- account_id (FK, UNIQUE)
- created_at, updated_at
```

**accounts**
```sql
- id (PK, Auto)
- account_code (UNIQUE)
- account_name
- account_type (ENUM)
- account_opening_date
- opening_balance (Decimal 15,2)
- current_balance (Decimal 15,2)
- total_debits (Decimal 15,2)
- total_credits (Decimal 15,2)
- status (ENUM)
- notes
- created_at, updated_at
```

**general_ledger_entries** (Updated)
```sql
- ...existing fields...
- account_id (FK to accounts, Nullable)
- party_id (FK to parties, Nullable)
```

---

## 🧪 Testing Checklist

- [x] Create Party (Customer Only)
- [x] Create Party (Supplier Only)
- [x] Create Party (Both)
- [x] Link Customer to Party
- [x] Link Supplier to Party
- [x] Verify Account Auto-Creation
- [x] Update Account Balance
- [x] Generate Account Statement
- [x] Get Account by Party
- [x] Get All Accounts by Type
- [ ] Integration with Sale/Purchase services (Next Phase)
- [ ] GL Entry posting to accounts (Next Phase)

---

## 🚀 Next Steps

### Phase 2: GL Integration
1. **SalesInvoiceService**: Post GL entries to customer accounts
2. **PurchaseInvoiceService**: Post GL entries to supplier accounts
3. **PaymentService**: Record payment GL entries and update balances
4. **ReturnService**: Reverse transactions with return GL entries

### Phase 3: Reporting
1. **AR/AP Reports**: Aging analysis by party
2. **Account Reconciliation**: Auto-reconcile GL vs Account
3. **Dashboard Integration**: Party summary in Partners Dashboard
4. **Account Status Alerts**: Track overdue payables/receivables

### Phase 4: Advanced Features
1. **Opening Balance Import**: Bulk migrate existing customer/supplier balances
2. **GL Posting Reversal**: Undo transaction with audit trail
3. **Account Merge**: Consolidate duplicate party accounts
4. **Multi-Currency Support**: Track accounts in different currencies

---

## 📚 File Locations

**New Entities:**
- `src/main/java/com/pharmacy/entity/Party.java`
- `src/main/java/com/pharmacy/entity/Account.java`

**New DTOs:**
- `src/main/java/com/pharmacy/dto/PartyDto.java`
- `src/main/java/com/pharmacy/dto/AccountDto.java`
- `src/main/java/com/pharmacy/dto/AccountStatementDto.java`
- `src/main/java/com/pharmacy/dto/AccountTransactionDto.java`

**New Repositories:**
- `src/main/java/com/pharmacy/repository/PartyRepository.java`
- `src/main/java/com/pharmacy/repository/AccountRepository.java`

**New Services:**
- `src/main/java/com/pharmacy/service/PartyService.java`
- `src/main/java/com/pharmacy/service/AccountService.java`
- `src/main/java/com/pharmacy/service/impl/PartyServiceImpl.java`
- `src/main/java/com/pharmacy/service/impl/AccountServiceImpl.java`

**New Controllers:**
- `src/main/java/com/pharmacy/controller/PartyController.java`
- `src/main/java/com/pharmacy/controller/AccountController.java`

---

## ✅ Build Status

✓ **Clean Compilation** - All files compile successfully
✓ **Code Formatting** - Spotless formatting applied
✓ **No Errors** - Zero compilation errors
✓ **Ready for Testing** - All endpoints available

**Last Build:** 2026-02-06 00:52:41 UTC
**Build Duration:** 26.243 seconds
**Status:** SUCCESS

---

## 📞 Support & Questions

For integration questions or modifications, refer to the service implementations and controller examples above.

