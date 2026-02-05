# 📚 Liquibase Database Schema Management Guide

## Overview

Liquibase is a database migration tool that tracks all schema changes in a version control system. Instead of letting Hibernate auto-generate your database schema (`ddl-auto=update`), Liquibase provides:

✅ **Version Control** - All schema changes tracked in XML/YAML files  
✅ **Audit Trail** - Complete history of who changed what and when  
✅ **Rollback Support** - Revert changes if needed  
✅ **Team Collaboration** - Multiple developers can work on migrations  
✅ **CI/CD Integration** - Automated migrations in pipelines  
✅ **Database Agnostic** - Works with PostgreSQL, MySQL, Oracle, etc.

---

## 🚀 Quick Start

### 1. Dependencies Already Added

```xml
<!-- In pom.xml -->
<dependency>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-core</artifactId>
</dependency>
```

### 2. Configuration Already Set

```properties
# In application.properties
spring.jpa.hibernate.ddl-auto=validate
spring.liquibase.enabled=true
spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.xml
spring.liquibase.contexts=dev
spring.liquibase.labels=dev
```

### 3. Files Already Created

```
src/main/resources/db/changelog/
├── db.changelog-master.xml              (Main changelog file)
└── v001/
    ├── 001_create_base_tables.xml      (Basic tables)
    └── 002_create_party_and_account_tables.xml (New ledger system)
```

### 4. Run Your Application

```bash
./mvnw spring-boot:run
```

Liquibase will:
- Read all changesets
- Create `databasechangelog` table to track migrations
- Execute any new migrations
- Print progress to console

---

## 📊 Directory Structure

```
src/main/resources/db/changelog/
├── db.changelog-master.xml           # Main entry point (includes all changelogs)
└── v001/                              # Version 1
    ├── 001_create_base_tables.xml
    └── 002_create_party_and_account_tables.xml
└── v002/                              # Version 2 (future)
    └── 001_new_feature.xml
```

---

## 📝 Creating New Migrations

### Step 1: Create New File

Create a new XML file in a version folder:

```
src/main/resources/db/changelog/v002/001_add_new_feature.xml
```

### Step 2: Write Migration

```xml
<?xml version="1.0" encoding="UTF-8"?>
<databaseChangeLog
    xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
    http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-4.25.0.xsd">

    <!-- Add new column to existing table -->
    <changeSet id="v002-001-add-credit-limit" author="john-dev">
        <addColumn tableName="parties">
            <column name="credit_limit" type="NUMERIC(15,2)" defaultValue="0.00">
                <constraints nullable="false" />
            </column>
        </addColumn>
    </changeSet>

    <!-- Create new index -->
    <changeSet id="v002-002-add-index-credit-limit" author="john-dev">
        <createIndex tableName="parties" indexName="idx_credit_limit">
            <column name="credit_limit" />
        </createIndex>
    </changeSet>

</databaseChangeLog>
```

### Step 3: Include in Master Changelog

Edit `db.changelog-master.xml`:

```xml
<include file="db/changelog/v002/001_add_new_feature.xml" />
```

### Step 4: Run Application

```bash
./mvnw spring-boot:run
```

Liquibase automatically executes new changesets!

---

## 🔧 Common Operations

### Add a New Column

```xml
<changeSet id="v002-001-add-column" author="developer">
    <addColumn tableName="accounts">
        <column name="new_column" type="VARCHAR(255)">
            <constraints nullable="false" defaultValue="value" />
        </column>
    </addColumn>
</changeSet>
```

### Create a New Table

```xml
<changeSet id="v002-002-create-table" author="developer">
    <createTable tableName="new_table">
        <column name="id" type="BIGINT" autoIncrement="true">
            <constraints primaryKey="true" />
        </column>
        <column name="name" type="VARCHAR(255)">
            <constraints nullable="false" />
        </column>
        <column name="created_at" type="TIMESTAMP" defaultValueComputed="CURRENT_TIMESTAMP">
            <constraints nullable="false" />
        </column>
    </createTable>
</changeSet>
```

### Create an Index

```xml
<changeSet id="v002-003-create-index" author="developer">
    <createIndex tableName="accounts" indexName="idx_account_code">
        <column name="account_code" />
    </createIndex>
</changeSet>
```

### Add Foreign Key

