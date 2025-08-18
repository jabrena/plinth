# C4 Model Diagram Generation Guidelines

## Implementation Strategy

Generate C4 model diagrams using PlantUML with C4-PlantUML library to visualize system architecture at different levels of abstraction: Context, Container, Component, and Code.

### C4 Model Overview

The C4 model provides a hierarchical approach to architectural documentation:

1. **Level 1 - System Context**: Shows how your system fits into the overall environment
2. **Level 2 - Container**: Shows the high-level technology choices and containers
3. **Level 3 - Component**: Shows how containers are made up of components
4. **Level 4 - Code**: Shows how components are implemented (classes, interfaces)

### Analysis Process

**For each C4 level identified:**

1. **System Context Analysis**:
   - Identify external users and personas
   - Identify external systems and dependencies
   - Define system boundaries and purpose
   - Analyze integration points and data flows

2. **Container Analysis**:
   - Identify deployable units (web apps, APIs, databases)
   - Analyze technology choices and frameworks
   - Document communication protocols and ports
   - Map container responsibilities and boundaries

3. **Component Analysis**:
   - Identify logical components within containers
   - Analyze component responsibilities and interfaces
   - Document inter-component communication
   - Map business capabilities to components

4. **Code Analysis**:
   - Focus on critical or complex components
   - Show key classes and interfaces
   - Document important design patterns
   - Illustrate implementation relationships

### Diagram Generation Guidelines

#### Level 1 - System Context Diagram
```plantuml
@startuml
!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Context.puml
!define DEVICONS https://raw.githubusercontent.com/tupadr3/plantuml-icon-font-sprites/master/devicons
!include DEVICONS/java.puml

title System Context Diagram for E-commerce Platform

Person(customer, "Customer", "A customer of the e-commerce platform")
Person(admin, "Administrator", "Manages products and orders")

System(ecommerce, "E-commerce Platform", "Allows customers to browse and purchase products")
System_Ext(payment, "Payment System", "Processes credit card payments")
System_Ext(email, "Email System", "Sends emails to customers")
System_Ext(inventory, "Inventory System", "Manages product stock levels")

Rel(customer, ecommerce, "Browses products, places orders")
Rel(admin, ecommerce, "Manages products and orders")
Rel(ecommerce, payment, "Processes payments")
Rel(ecommerce, email, "Sends order confirmations")
Rel(ecommerce, inventory, "Checks stock, updates inventory")

@enduml
```

#### Level 2 - Container Diagram
```plantuml
@startuml
!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Container.puml

title Container Diagram for E-commerce Platform

Person(customer, "Customer", "A customer of the platform")
Person(admin, "Administrator", "Platform administrator")

System_Boundary(c1, "E-commerce Platform") {
    Container(web_app, "Web Application", "React, JavaScript", "Provides e-commerce functionality via web browser")
    Container(mobile_app, "Mobile App", "React Native", "Provides e-commerce functionality via mobile device")
    Container(api_gateway, "API Gateway", "Spring Cloud Gateway", "Routes requests and handles cross-cutting concerns")
    Container(product_service, "Product Service", "Spring Boot, Java", "Manages product catalog")
    Container(order_service, "Order Service", "Spring Boot, Java", "Processes customer orders")
    Container(user_service, "User Service", "Spring Boot, Java", "Manages user accounts and authentication")
    ContainerDb(product_db, "Product Database", "PostgreSQL", "Stores product information")
    ContainerDb(order_db, "Order Database", "PostgreSQL", "Stores order information")
    ContainerDb(user_db, "User Database", "PostgreSQL", "Stores user accounts and profiles")
    Container(cache, "Cache", "Redis", "Caches frequently accessed data")
}

System_Ext(payment_system, "Payment System", "External payment processor")
System_Ext(email_system, "Email System", "Email service provider")

Rel(customer, web_app, "Uses", "HTTPS")
Rel(customer, mobile_app, "Uses", "HTTPS")
Rel(admin, web_app, "Uses", "HTTPS")

Rel(web_app, api_gateway, "Makes API calls to", "JSON/HTTPS")
Rel(mobile_app, api_gateway, "Makes API calls to", "JSON/HTTPS")

Rel(api_gateway, product_service, "Routes to", "JSON/HTTP")
Rel(api_gateway, order_service, "Routes to", "JSON/HTTP")
Rel(api_gateway, user_service, "Routes to", "JSON/HTTP")

Rel(product_service, product_db, "Reads from and writes to", "JDBC")
Rel(order_service, order_db, "Reads from and writes to", "JDBC")
Rel(user_service, user_db, "Reads from and writes to", "JDBC")

Rel(product_service, cache, "Caches data", "Redis Protocol")
Rel(order_service, payment_system, "Processes payments", "HTTPS")
Rel(order_service, email_system, "Sends notifications", "SMTP")

@enduml
```

