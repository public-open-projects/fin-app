Here is the updated version of the coding conventions file with **Moduliths** and **Domains** included, while keeping the rest intact:

```markdown
# Coding Conventions for Aider Spring Boot Project

## General Guidelines
1. **Spring Boot Version**: Use **Spring Boot 3.3**.
2. **Java Version**: Use **Java 21** with:
   - **Records** for immutable data classes.
   - **Guarded patterns** in `switch` expressions.
   - **Sealed classes** for defining closed hierarchies.
   - **Virtual threads** (Project Loom) enabled by default for concurrency.
3. **Deployment**:
   - Use **GraalVM** for ahead-of-time (AOT) compilation to native images.
   - Deploy applications using **containerized environments** (e.g., Docker, Kubernetes).
4. **Architecture**: Follow **Domain-Driven Design (DDD)** principles with **Moduliths**.
5. **Documentation**:
   - Use **OpenAPI** for API documentation.
   - Adhere to **API-first** design principles when designing REST endpoints.

---

## Project Structure
Adopt a clean, **DDD-compliant layered structure** with **Moduliths**:

```
src/
  main/
    java/
      com.yourcompany.project/
        application/  # Application Layer (Use Cases, Services)
        domain/       # Domain Layer (Entities, Repositories)
          user/       # Example Domain (User)
          order/      # Example Domain (Order)
        interfaces/   # Interface Layer (REST API, Controllers)
        infrastructure/  # Infrastructure Layer (Database, Providers)
          persistence/  # Database Repositories and Configurations
          messaging/    # Messaging and Event Handlers
  test/
    java/
      com.yourcompany.project/
        application/  # Unit and Integration tests for Application Layer
        domain/       # Unit and Integration tests for Domain Layer
        interfaces/   # Unit and Integration tests for Interface Layer
        infrastructure/  # Tests for Infrastructure Layer
