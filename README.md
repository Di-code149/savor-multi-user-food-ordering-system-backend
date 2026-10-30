# Savor Multi User Food Ordering System Backend

**Course:** Web Technology and Internet · AUCA  
**Student:** Divine MUHOZA  
**Student ID:** 27693  
**Project:** Savor Multi User Food Ordering System (Mid-Term Examination)  

---

## 1. Project Overview

Savor digitises food ordering in a restaurant, managing operations from customer orders to the kitchen and delivery. 

### Domain Model & Architecture
- **`Customer`**: The customer ordering food. Identified by unique business key `username`.
- **`Cook`**: Kitchen staff handling orders. Trained in multiple menu sections and assigned to a kitchen station.
- **`KitchenStation`**: Dedicated kitchen work area identified by `stationNumber` (knows nothing of its cook).
- **`MenuSection`**: Classification of menu offerings (e.g. Grills, Local Dishes, Pizza, Desserts). Cook holds a Many-to-Many relation to MenuSection (`cook_menu_section` join table), while MenuSection marks `cooks` with `@JsonIgnore` to prevent recursive JSON loops.
- **`FoodOrder`**: The dated transaction containing items, total amount, delivery address, status (`PENDING`, `PREPARING`, `DELIVERED`, `CANCELLED`), assigned Cook, and Customer.
- **`Review`**: Feedback recorded exclusively for `DELIVERED` food orders.

---

## 2. Technology Stack & Configuration

- **Language & Runtime:** Java 17
- **Framework:** Spring Boot, Spring Data JPA, Spring Web MVC
- **Database:** PostgreSQL (`savor_db`)
- **Build Tool:** Apache Maven
- **Port:** `8080` (Base URL: `http://localhost:8080`)

### Database Configuration (`src/main/resources/application.properties`)
```properties
spring.application.name=savor-multi-user-food-ordering-system

spring.datasource.url=jdbc:postgresql://localhost:5432/savor_db
spring.datasource.username=postgres
spring.datasource.password=12345
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true
```

---

## 3. How to Run the Application

### Prerequisites
1. PostgreSQL running locally on port `5432` with a database named `savor_db`.
2. Java 17 (JDK) installed.
3. Maven installed (or using the included Maven Wrapper).

### Steps to Run
1. Ensure the PostgreSQL database `savor_db` exists:
   ```bash
   psql -U postgres -c "CREATE DATABASE savor_db;"
   ```
2. Navigate to the project root directory:
   ```bash
   cd savor-multi-user-food-ordering-system
   ```
3. Run the Spring Boot application using Maven:
   ```bash
   mvn spring-boot:run
   ```
   Or on Windows Command Prompt / PowerShell:
   ```cmd
   .\mvnw.cmd spring-boot:run
   ```
4. The server will start on port `8080`.

---

## 4. Postman Collection & Testing

The repository includes the complete Postman collection at the root:  
**`savor-multi-user-food-ordering-system-midterm.postman_collection.json`**

### Test Execution Procedure
1. Import `savor-multi-user-food-ordering-system-midterm.postman_collection.json` into Postman.
2. Run **Folder 0: Seed data (Step 0, run once)** on an empty database. This seeds:
   - 4 MenuSections: Grills, Local Dishes, Pizza, Desserts (Desserts unassigned).
   - 4 KitchenStations: Stations 1, 2, 3, 4 (Station 4 unassigned).
   - 3 Cooks: assigned to stations and menu sections.
   - 5 Customers: including two with last name `Muhoza` and the primary username `divine27`.
   - 10 Food Orders: across statuses with appropriate dates.
3. Run each folder in sequence:
   - **Part A:** Domain model & CRUD
   - **Part B:** Derived queries
   - **Part C:** JPQL queries with joins
   - **Part D:** Business rules & validations
   - **Part E:** Review entity & protected order deletion
   - **Part F:** Reports & bulk updates
   - **Bonus:** Dashboard summary & pagination

---

## 5. API Endpoints Reference

### Part A: CRUD Endpoints
| Requirement | Method | Endpoint | Success / Error |
| :--- | :--- | :--- | :--- |
| **A2** | `POST` | `/api/{path}/save` | `201 Created` — `"<Entity> saved successfully"` |
| **A2** | `GET` | `/api/{path}/all` | `200 OK` — List of entities |
| **A2** | `GET` | `/api/{path}/{id}` | `200 OK` or `404 Not Found` |
| **A2** | `PUT` | `/api/{path}/update/{id}` | `200 OK` or `404 Not Found` |
| **A2** | `DELETE`| `/api/{path}/delete/{id}` | `200 OK` or `404 Not Found` |
*`{path}` corresponds to `menu-sections`, `kitchen-stations`, `cooks`, `customers`, and `food-orders`.*

