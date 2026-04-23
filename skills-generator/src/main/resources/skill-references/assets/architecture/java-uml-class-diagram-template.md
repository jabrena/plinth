# UML Class Diagram Generation Guidelines

## Implementation Strategy

Generate UML class diagrams using PlantUML syntax to illustrate the structure, relationships, and design patterns within Java packages and modules.

### Analysis Process

**For each package or module identified:**

1. **Identify class types and categories**:
   - Domain entities and value objects
   - Service classes and business logic
   - Repository and data access classes
   - Controller and API classes
   - Configuration and utility classes
   - Interfaces and abstract classes

2. **Analyze class relationships**:
   - Inheritance hierarchies (extends, implements)
   - Composition and aggregation relationships
   - Dependencies and associations
   - Interface implementations

3. **Determine diagram scope** based on user selection:
   - **All packages**: Complete class structure overview
   - **Core business logic**: Domain models and business services
   - **Specific packages**: User-selected packages for detailed analysis

### Diagram Generation Guidelines

#### Basic Class Structure
```plantuml
@startuml
!theme plain
title Package Class Structure

class Entity {
    - id: Long
    - name: String
    - createdAt: LocalDateTime
    + getId(): Long
    + getName(): String
    + setName(String): void
}

interface Repository {
    + findById(Long): Optional<Entity>
    + save(Entity): Entity
    + deleteById(Long): void
}

class EntityService {
    - repository: Repository
    + EntityService(Repository)
    + findById(Long): Entity
    + save(Entity): Entity
    + delete(Long): void
}

class EntityController {
    - service: EntityService
    + EntityController(EntityService)
    + getEntity(Long): ResponseEntity<Entity>
    + createEntity(Entity): ResponseEntity<Entity>
    + deleteEntity(Long): ResponseEntity<Void>
}

EntityService --> Repository : uses
EntityController --> EntityService : uses
Repository ..> Entity : manages
EntityService ..> Entity : processes
EntityController ..> Entity : exposes
@enduml
```

#### Advanced Patterns

**Domain Model with Inheritance**:
```plantuml
@startuml
!theme plain
title Domain Model Hierarchy

abstract class BaseEntity {
    # id: Long
    # createdAt: LocalDateTime
    # updatedAt: LocalDateTime
    + getId(): Long
    + getCreatedAt(): LocalDateTime
    {abstract} + validate(): boolean
}

class User extends BaseEntity {
    - username: String
    - email: String
    - password: String
    + getUsername(): String
    + getEmail(): String
    + validate(): boolean
}

class Product extends BaseEntity {
    - name: String
    - price: BigDecimal
    - category: Category
    + getName(): String
    + getPrice(): BigDecimal
    + validate(): boolean
}

enum Category {
    ELECTRONICS
    CLOTHING
    BOOKS
    + getDisplayName(): String
}

class Order extends BaseEntity {
    - user: User
    - items: List<OrderItem>
    - totalAmount: BigDecimal
    + addItem(OrderItem): void
    + calculateTotal(): BigDecimal
    + validate(): boolean
}

class OrderItem {
    - product: Product
    - quantity: Integer
    - unitPrice: BigDecimal
    + getSubtotal(): BigDecimal
}

User ||--o{ Order : places
Order ||--o{ OrderItem : contains
OrderItem }o--|| Product : references
Product }o--|| Category : belongs to

note top of BaseEntity : All entities extend this base class\nfor common functionality

note right of Order : Calculates total from\ncontained OrderItems
@enduml
```

**Service Layer Architecture**:
```plantuml
@startuml
!theme plain
title Service Layer Architecture

package "Controller Layer" {
    class UserController {
        - userService: UserService
        + getUsers(): List<UserDTO>
        + createUser(UserDTO): UserDTO
        + updateUser(Long, UserDTO): UserDTO
    }

    class OrderController {
        - orderService: OrderService
        + getOrders(): List<OrderDTO>
        + createOrder(OrderDTO): OrderDTO
    }
}

package "Service Layer" {
    interface UserService {
        + findAll(): List<User>
        + findById(Long): User
        + save(User): User
        + update(Long, User): User
    }

    class UserServiceImpl implements UserService {
        - userRepository: UserRepository
        - userMapper: UserMapper
        + findAll(): List<User>
        + save(User): User
    }

    interface OrderService {
        + createOrder(Order): Order
        + processOrder(Long): void
    }

    class OrderServiceImpl implements OrderService {
        - orderRepository: OrderRepository
        - userService: UserService
        - emailService: EmailService
        + createOrder(Order): Order
    }
}

package "Repository Layer" {
    interface UserRepository {
        + findAll(): List<User>
        + findById(Long): Optional<User>
        + save(User): User
    }

    interface OrderRepository {
        + save(Order): Order
        + findByUserId(Long): List<Order>
    }
}

package "Infrastructure" {
    class EmailService {
        + sendOrderConfirmation(Order): void
    }

    class UserMapper {
        + toDTO(User): UserDTO
        + toEntity(UserDTO): User
    }
}

UserController --> UserService : uses
OrderController --> OrderService : uses
UserServiceImpl --> UserRepository : uses
OrderServiceImpl --> OrderRepository : uses
OrderServiceImpl --> UserService : uses
OrderServiceImpl --> EmailService : uses
UserServiceImpl --> UserMapper : uses
@enduml
```