```

### 1. **Moduliths Structure**
In this architecture, the **Moduliths** approach is followed, where the system is split into modular **domains**. Each domain contains its own **domain models**, **services**, and **repositories**. These domains are loosely coupled and interact through well-defined interfaces.

Example Domains:
- **user**: Handles user-related logic, such as registration, authentication, and management.
- **order**: Manages order creation, tracking, and invoicing.

Each domain should be self-contained with its own logic, entities, and repository.

### 2. **Application Layer**
- **Purpose**: Implements business use cases and orchestrates workflow logic.
- **Components**:
  - **Service**: Core logic encapsulated in services.
  - **DTOs**: Transfer data between layers.
  - **Use Case Classes**: Encapsulate reusable workflows using patterns like **Command** or **Template Method**.
- **Annotations**:
  - Custom annotation `@ApplicationService` (wraps `@Service`).

### 3. **Interface Layer**
- **Purpose**: Exposes APIs to external clients (REST, GraphQL, etc.).
- **Components**:
  - **Controllers**: Annotated with `@RestController` or `@Controller`.
  - Custom annotation `@ApiController` to extend `@RestController`.

### 4. **Domain Layer**
- **Purpose**: Encapsulates the core domain logic and enforces business rules.
- **Components**:
  - **Entities**: Entities contain both state and business logic (rich domain models). Avoid anemic entities.
  - **Value Objects**: For immutable, logically grouped values.
  - **Repositories**: Interfaces for database interaction, implemented in the infrastructure layer.
  - **Domain Services**: Encapsulate domain logic that doesn't fit in entities.
  - **Factories/Builders**: Encapsulate complex creation logic for entities or aggregates.
- **Annotations**:
  - Custom annotation `@DomainEntity` to document entities.
  - Custom annotation `@ValueObject` to document value objects.

### 5. **Infrastructure Layer**
- **Purpose**: Provides concrete implementations of domain interfaces.
- **Components**:
  - External system integrations (e.g., APIs, third-party services).
  - **Persistence**: Database clients (e.g., repositories, custom queries).
  - **Messaging/Events**: Implementation of event-driven architecture.
  - **Service Providers**: Implementations for domain service interfaces.
- **Annotations**:
  - Custom annotation `@InfrastructureComponent` for infrastructure services.

---

## Dependency Injection
- Use **full dependency injection** through annotations:
  - `@Component`, `@Service`, `@Repository`, etc.
  - **Constructor injection** should always be preferred over field injection.

---

## Profiles
- **Environment-Specific Configurations**:
  - `application-dev.yml`
  - `application-test.yml`
  - `application-prod.yml`
- Use **Spring's `@Profile` annotation** to define beans for specific environments.

---

## Coding Standards
### Java 21 Features
1. **Records**: Use for immutable data models in the application layer.
2. **Guarded Patterns**: Use in `switch` expressions to simplify condition checks.
3. **Sealed Classes**: Use for defining limited hierarchies in the domain layer.
4. **Virtual Threads**: Enable by default using configuration:
   ```java
   ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
   ```

### Design Patterns
- **Command Pattern** for encapsulating use cases:
  ```java
  public interface UseCaseCommand<T> {
      T execute();
  }
  ```
- **Template Method Pattern** for reusable algorithmic steps:
  ```java
  public abstract class BaseTemplate {
      public final void execute() {
          step1();
          step2();
      }
      protected abstract void step1();
      protected abstract void step2();
  }
  ```

---

## Development Practices
### Test-Driven Development (TDD)
- Write unit tests first.
- Use **JUnit 5** and **Mockito** for unit testing.
- Aim for **100% code coverage** wherever possible.
- Mock infrastructure dependencies (e.g., database, external services).

### OpenAPI Integration
- Use **springdoc-openapi** for API documentation:
  ```xml
  <dependency>
      <groupId>org.springdoc</groupId>
      <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
      <version>2.0.4</version>
  </dependency>
  ```

### Event Handling
- Use **Spring Integration** for handling events.
- Publish and listen to domain events using annotations:
  ```java
  @EventListener
  public void handleDomainEvent(DomainEvent event) {
      // Handle event
  }
  ```

---

## Rich Domain Models (No Anemic Entities)
Entities must contain both **data** and **behavior** to ensure rich domain logic. Repositories should be injected directly into entities when necessary, allowing the domain logic to evolve naturally and remain consistent.

### Example of Rich Entity with Logic

```java
@DomainEntity
public class User {
    private final String id;
    private final String name;
    private final String email;
    private final UserRepository userRepository;

    public User(String id, String name, String email, UserRepository userRepository) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.userRepository = userRepository;
    }

    public void updateEmail(String newEmail) {
        // Business rule: email must be valid
        if (!newEmail.contains("@")) {
            throw new IllegalArgumentException("Invalid email address");
        }
        this.email = newEmail;
        userRepository.save(this);
    }

    public String getName() {
        return name;
    }
}
```

In this example:
- **Business logic** (`updateEmail()`) resides directly within the entity.
- **Repository injection** (`UserRepository`) allows the entity to persist changes.

### Example of Domain Service
In some cases, domain services are used to encapsulate logic that doesn't belong directly within an entity.

```java
public class UserRegistrationService {
    private final UserRepository userRepository;

    public UserRegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerNewUser(String name, String email) {
        User user = new User(UUID.randomUUID().toString(), name, email, userRepository);
        userRepository.save(user);
        return user;
    }
}
```

### Repository Interface Example

```java
@Repository
public interface UserRepository {
    void save(User user);
    Optional<User> findById(String id);
}
```

---

## GraalVM Configuration
1. Use **spring-native** or GraalVM’s native image capabilities.
2. Configure AOT settings in `application.yml` (please use .properties not yml):
   ```yaml
   spring:
     native:
       build:
         enabled: true
   ```

---

## Example Conventions in Action

### Example Record for DTO
```java
public record UserDto(String id, String name, String email) {}
```

### Example Use Case
```java
@ApplicationService
public class RegisterUserUseCase implements UseCaseCommand<UserDto> {

    private final UserRepository userRepository;

    public RegisterUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto execute() {
        // Business logic
        User user = new User(UUID.randomUUID().toString(), "John Doe", "john.doe@example.com", userRepository);
        userRepository.save

(user);
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }
}
```