```xml
<changeSet id="v002-004-add-fk" author="developer">
    <addForeignKeyConstraint
        baseTableName="accounts"
        baseColumnNames="party_id"
        constraintName="fk_accounts_party"
        referencedTableName="parties"
        referencedColumnNames="id" />
</changeSet>
```

### Rename Column

```xml
<changeSet id="v002-005-rename-column" author="developer">
    <renameColumn
        tableName="accounts"
        oldColumnName="old_name"
        newColumnName="new_name"
        columnDataType="VARCHAR(255)" />
</changeSet>
```

### Drop Column

```xml
<changeSet id="v002-006-drop-column" author="developer">
    <dropColumn tableName="accounts" columnName="column_to_remove" />
</changeSet>
```

### Modify Column

```xml
<changeSet id="v002-007-modify-column" author="developer">
    <modifyDataType tableName="accounts" columnName="balance" newDataType="NUMERIC(20,2)" />
</changeSet>
```

---

## 🎯 Best Practices

### ✅ DO

1. **Use Meaningful IDs**

   ```xml
   <changeSet id="v002-001-add-credit-limit" author="john-dev">
   ```
2. **One Change Per Changeset**

   ```xml
   <changeSet id="v002-001-add-column">
       <addColumn .../>
   </changeSet>
   <changeSet id="v002-002-add-index">
       <createIndex .../>
   </changeSet>
   ```
3. **Add Comments**

   ```xml
   <changeSet id="v002-001-add-credit-limit" author="john-dev">
       <!-- Credit limit for each party - Feb 10, 2026 -->
       <addColumn tableName="parties">
   ```
4. **Use Author Names**

   ```xml
   <changeSet id="..." author="john-dev">
   ```
5. **Include Default Values**

   ```xml
   <column name="status" type="VARCHAR(50)" defaultValue="ACTIVE">
       <constraints nullable="false" />
   </column>
   ```

### ❌ DON'T

1. **Don't Modify Existing Changesets**
   - Once executed, changesets are immutable
   - Create new changesets for changes
2. **Don't Mix Multiple Changes**

   ```xml
   <!-- WRONG -->
   <changeSet id="v002-001-multiple-changes">
       <addColumn .../>
       <createTable .../>
       <createIndex .../>
   </changeSet>

   <!-- RIGHT -->
   <changeSet id="v002-001-add-column">
       <addColumn .../>
   </changeSet>
   <changeSet id="v002-002-create-table">
       <createTable .../>
   </changeSet>
   ```
3. **Don't Rely on Hibernate ddl-auto**
   - Set to `validate` mode
   - All changes go through Liquibase

---

## 📊 Liquibase Database Tables

Liquibase maintains two tables:

### `databasechangelog`

Records all executed changesets:

```
ID           AUTHOR        FILENAME                              DATE        EXECTYPE
v001-001...  system        db/changelog/v001/001_create...xml   2026-02-06  EXECUTED
v001-002...  system        db/changelog/v001/002_create...xml   2026-02-06  EXECUTED
```

### `databasechangeloglock`

Prevents concurrent migrations:

```
ID   LOCKED   LOCKGRANTED         LOCKEDBY
1    false    NULL                NULL
```

You can query these to see migration history:

```sql
SELECT * FROM databasechangelog ORDER BY orderexecuted DESC;
```

---

## 🔄 Rollback (Advanced)

### Rollback Last Changeset

```bash
./mvnw liquibase:rollback \
  -Dliquibase.rollbackCount=1
```

### Rollback to Specific Date

```bash
./mvnw liquibase:rollback \
  -Dliquibase.rollbackDate='2026-02-06'
```

### Rollback to Specific Tag

```bash
# Tag current state
./mvnw liquibase:tag \
  -Dliquibase.tag=v1.0

# Later, rollback to tag
./mvnw liquibase:rollback \
  -Dliquibase.rollbackTag=v1.0
```

⚠️ **Note:** Rollback requires changesets to have `<rollback>` blocks:

```xml
<changeSet id="v002-001">
    <addColumn tableName="accounts">
        <column name="new_col" type="VARCHAR(255)" />
    </addColumn>
    <rollback>
        <dropColumn tableName="accounts" columnName="new_col" />
    </rollback>
</changeSet>
```

---

## 🧪 Testing Migrations Locally

### View SQL Before Execution

```bash
./mvnw liquibase:updateSQL
```