### Part B: Derived Queries
| ID | Method | Endpoint | Query Type | Description |
| :--- | :--- | :--- | :--- | :--- |
| **B1** | `GET` | `/api/customers/by-last-name?lastName=muhoza` | DERIVED | `findByLastNameIgnoreCaseOrderByFirstNameAsc` |
| **B2** | `GET` | `/api/food-orders/by-status?status=PENDING` | DERIVED | `findByStatusOrderByOrderDateAsc` |
| **B3** | `GET` | `/api/food-orders/between?start=...&end=...` | DERIVED | `findByOrderDateBetweenOrderByOrderDateAsc` |

### Part C: JPQL with Joins
| ID | Method | Endpoint | Query Type | Description |
| :--- | :--- | :--- | :--- | :--- |
| **C1** | `GET` | `/api/cooks/by-menu-section?name=grills` | JPQL | Join collection with `LOWER` |
| **C2** | `GET` | `/api/kitchen-stations/free` | JPQL | `NOT EXISTS (SELECT c FROM Cook c WHERE c.kitchenStation = k)` |
| **C3** | `GET` | `/api/menu-sections/unused` | JPQL | `WHERE m.cooks IS EMPTY` |
| **C4** | `GET` | `/api/customers/of-cook/{cookId}` | JPQL | `SELECT DISTINCT o.customer FROM FoodOrder o` (404 if cook doesn't exist) |

### Part D: Business Rules
| ID | Method | Endpoint | Description |
| :--- | :--- | :--- | :--- |
| **D1** | `POST` | `/api/food-orders/save` | Rejects past dates (400) and duplicate non-cancelled orders for cook on that date (409); forces status to `PENDING` |
| **D2** | `PATCH`| `/api/food-orders/{id}/status?status=...` | Enforces transition state machine: `PENDING` -> `PREPARING`/`CANCELLED`; `PREPARING` -> `DELIVERED`/`CANCELLED` |
| **D3** | `PUT` | `/api/cooks/{cookId}/menu-sections/{msId}` | Adds menu section to cook's collection; returns 409 if cook already has it |
| **D4** | `PATCH`| `/api/cooks/{cookId}/kitchen-station?stationNumber=...` | Assigns cook to kitchen station; returns 409 if taken by another cook |

### Part E: Review Entity
| ID | Method | Endpoint | Query Type | Description |
| :--- | :--- | :--- | :--- | :--- |
| **E1** | `POST` | `/api/reviews/save?foodOrderId=...` | Business Rule | Writes review for `DELIVERED` orders; sets `reviewDate` to today |
| **E2** | `GET` | `/api/reviews/customer/{username}` | JPQL | Navigates `r.foodOrder.customer.username ORDER BY r.reviewDate DESC` |
| **E3** | `DELETE`| `/api/reviews/delete/{id}` | CRUD | Deletes review |
| **E3** | `DELETE`| `/api/food-orders/delete/{id}` | DERIVED | Blocks deletion (409) if food order has associated reviews |

### Part F: Reports and Bulk Updates
| ID | Method | Endpoint | Query Type | Description |
| :--- | :--- | :--- | :--- | :--- |
| **F1** | `GET` | `/api/food-orders/stats/by-status` | JPQL | `GROUP BY o.status` with `COUNT(o)` |
| **F2** | `PATCH`| `/api/food-orders/cancel-day?cookId=...&date=...` | JPQL | Bulk update (`@Modifying`, `@Transactional`) non-delivered orders |

### Bonus (+10 Marks)
| ID | Method | Endpoint | Query Type | Description |
| :--- | :--- | :--- | :--- | :--- |
| **+5** | `GET` | `/api/dashboard/summary` | Reused / Derived | Returns JSON map: `customers`, `cooks`, `freeKitchenStations`, `ordersToday`, `pending`, `reviews` |
| **+5** | `GET` | `/api/food-orders/page?page=0&size=5&sort=orderDate,desc` | Pageable | Returns `Page<FoodOrder>` with `totalPages` and `totalElements` |

---

## 6. Live Demo Guidance

During the examination demo:
- **Success Case:** Show the request returning `200 OK` or `201 Created` with expected JSON payload.
- **Error Case:** Show the required validation trigger (`400 Bad Request`, `404 Not Found`, or `409 Conflict`).
- **Code Explanation:** Highlight whether the solution is a pure **DERIVED query** (method name in repository without `@Query`), a **JPQL query** (explicit `@Query`), or a **Service Business Rule**.
