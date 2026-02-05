# ✅ Liquibase Implementation - Quick Setup Guide

## What Was Done

You now have **Liquibase** integrated into your Pharmacy Management System for database schema version control.

### ✅ Changes Made:

1. **Added Liquibase Dependency** to `pom.xml`

   ```xml
   <dependency>
       <groupId>org.liquibase</groupId>
       <artifactId>liquibase-core</artifactId>
   </dependency>
   ```
2. **Updated Configuration** in `application.properties`

   ```properties
   # Hibernate ddl-auto set to VALIDATE (no auto-generation)
   spring.jpa.hibernate.ddl-auto=validate

   # Liquibase enabled
   spring.liquibase.enabled=true
   spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.xml
   ```
3. **Created Changelog Structure**

   ```
   src/main/resources/db/changelog/
   ├── db.changelog-master.xml
   └── v001/
       ├── 001_create_base_tables.xml
       └── 002_create_party_and_account_tables.xml
   ```
4. **Switched to H2 Database** for easy local development

   ```properties
   spring.datasource.url=jdbc:h2:mem:pharmacydb
   spring.h2.console.enabled=true
   spring.h2.console.path=/h2-console
   ```

---

## 🚀 How It Works Now

### On Application Startup:

1. Liquibase reads `db.changelog-master.xml`
2. It creates `databasechangelog` and `databasechangeloglock` tables
3. It executes any new changesets
4. Hibernate validates existing schema (no auto-creation)

### Benefits:

✅ **Schema tracked in Git** - All changes in XML files  
✅ **No more `ddl-auto=update`** - Safer for production  
✅ **Version control** - Complete history of schema changes  
✅ **Team collaboration** - Multiple developers can work on migrations  
✅ **Easy rollback** - Revert to previous schema versions

---

## 📝 Creating New Migrations

### Step 1: Create Migration File

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

    <changeSet id="v002-001-add-column" author="your-name">
        <addColumn tableName="parties">
            <column name="credit_limit" type="NUMERIC(15,2)" defaultValue="0.00">
                <constraints nullable="false" />
            </column>
        </addColumn>
    </changeSet>

</databaseChangeLog>
```

### Step 3: Include in Master

Edit `db.changelog-master.xml`:

```xml
<include file="db/changelog/v002/001_add_new_feature.xml" />
```

### Step 4: Run App

```bash
./mvnw spring-boot:run
```

Liquibase automatically applies the migration! ✅

---

## 🔧 Common Liquibase Operations

### Add Column

```xml
<addColumn tableName="accounts">
    <column name="new_field" type="VARCHAR(255)">
        <constraints nullable="false" />
    </column>
</addColumn>
```

### Create Index

```xml
<createIndex tableName="accounts" indexName="idx_account_code">
    <column name="account_code" />
</createIndex>
```

### Rename Column

```xml
<renameColumn tableName="accounts" 
    oldColumnName="old_name" 
    newColumnName="new_name" 
    columnDataType="VARCHAR(255)" />
```

### Drop Column

```xml
<dropColumn tableName="accounts" columnName="unused_column" />
```

### Modify Column

```xml
<modifyDataType tableName="accounts" 
    columnName="balance" 
    newDataType="NUMERIC(20,2)" />
```

---

## 📊 Database Tables

Liquibase creates and maintains two tables:

### `databasechangelog`

Records all executed migrations:

```sql
SELECT id, author, filename, dateexecuted FROM databasechangelog;
```

### `databasechangeloglock`

Prevents concurrent migrations:

```sql
SELECT * FROM databasechangeloglock;
```

---

## 💡 Best Practices

### ✅ DO:

1. **Create one changeset per change**

   ```xml
   <changeSet id="v002-001-add-column">
       <addColumn .../>
   </changeSet>
   <changeSet id="v002-002-create-index">
       <createIndex .../>
   </changeSet>
   ```
2. **Use meaningful IDs**

   ```xml
   id="v002-001-add-credit-limit"
   ```
3. **Include author names**

   ```xml
   author="john-dev"
   ```

### ❌ DON'T:

1. **Don't modify executed changesets** - Create new ones instead
2. **Don't rely on Hibernate ddl-auto** - Use Liquibase for all schema changes
3. **Don't mix multiple changes** in one changeset

---

## 🎯 Configuration Summary

### application.properties

```properties
# Hibernate - VALIDATE mode (no auto schema generation)
spring.jpa.hibernate.ddl-auto=validate

# Liquibase Configuration
spring.liquibase.enabled=true
spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.xml
spring.liquibase.default-schema=PUBLIC
spring.liquibase.drop-first=false
spring.liquibase.contexts=dev

# H2 Database (for local development)
spring.datasource.url=jdbc:h2:mem:pharmacydb
spring.datasource.username=sa
spring.datasource.password=sa
spring.datasource.driver-class-name=org.h2.Driver
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

---

## 📋 Production Deployment

### For PostgreSQL:

Create `application-prod.properties`:

```properties
spring.datasource.url=jdbc:postgresql://prod-host:5432/pharmacy_db
spring.datasource.username=prod_user
spring.datasource.password=prod_password
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.liquibase.enabled=true
spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.xml
spring.liquibase.contexts=prod
```

Run with:

```bash
./mvnw spring-boot:run -Dspring.profiles.active=prod
```

---

## 🆘 Troubleshooting

### Issue: "Table already exists"

**Solution:** This is normal on first run. Liquibase tracks all changes in `databasechangelog` table.

### Issue: "Changeset failed to execute"

**Solution:** Check changeset syntax and database compatibility. Use `dbms="h2"` attribute if needed.

### Issue: "Need to drop database"

**Solution:** In development only, add to properties:

```properties
spring.liquibase.drop-first=true
```

---

## ✅ Verification Checklist

- [x] Liquibase dependency added
- [x] Hibernate ddl-auto set to `validate`
- [x] Master changelog created
- [x] Migration files created
- [x] Configuration updated
- [x] H2 database configured
- [x] Ready for new migrations

---

## 🎓 Next Steps

1. **Run the application:**

   ```bash
   ./mvnw spring-boot:run
   ```
2. **Check H2 Console** (optional):

   ```
   http://localhost:2005/h2-console
   ```
3. **Create new migrations** as needed using the examples above
4. **For production**, use PostgreSQL with profile:

   ```bash
   mvn spring-boot:run -Dspring.profiles.active=postgres
   ```

---

## 📚 Files Reference

### Configuration

- `src/main/resources/application.properties` ✅ Updated
- `pom.xml` ✅ Liquibase dependency added

### Changelogs

- `src/main/resources/db/changelog/db.changelog-master.xml` ✅ Created
- `src/main/resources/db/changelog/v001/001_create_base_tables.xml` ✅ Created
- `src/main/resources/db/changelog/v001/002_create_party_and_account_tables.xml` ✅ Created

### Documentation

- `LIQUIBASE_SETUP_GUIDE.md` ✅ Comprehensive guide

---

## 🎉 Summary

You now have:

✅ **Liquibase integrated** - Schema version control  
✅ **Database migrations tracked in Git** - All changes in XML files  
✅ **No Hibernate auto-generation** - Safer for production  
✅ **H2 for development** - Easy local testing  
✅ **Ready for PostgreSQL** - Production-ready setup

**Your database schema is now properly versioned and managed!** 🚀

---

*Setup completed: February 6, 2026*  
*Status: Ready for Use ✅*
