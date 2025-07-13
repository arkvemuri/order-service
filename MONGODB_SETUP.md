# 🍃 MongoDB Setup Guide for Order Service

## 🚨 **Current Issue**
```
Exception in monitor thread while connecting to server localhost:27017
```

**Root Cause**: MongoDB is not running or not accessible on `localhost:27017`

## 🎯 **Solutions Available**

### **Option 1: Docker Compose (Recommended for Development)**

#### **1.1 Start All Services**
```bash
# Start MongoDB, Eureka, and Order Service
docker-compose up -d

# View logs
docker-compose logs -f order-service
```

#### **1.2 Start Only MongoDB**
```bash
# Start only MongoDB
docker-compose up -d mongodb

# Run your application locally
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

#### **1.3 Configuration**
- **MongoDB**: `localhost:27017`
- **Database**: `orderdb`
- **Username**: `admin`
- **Password**: `password`

---

### **Option 2: Local MongoDB Installation**

#### **2.1 Install MongoDB Community Edition**
```bash
# Windows (using Chocolatey)
choco install mongodb

# Or download from: https://www.mongodb.com/try/download/community
```

#### **2.2 Start MongoDB Service**
```bash
# Windows
net start MongoDB

# Or run manually
mongod --dbpath C:\data\db
```

#### **2.3 Create Database**
```bash
# Connect to MongoDB
mongosh

# Create database and user
use orderdb
db.createUser({
  user: "orderuser",
  pwd: "orderpass",
  roles: ["readWrite"]
})
```

---

### **Option 3: MongoDB Atlas (Cloud)**

#### **3.1 Setup**
1. Go to [MongoDB Atlas](https://www.mongodb.com/atlas)
2. Create a free cluster
3. Get connection string

#### **3.2 Update Configuration**
```yaml
# application.yaml
spring:
  data:
    mongodb:
      uri: mongodb+srv://username:password@cluster.mongodb.net/orderdb
```

---

### **Option 4: Testcontainers (For Testing)**

#### **4.1 Run Tests**
```bash
# Tests will automatically start MongoDB container
mvn test

# Or run specific test
mvn test -Dtest=OrderApplicationTests
```

#### **4.2 Benefits**
- ✅ No local MongoDB installation needed
- ✅ Isolated test environment
- ✅ Automatic container management

---

## 🔧 **Environment-Specific Configuration**

### **Development Profile**
```bash
# Use local MongoDB
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### **Docker Profile**
```bash
# Use containerized MongoDB
mvn spring-boot:run -Dspring-boot.run.profiles=docker
```

### **Test Profile**
```bash
# Use Testcontainers
mvn test -Dspring.profiles.active=test
```

---

## 🚀 **Quick Start Commands**

### **Option A: Full Docker Environment**
```bash
# 1. Start all services
docker-compose up -d

# 2. Check logs
docker-compose logs -f order-service

# 3. Test the application
curl http://localhost:9094/actuator/health
```

### **Option B: Local Development**
```bash
# 1. Start only MongoDB
docker-compose up -d mongodb

# 2. Run application locally
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# 3. Test the application
curl http://localhost:9094/actuator/health
```

### **Option C: Testing Only**
```bash
# Run tests with Testcontainers
mvn clean test
```

---

## 🔍 **Troubleshooting**

### **Check MongoDB Connection**
```bash
# Test MongoDB connection
mongosh mongodb://localhost:27017/orderdb

# Or with authentication
mongosh mongodb://admin:password@localhost:27017/orderdb?authSource=admin
```

### **Check Docker Containers**
```bash
# List running containers
docker ps

# Check MongoDB logs
docker logs order-mongodb

# Check application logs
docker logs order-service
```

### **Verify Application Configuration**
```bash
# Check active profiles
curl http://localhost:9094/actuator/env | grep profiles

# Check MongoDB configuration
curl http://localhost:9094/actuator/configprops | grep mongodb
```

---

## 📋 **Connection Strings by Environment**

| Environment | Connection String |
|-------------|-------------------|
| **Local Dev** | `mongodb://localhost:27017/orderdb` |
| **Docker** | `mongodb://admin:password@mongodb:27017/orderdb?authSource=admin` |
| **Test** | Managed by Testcontainers |
| **Atlas** | `mongodb+srv://user:pass@cluster.mongodb.net/orderdb` |

---

## ✅ **Verification Steps**

1. **MongoDB is Running**
   ```bash
   # Check if MongoDB is accessible
   telnet localhost 27017
   ```

2. **Application Connects**
   ```bash
   # Check application health
   curl http://localhost:9094/actuator/health
   ```

3. **Database Operations Work**
   ```bash
   # Test API endpoints
   curl -X POST http://localhost:9094/orders \
     -H "Content-Type: application/json" \
     -d '{"items": [{"name": "Test Item", "quantity": 1}]}'
   ```

Choose the option that best fits your development environment! 🚀