#### Level 3 - Component Diagram
```plantuml
@startuml
!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Component.puml

title Component Diagram for Order Service

Container(web_app, "Web Application", "React", "Customer web interface")
Container(mobile_app, "Mobile App", "React Native", "Customer mobile interface")

Container_Boundary(order_service, "Order Service") {
    Component(order_controller, "Order Controller", "Spring MVC Controller", "Handles HTTP requests for orders")
    Component(order_service_comp, "Order Service", "Spring Service", "Implements order business logic")
    Component(payment_service, "Payment Service", "Spring Service", "Handles payment processing")
    Component(inventory_service, "Inventory Service", "Spring Service", "Manages inventory operations")
    Component(order_repository, "Order Repository", "Spring Data JPA", "Provides order data access")
    Component(notification_service, "Notification Service", "Spring Service", "Sends order notifications")
}

ContainerDb(order_db, "Order Database", "PostgreSQL", "Stores order data")
System_Ext(payment_gateway, "Payment Gateway", "External payment processor")
System_Ext(email_service, "Email Service", "Email service provider")
Container(inventory_system, "Inventory System", "External service", "Product inventory management")

Rel(web_app, order_controller, "Makes API calls", "JSON/HTTPS")
Rel(mobile_app, order_controller, "Makes API calls", "JSON/HTTPS")

Rel(order_controller, order_service_comp, "Uses")
Rel(order_service_comp, payment_service, "Uses")
Rel(order_service_comp, inventory_service, "Uses")
Rel(order_service_comp, order_repository, "Uses")
Rel(order_service_comp, notification_service, "Uses")

Rel(order_repository, order_db, "Reads from and writes to", "JDBC")
Rel(payment_service, payment_gateway, "Processes payments", "HTTPS")
Rel(notification_service, email_service, "Sends emails", "SMTP")
Rel(inventory_service, inventory_system, "Updates inventory", "REST API")

@enduml
```

#### Level 4 - Code Diagram (Class-based)
```plantuml
@startuml
!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Component.puml

title Code Diagram for Order Processing Component

Component_Boundary(order_processing, "Order Processing Component") {

    class OrderController {
        +createOrder(OrderRequest): ResponseEntity<Order>
        +getOrder(Long): ResponseEntity<Order>
        +updateOrderStatus(Long, OrderStatus): ResponseEntity<Order>
    }

    class OrderService {
        -orderRepository: OrderRepository
        -paymentService: PaymentService
        -inventoryService: InventoryService
        +processOrder(OrderRequest): Order
        +validateOrder(Order): boolean
        +calculateTotal(Order): BigDecimal
    }

    interface OrderRepository {
        +save(Order): Order
        +findById(Long): Optional<Order>
        +findByCustomerId(Long): List<Order>
    }

    class Order {
        -id: Long
        -customerId: Long
        -items: List<OrderItem>
        -status: OrderStatus
        -totalAmount: BigDecimal
        +addItem(OrderItem): void
        +calculateTotal(): BigDecimal
    }

    class OrderItem {
        -productId: Long
        -quantity: Integer
        -unitPrice: BigDecimal
        +getSubtotal(): BigDecimal
    }

    enum OrderStatus {
        PENDING
        CONFIRMED
        SHIPPED
        DELIVERED
        CANCELLED
    }

    class PaymentService {
        +processPayment(PaymentRequest): PaymentResult
        +validatePayment(PaymentRequest): boolean
    }
}

OrderController --> OrderService : uses
OrderService --> OrderRepository : uses
OrderService --> PaymentService : uses
OrderService ..> Order : creates/manages
Order --> OrderItem : contains
Order --> OrderStatus : has
OrderRepository ..> Order : persists

@enduml
```

