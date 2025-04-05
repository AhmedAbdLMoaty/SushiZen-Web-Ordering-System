# Sushi Zen API - Spring Boot Project

**Collecting Workspace Information**

This project implements a RESTful API for a sushi restaurant ordering system using Spring Boot, JPA, and Hibernate with an H2 database. The system follows a layered architecture pattern and provides functionality for user management, menu items, shopping cart, and order processing.

## Project Architecture

The project is structured according to the layered architecture pattern:

1. **Controller Layer** (controller)
   - Handles HTTP requests and responses
   - Maps REST endpoints defined in the OpenAPI specification
   - Validates input data

2. **Service Layer** (service)
   - Contains business logic
   - Performs conversions between DTOs and entity objects
   - Implements transaction management

3. **Repository Layer** (repository)
   - Interfaces extending JpaRepository for data access
   - Custom query methods for specific data operations

4. **Entity Layer** (entity)
   - JPA entity classes representing database tables
   - Entity relationships (One-to-Many, Many-to-One, Many-to-Many)

5. **DTO Layer** (`src/main/java/ism/lab02_ism/model/`)
   - Data Transfer Objects for API communication
   - Generated from OpenAPI specification

## Prerequisites

- JDK 17 or higher
- Maven 3.6 or higher
- Git (optional)

## How to Build and Run

### Clone the Repository (if using Git)
```bash
git clone https://github.com/AhmedAbdLMoaty/SushiZen-Web-Ordering-System.git
cd SushiZen-Web-Ordering-System
```

### Build the Project
```bash
mvn clean compile
```

### Run the Application
```bash
mvn spring-boot:run
```

The application will start on port 8080, and the H2 database will be initialized with sample data from the `DataInitializer` class.

## Testing the Endpoints

### Using Swagger UI
The API is documented with Swagger UI, accessible at:
```
http://localhost:8080/swagger-ui/index.html
```

### Using cURL

#### User Management
```bash
# Register a new user
curl -X POST http://localhost:8080/users/register \
  -H "Content-Type: application/json" \
  -d '{"userID":"user3","name":"Test User","email":"test3@example.com","phoneNumber":"123-456-7890","password":"password123","role":"Customer"}'

# Get all users
curl -X GET http://localhost:8080/users
```

#### Menu Management
```bash
# Get all menu items
curl -X GET http://localhost:8080/menu

# Get a specific menu item
curl -X GET http://localhost:8080/menu/item1

# Create a new menu item (admin)
curl -X POST http://localhost:8080/menu \
  -H "Content-Type: application/json" \
  -d '{"itemId":"item6","itemName":"Rainbow Roll","itemDescription":"Colorful roll with various fish","itemPrice":14.99,"itemPicture":"https://example.com/images/rainbow.jpg","available":true}'
```

#### Cart Management
```bash
# View user's cart
curl -X GET http://localhost:8080/cart

# Add item to cart
curl -X POST http://localhost:8080/cart \
  -H "Content-Type: application/json" \
  -d '{"itemId":"item1","quantity":2,"totalPrice":17.98}'

# Remove item from cart
curl -X DELETE http://localhost:8080/cart/item1
```

#### Order Management
```bash
# Place a new order
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{"userId":"1","items":[{"itemId":"item1","quantity":2,"totalPrice":17.98}],"status":"pending","paymentStatus":"paid"}'

# Get all orders
curl -X GET http://localhost:8080/orders

# Get a specific order
curl -X GET http://localhost:8080/orders/order1

# Update order status (kitchen staff)
curl -X PUT http://localhost:8080/kitchen/orders/order1/status \
  -H "Content-Type: application/json" \
  -d '{"status":"preparing"}'
```

## Database Schema

The H2 database implements the following entity relationships:

- `User` - Stores user information (customers, admin, kitchen staff)
- `MenuItem` - Contains details of menu items available for ordering
- `Cart` - Represents a user's shopping cart
- `CartItem` - Maps menu items to carts with quantities
- `Order` - Represents a customer order
- `OrderItem` - Maps menu items to orders with quantities

The project demonstrates both simple one-to-many relationships (User to Cart) and more complex many-to-many relationships with additional attributes (through CartItem and OrderItem junction entities).

## H2 Database Console

You can access the H2 database console at:
```
http://localhost:8080/h2-console
```

Connection details:
- JDBC URL: `jdbc:h2:./sushi`
- Username: (leave empty)
- Password: (leave empty)

## Key Implementation Features

1. **Entity-DTO Conversion**: Service layer handles conversion between entity objects and DTOs
2. **Data Initialization**: Sample data is loaded on application startup
3. **Transaction Management**: Using `@Transactional` annotations for consistent database operations
4. **Error Handling**: Proper HTTP status codes for different error scenarios
5. **OpenAPI Integration**: REST endpoints defined in OpenAPI specification (YAML)
