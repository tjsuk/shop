# Shop REST API Project

## Introduction

This project is a Java-based REST API developed using Spring Boot. The application demonstrates the construction of a layered enterprise-style application using modern Java development practices.

The system manages a collection of products and exposes a RESTful API that allows products to be:

- Retrieved
- Created
- Updated
- Deleted
- Filtered by price

The project has been designed as a learning exercise for students who are developing skills in:

- Java 25
- Spring Boot
- REST API development
- Spring MVC
- Spring Data JPA
- Validation
- Testing with MockMvc
- Gradle Kotlin DSL
- H2 in-memory databases

---

# Technologies Used

| Technology | Purpose |
|---|---|
| Java 25 | Primary programming language |
| Spring Boot 4 | Application framework |
| Spring MVC | REST controller layer |
| Spring Data JPA | Database abstraction and repository layer |
| Hibernate ORM | Object relational mapping |
| H2 Database | In-memory development database |
| Gradle Kotlin DSL | Build automation |
| MockMvc | REST API testing |
| Jakarta Validation | Validation of incoming request data |
| Spring Actuator | Monitoring and diagnostics |
| IntelliJ IDEA 2026.1.1 | Recommended IDE |

---

# Project Architecture

The project follows a layered architecture.

```text
Client
   ↓
Controller Layer
   ↓
Service Layer
   ↓
Repository Layer
   ↓
Database
```

---

# Package Structure

```text
org.acme.shop
│
├── controllers
│   ├── ProductRestController
│   └── ValidationExceptionHandler
│
├── services
│   └── ProductService
│
├── dao
│   └── ProductRepository
│
├── entities
│   └── Product
│
└── ShopApplication
```

---

# Key Concepts Demonstrated

## 1. REST APIs

The project demonstrates how HTTP endpoints are mapped using:

```java
@GetMapping
@PostMapping
@PutMapping
@DeleteMapping
```

The API exposes endpoints such as:

| Method | Endpoint | Description |
|---|---|---|
| GET | /products | Returns all products |
| GET | /products/{id} | Returns a single product |
| POST | /products | Creates a product |
| PUT | /products/{id} | Updates a product |
| DELETE | /products/{id} | Deletes a product |
| DELETE | /products | Deletes all products |

---

## 2. Dependency Injection

Spring Boot automatically creates and injects dependencies.

Example:

```java
private final ProductService service;

public ProductRestController(ProductService service) {
    this.service = service;
}
```

This demonstrates constructor injection.

---

## 3. Spring Data JPA

The repository layer extends:

```java
JpaRepository<Product, Long>
```

This automatically provides:

- save()
- findAll()
- findById()
- deleteById()
- deleteAll()
- existsById()

The project also demonstrates derived query methods:

```java
findByPriceGreaterThanEqual(BigDecimal min)
```

Spring generates the SQL automatically based on the method name.

---

## 4. Validation

The application validates incoming request data.

Example:

```java
@DecimalMin("0.00")
```

This ensures negative prices cannot be submitted.

The project also demonstrates:

- @Validated
- @Valid
- ConstraintViolationException
- Global exception handling

---

## 5. Exception Handling

The application demonstrates centralised exception handling using:

```java
@RestControllerAdvice
```

This allows validation exceptions to be translated into:

```text
400 Bad Request
```

instead of application crashes.

---

## 6. MockMvc Testing

The project demonstrates REST API testing using:

```java
MockMvc
```

This allows endpoints to be tested without deploying the application to a real server.

Example:

```java
mockMvc.perform(get("/products"))
       .andExpect(status().isOk());
```

---

# Database

The application uses an H2 in-memory database.

Benefits:

- No external database installation required
- Fast startup
- Ideal for testing and student projects
- Automatically resets between runs

The database is populated using:

```java
initialiseDatabase()
```

inside the service layer.

---

# Gradle Kotlin DSL

The project uses:

```text
build.gradle.kts
```

instead of the older Groovy Gradle syntax.

Important dependencies include:

```kotlin
implementation("org.springframework.boot:spring-boot-starter-webmvc")
implementation("org.springframework.boot:spring-boot-starter-data-jpa")
implementation("org.springframework.boot:spring-boot-starter-validation")
runtimeOnly("com.h2database:h2")
```

---

# Running the Application

## Requirements

Students should have:

- Java 25 installed
- IntelliJ IDEA 2026.1.1 or later
- Gradle support enabled

---

## Opening the Project

1. Open IntelliJ IDEA
2. Select:

```text
Open Project
```

3. Choose the root project folder
4. Allow Gradle to import dependencies
5. Wait for indexing to complete

---

# Running the Application

Run:

```text
ShopApplication
```

Spring Boot will start the embedded server.

Default server:

```text
http://localhost:8080
```

---

# Example Requests

## Retrieve all products

```http
GET /products
```

---

## Retrieve product by ID

```http
GET /products/1
```

---

## Create product

```http
POST /products
Content-Type: application/json

{
  "name": "Laptop",
  "price": 999.99
}
```

---

## Update product

```http
PUT /products/1
Content-Type: application/json

{
  "name": "Updated Laptop",
  "price": 1099.99
}
```

---

## Delete product

```http
DELETE /products/1
```

---

# Running Tests

Tests are located inside:

```text
src/test/java
```

Run tests using:

```text
./gradlew test
```

or directly inside IntelliJ IDEA.

---

# Important Learning Outcomes

After completing this project, students should understand:

- How REST APIs work
- HTTP methods and status codes
- Layered application architecture
- Spring dependency injection
- Repository patterns
- Validation and exception handling
- Integration testing with MockMvc
- Using Gradle Kotlin DSL
- Working with H2 databases
- Spring Boot auto-configuration

---

# Common Issues Students May Encounter

## 1. Dependency Problems

Always refresh Gradle after modifying dependencies.

Inside IntelliJ:

```text
Gradle Window → Refresh
```

---

## 2. Port Already In Use

If port 8080 is already in use:

```properties
server.port=8081
```

inside:

```text
application.properties
```

---

## 3. Validation Exceptions

Validation errors returning:

```text
400 Bad Request
```

are expected behaviour.

---

## 4. MockMvc Autowiring Issues

Ensure:

```java
@AutoConfigureMockMvc
```

is imported from:

```java
org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
```

---

# Suggested Student Exercises

Students should attempt the following extensions:

1. Add product categories
2. Add pagination
3. Add sorting
4. Add Swagger/OpenAPI documentation
5. Add authentication
6. Add persistent databases such as PostgreSQL or SQL Server
7. Add DTO objects
8. Add service-level validation
9. Add logging
10. Add unit testing with Mockito

---

# Conclusion

This project demonstrates many of the core concepts used in modern enterprise Java development.

Although the application is relatively small, it introduces students to real-world development practices including layered architecture, REST API development, validation, testing and persistence.

Mastering these concepts provides an excellent foundation for larger Spring Boot systems and microservice-based architectures.