### PlantUML-Specific Features for Class Diagrams

1. **Visibility Modifiers**:
   - `+` for public
   - `-` for private
   - `#` for protected
   - `~` for package-private

2. **Relationship Types**:
   - `-->` : Association
   - `--` : Association (bidirectional)
   - `<|--` : Inheritance/Extension
   - `<|..` : Interface Implementation
   - `*--` : Composition
   - `o--` : Aggregation
   - `..>` : Dependency

3. **Advanced Features**:
   - `{abstract}` for abstract classes/methods
   - `{static}` for static members
   - `<<interface>>` or `interface` keyword
   - `enum` for enumerations
   - `note` for annotations and comments

4. **Styling and Organization**:
   - `package` for logical grouping
   - `!theme` for consistent styling
   - `title` for diagram context
   - Colors and stereotypes for categorization

### Content Requirements

1. **Accurate Structure Representation**:
   - Include actual class names, methods, and attributes from codebase
   - Show correct visibility modifiers
   - Represent accurate inheritance and interface relationships
   - Include important annotations (e.g., @Entity, @Service, @Controller)

2. **Meaningful Relationships**:
   - Show composition vs aggregation appropriately
   - Include important dependencies between classes
   - Demonstrate design patterns (Strategy, Factory, Observer, etc.)
   - Show package boundaries and layered architecture

3. **Appropriate Level of Detail**:
   - Include key methods and attributes
   - Avoid cluttering with trivial getters/setters unless important
   - Focus on business logic and architectural significance
   - Show method signatures for important operations

4. **Clear Organization**:
   - Group related classes using packages
   - Use consistent naming conventions
   - Add notes for complex relationships or business rules
   - Organize layout for readability

### Integration with Documentation

#### In README.md Files
- Include class diagrams in "Architecture" or "Design" sections
- Show high-level package relationships and key design patterns
- Provide context explaining the architectural decisions

#### In package-info.java Files
- Reference class diagrams that illustrate package structure
- Include simplified ASCII versions for basic relationships
- Link to external diagram files for complex structures

#### Separate Documentation Files
- Create dedicated architecture.md files for complex systems
- Organize diagrams by business domain or technical layer
- Include both overview and detailed diagrams

### Example Integration

**README.md Section**:
```markdown

## System Architecture

### Domain Model

The following class diagram shows the core domain entities and their relationships:

```plantuml
@startuml
!theme plain
title Core Domain Model

class User {
    - id: Long
    - username: String
    - email: String
    + getOrders(): List<Order>
}

class Order {
    - id: Long
    - orderDate: LocalDateTime
    - status: OrderStatus
    + addItem(Product, Integer): void
    + calculateTotal(): BigDecimal
}

class Product {
    - id: Long
    - name: String
    - price: BigDecimal
    + isAvailable(): boolean
}

enum OrderStatus {
    PENDING
    CONFIRMED
    SHIPPED
    DELIVERED
}

User ||--o{ Order : places
Order }o--o{ Product : contains
Order ||--|| OrderStatus : has
@enduml
```

This diagram illustrates the core business entities and their relationships, showing how users place orders containing products.
```

### Validation

After generating class diagrams:

1. **Verify accuracy** against actual codebase structure
2. **Test PlantUML syntax** for proper rendering
3. **Ensure relationship correctness** (inheritance, composition, etc.)
4. **Validate completeness** of important classes and relationships
5. **Check diagram readability** and appropriate level of detail

### Output Locations

- **README.md files**: Include architectural overview diagrams
- **Package-specific .md files**: Detailed diagrams for complex packages
- **Documentation directories**: Organize in docs/diagrams/ or architecture/ folders
- **Inline documentation**: Simple diagrams in package-info.java files
