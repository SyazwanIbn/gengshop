# GengShop - E-Commerce REST API

A Spring Boot-based e-commerce backend API featuring JWT authentication, role-based access control, and modular entity management. Built as a portfolio project to demonstrate RESTful API design, Spring Security implementation, and database relationship management.

## 🚀 Tech Stack

- **Backend Framework:** Spring Boot 4.0.0
- **Language:** Java 21
- **Security:** Spring Security with JWT (JJWT 0.12.6)
- **Database:** PostgreSQL 16
- **ORM:** Hibernate/JPA
- **Validation:** Jakarta Bean Validation
- **Build Tool:** Maven
- **Development Tools:** Lombok, Spring DevTools
- **Containerization:** Docker & Docker Compose

## ✨ Features

### ✅ Implemented
- **User Authentication & Authorization**
  - User registration with email validation
  - Login with JWT token generation
  - Password encryption using BCrypt
  - Role-based access control (ROLE_USER default)
  
- **Security**
  - Stateless JWT authentication
  - Token validation with 15-minute expiration
  - Protected endpoints with role-based authorization
  - CSRF protection disabled for REST API

- **Category Management**
  - Category entity with JPA mapping
  - Database schema auto-generation
  - Prepared for CRUD operations

### 🔄 In Progress
- Category CRUD endpoints (GET, POST, PUT, DELETE)
- Product entity with Category relationship
- Product management system

### 📋 Planned
- Shopping cart functionality
- Order management
- Payment processing integration
- Product search and filtering
- Image upload for products
- Pagination for product listings

## 🗂️ Database Schema

### Current Tables

**users**
```
id          BIGINT PRIMARY KEY AUTO_INCREMENT
username    VARCHAR UNIQUE NOT NULL
email       VARCHAR UNIQUE NOT NULL
password    VARCHAR NOT NULL (BCrypt hashed)
roles       VARCHAR[] (ROLE_USER, ROLE_ADMIN)
created_at  TIMESTAMP
updated_at  TIMESTAMP
```

**categories**
```
id          BIGINT PRIMARY KEY AUTO_INCREMENT
name        VARCHAR(100) UNIQUE NOT NULL
```

### Planned Tables
- **products** - Product catalog with category relationships
- **cart** - Shopping cart management
- **orders** - Order processing and tracking
- **order_items** - Order line items

## 🔧 Prerequisites

- Java 21 or higher
- Maven 3.8+
- PostgreSQL 16+
- Docker & Docker Compose (optional, for containerized setup)

## 📦 Installation & Setup

### Option 1: Local Setup

#### 1. Clone the repository
```bash
git clone https://github.com/SyazwanIbn/gengshop.git
cd gengshop
```

#### 2. Configure PostgreSQL Database

Create a new database:
```sql
CREATE DATABASE gengshop;
CREATE USER gengshopuser WITH PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE gengshop TO gengshopuser;
```

#### 3. Update application.properties (if needed)

The default configuration connects to:
- **Host:** localhost
- **Port:** 5435
- **Database:** gengshop
- **Username:** gengshopuser
- **Password:** password

To change these, edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5435/gengshop
spring.datasource.username=your_username
spring.datasource.password=your_password
```

#### 4. Generate JWT Secret Key (Optional)

The project includes a default JWT secret key. For production, generate your own:
```bash
openssl rand -base64 32
```

Update in `application.properties`:
```properties
application.security.jwt.secret-key=your_generated_key
```

#### 5. Build and Run
```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`

### Option 2: Docker Setup

#### 1. Create `.env` file in project root
```env
POSTGRES_DB=gengshop
POSTGRES_USER=gengshopuser
POSTGRES_PASSWORD=password
```

#### 2. Start services
```bash
docker-compose up -d
```

This will start PostgreSQL on port 5435. The Spring Boot application needs to be run separately (local or add to docker-compose).

#### 3. Stop services
```bash
docker-compose down
```

#### 4. View logs
```bash
docker-compose logs -f db
```

## 📡 API Endpoints

### Authentication Endpoints

#### Register New User
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "password123"
}
```