### Generate Change Report

```bash
./mvnw liquibase:changelogSyncToTag \
  -Dliquibase.tag=test-tag
```

### Validate Changesets

```bash
./mvnw liquibase:validate
```

---

## 📋 Current Migration Status

### Already Created Migrations

#### v001/001_create_base_tables.xml

- `roles` table
- `users` table
- `customer_status_entity` table
- `customer_type_entity` table
- `supplier_status_entity` table
- `store_type_entity` table
- `store_status_entity` table
- `alert_type_entity` table
- `stores` table
- `category` table
- `sub_category` table

#### v001/002_create_party_and_account_tables.xml

- `accounts` table (NEW - Ledger system)
- `parties` table (NEW - Unified entities)
- Add `party_id` column to `customers`
- Add `party_id` column to `suppliers`
- Enhance `general_ledger_entry` with account/party references

---

## 🚀 Next Steps After Initial Setup

### Step 1: Verify Execution

```bash
# Check that migrations ran
SELECT COUNT(*) FROM databasechangelog;

# Should show: 2 (if first run)
```

### Step 2: Add More Migrations as Needed

```
src/main/resources/db/changelog/v002/001_feature_name.xml
src/main/resources/db/changelog/v002/002_another_feature.xml
```

### Step 3: Update Master Changelog

```xml
<!-- In db.changelog-master.xml -->
<include file="db/changelog/v002/001_feature_name.xml" />
```

### Step 4: Run Application

```bash
./mvnw spring-boot:run
```

---

## 🎓 Example: Adding a New Field

**Scenario:** Add `credit_limit` to parties table

### 1. Create Migration File

```
db/changelog/v002/001_add_credit_limit.xml
```

### 2. Write Changeset

```xml
<?xml version="1.0" encoding="UTF-8"?>
<databaseChangeLog ...>
    <changeSet id="v002-001-add-credit-limit" author="john-dev">
        <comment>Add credit limit field to parties</comment>
        <addColumn tableName="parties">
            <column name="credit_limit" type="NUMERIC(15,2)" defaultValue="0.00">
                <constraints nullable="false" />
            </column>
        </addColumn>
    </changeSet>

    <changeSet id="v002-002-add-index-credit-limit" author="john-dev">
        <createIndex tableName="parties" indexName="idx_credit_limit">
            <column name="credit_limit" />
        </createIndex>
    </changeSet>
</databaseChangeLog>
```

### 3. Update Master Changelog

```xml
<!-- In db.changelog-master.xml -->
<include file="db/changelog/v002/001_add_credit_limit.xml" />
```

### 4. Update Entity

```java
@Entity
@Table(name = "parties")
public class Party {
    // ...existing fields...
    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal creditLimit = BigDecimal.ZERO;
}
```

### 5. Run Application

```bash
./mvnw spring-boot:run
```

✅ Done! Liquibase automatically applies the migration.

---

## 📞 Configuration Reference

### application.properties Options

```properties
# Enable/disable Liquibase
spring.liquibase.enabled=true

# Path to master changelog
spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.xml

# Default schema
spring.liquibase.default-schema=public
spring.liquibase.liquibase-schema=public

# Drop all before migrations (USE WITH CAUTION!)
spring.liquibase.drop-first=false

# Validate schema before migrating
spring.liquibase.validate-on-migrate=true

# Contexts to execute (for conditional migrations)
spring.liquibase.contexts=dev

# Labels to execute (for conditional migrations)
spring.liquibase.labels=dev
```

---

## ✅ Verification Checklist

- [x] Liquibase dependency added to pom.xml
- [x] Hibernate ddl-auto set to `validate` (not `update` or `create`)
- [x] Master changelog created
- [x] v001 migration files created
- [x] Liquibase configured in application.properties
- [x] Ready to add new migrations

---

## 🎉 Summary

You now have:

✅ **Database Version Control** via Liquibase  
✅ **Schema Tracked in Git** in XML format  
✅ **No Hibernate Auto Schema Generation** (safer for production)  
✅ **Audit Trail** of all schema changes  
✅ **Ready for CI/CD** pipelines  
✅ **Rollback Capability** for emergency reverts

**Start creating new migrations whenever you need schema changes!**

---

*Setup completed: February 6, 2026*  
*Ready for production use: ✅ YES*