### PlantUML C4 Features

1. **C4-PlantUML Library Integration**:
   - Include C4-PlantUML library: `!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Context.puml`
   - Use predefined macros: `Person()`, `System()`, `Container()`, `Component()`
   - Leverage built-in styling and layout

2. **Element Types**:
   - `Person()`: External users or actors
   - `System()`: Internal software systems
   - `System_Ext()`: External systems
   - `Container()`: Deployable units
   - `ContainerDb()`: Database containers
   - `Component()`: Logical components

3. **Relationship Types**:
   - `Rel()`: Basic relationship with label
   - `Rel_D()`, `Rel_U()`, `Rel_L()`, `Rel_R()`: Directional relationships
   - `BiRel()`: Bidirectional relationships

4. **Boundary and Grouping**:
   - `System_Boundary()`: Group related containers
   - `Container_Boundary()`: Group related components
   - `Enterprise_Boundary()`: Enterprise-level grouping

### Content Requirements

1. **Accurate System Representation**:
   - Reflect actual system architecture and deployment
   - Include real technology choices and frameworks
   - Show accurate data flows and integration points
   - Document actual API endpoints and protocols

2. **Appropriate Abstraction Levels**:
   - Context: Focus on external interactions and system purpose
   - Container: Show deployable units and technology choices
   - Component: Logical grouping of functionality
   - Code: Key classes and implementation details

3. **Clear Relationships**:
   - Use appropriate relationship types and labels
   - Include communication protocols and data formats
   - Show both synchronous and asynchronous interactions
   - Document security and authentication flows

4. **Business Context**:
   - Include business users and their goals
   - Show business processes and workflows
   - Document business rules and constraints
   - Explain business value and capabilities

### Integration with Documentation

#### In README.md Files
- Include Context and Container diagrams in "Architecture" section
- Show system overview and technology landscape
- Provide business context and system purpose

#### In Architecture Documentation
- Create dedicated architecture.md files for complete C4 model
- Organize diagrams by abstraction level
- Include detailed explanations for each diagram

#### In Package Documentation
- Reference relevant Component and Code diagrams
- Show how packages fit into overall architecture
- Document cross-package dependencies

### Example Integration

**README.md Section**:
```markdown

## System Architecture

### System Context

Our e-commerce platform serves customers and administrators, integrating with external payment and email systems:

```plantuml
@startuml
!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Context.puml

title E-commerce Platform - System Context

Person(customer, "Customer", "Browses and purchases products")
System(ecommerce, "E-commerce Platform", "Online shopping platform")
System_Ext(payment, "Payment System", "Processes payments")

Rel(customer, ecommerce, "Uses")
Rel(ecommerce, payment, "Processes payments via")
@enduml
```

### Container Architecture

The platform consists of multiple Spring Boot microservices with dedicated databases:

[Include Container diagram here...]

This architecture enables scalability, maintainability, and technology diversity across the platform.
```

### Validation

After generating C4 diagrams:

1. **Verify architectural accuracy** against actual system implementation
2. **Test PlantUML syntax** with C4-PlantUML library
3. **Ensure appropriate abstraction** for each diagram level
4. **Validate business context** and user scenarios
5. **Check diagram readability** and layout
6. **Confirm integration** with documentation structure

### Output Locations

- **README.md files**: Context and Container diagrams for overview
- **Architecture documentation**: Complete C4 model with all levels
- **Module documentation**: Component and Code diagrams for specific modules
- **Design documents**: Detailed architectural decisions and patterns