**Response:**
```
Status: 200 OK
Body: "User Created Successfully"
```

#### User Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}
```

**Response:**
```
Status: 200 OK
Body: "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." (JWT Token)
```

### Category Endpoints (Coming Soon)

The following endpoints are planned:

```http
GET    /api/categories           # List all categories
GET    /api/categories/{id}      # Get category by ID
POST   /api/categories           # Create new category (Admin)
PUT    /api/categories/{id}      # Update category (Admin)
DELETE /api/categories/{id}      # Delete category (Admin)
```

## 🔐 Authentication

### Using JWT Token

After successful login, include the JWT token in subsequent requests:

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### Token Configuration
- **Expiration:** 15 minutes (900,000 ms)
- **Algorithm:** HS256 (HMAC with SHA-256)
- **Token Type:** Bearer

### Security Features
- Stateless session management
- BCrypt password hashing
- Role-based authorization
- Token expiration validation

## 🏗️ Project Structure

```
gengshop/
├── src/main/java/com/project/gengshop/
│   ├── config/              # Security & JWT configuration
│   │   ├── SecurityConfig.java
│   │   └── JwtAuthenticationFilter.java
│   ├── controller/          # REST API endpoints
│   │   └── AuthController.java
│   ├── dto/                 # Data Transfer Objects
│   │   ├── LoginRequestDto.java
│   │   └── RegisterRequestDto.java
│   ├── model/               # Entity classes
│   │   ├── User.java
│   │   ├── Category.java
│   │   └── Role.java
│   ├── repository/          # JPA Repositories
│   ├── service/             # Business logic
│   │   ├── AuthService.java
│   │   ├── JwtService.java
│   │   └── CustomUserDetailsService.java
│   └── GengshopApplication.java
├── src/main/resources/
│   └── application.properties
├── docker-compose.yml
├── Dockerfile
└── pom.xml
```

## 🛠️ Development

### Run in Development Mode

Spring Boot DevTools is included for hot reload:
```bash
mvn spring-boot:run
```

### View SQL Queries

SQL logging is enabled in development. Check console output for:
- Generated SQL statements (formatted)
- Parameter bindings
- Hibernate operations

### Access Database

**Using Docker:**
```bash
docker exec -it gengshop-db psql -U gengshopuser -d gengshop
```

**Using pgAdmin/DBeaver:**
- Host: localhost
- Port: 5435
- Database: gengshop
- Username: gengshopuser
- Password: password

## 🧪 Testing

Test dependencies are included in `pom.xml`:
- Spring Boot Test
- Spring Security Test
- JPA Test

Unit tests coming soon.

## 📝 Current Development Progress

**Estimated Completion: 40%**

- ✅ Project setup & configuration
- ✅ Database integration (PostgreSQL)
- ✅ JWT authentication implementation
- ✅ User registration & login
- ✅ Security configuration
- ✅ Category entity setup
- 🔄 Category CRUD operations (in progress)
- ⏳ Product management
- ⏳ Shopping cart
- ⏳ Order processing

## 🎯 Upcoming Features

1. **Complete Category Management** - Full CRUD with validation
2. **Product Module** - Product entity with category relationship
3. **Shopping Cart** - Add to cart, view cart, update quantities
4. **Order System** - Checkout, order creation, order history
5. **Search & Filters** - Product search, category filtering
6. **Pagination** - Efficient data retrieval for large datasets
7. **API Documentation** - Swagger/OpenAPI integration

## 👨‍💻 About This Project

This project is part of my portfolio demonstrating:
- RESTful API design principles
- Spring Boot ecosystem proficiency
- JWT authentication & Spring Security
- JPA/Hibernate entity relationships
- Clean code architecture
- Docker containerization
- Professional Git workflow

## 📧 Contact

**Muhammad Nursyazwan**
- Email: 28nursyazwan@gmail.com
- GitHub: [@SyazwanIbn](https://github.com/SyazwanIbn)
- LinkedIn: www.linkedin.com/in/nursyazwanmalek


---

**Note:** This is an active learning project. Features and documentation are continuously updated as development progresses.
