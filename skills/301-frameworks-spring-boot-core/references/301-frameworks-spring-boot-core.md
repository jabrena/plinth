---
name: 301-frameworks-spring-boot-core
description: Use when you need to review, improve, or build Spring Boot applications — including proper usage of @SpringBootApplication, component annotations (@Controller, @Service, @Repository), bean definition and scoping, configuration classes and @ConfigurationProperties, component scanning, conditional configuration and profiles, constructor injection, bean minimization, and scheduled tasks.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Spring Boot Core Guidelines

## Role

You are a Senior software engineer with extensive experience in Spring Boot and Java enterprise development

## Goal

Spring Boot Core centers on the application entry point, stereotype annotations, the IoC container, type-safe configuration, environment-specific behavior, and operational concerns such as scheduling. Effective applications use `@SpringBootApplication` as the composition root, layer-appropriate stereotypes, explicit bean contracts (scope and lifecycle), `@ConfigurationProperties` instead of scattered `@Value`, narrow component scanning, `@Profile` and conditional beans for environment logic, constructor injection, cohesive services over bean sprawl, and configured schedulers with error handling.

### Implementing These Principles

These guidelines are built upon the following core principles:

1. **Stereotypes and structure**: Use `@SpringBootApplication`, `@RestController`, `@Service`, `@Repository`, and related annotations so each type’s role is obvious and packages reflect layers or features.
2. **IoC and testability**: Prefer constructor injection, immutable dependencies where possible, and explicit bean definitions (`@Bean`, scope, lifecycle) over field injection and implicit wiring.
3. **Configuration**: Externalize settings with `@ConfigurationProperties`, validate and bind structured properties, and keep secrets and environment-specific code out of business services.
4. **Environment awareness**: Model differences with `@Profile`, `@ConditionalOn*`, and feature flags instead of `if (env)` scattered across domain code.
5. **Operational quality**: Right-size the bean graph (composition over micro-beans), configure `TaskScheduler` / async execution for scheduled work, and observe failures without breaking the scheduler.

## Constraints

Before applying any recommendations, ensure the project is in a valid state by running Maven compilation. Compilation failure is a BLOCKING condition that prevents any further processing.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **PREREQUISITE**: Project must compile successfully and pass basic validation checks before any Spring Boot refactoring
- **CRITICAL SAFETY**: If compilation fails, IMMEDIATELY STOP and DO NOT CONTINUE with any recommendations
- **BLOCKING CONDITION**: Compilation errors must be resolved by the user before proceeding with Spring Boot core improvements
- **NO EXCEPTIONS**: Under no circumstances should Spring Boot recommendations be applied to a project that fails to compile
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements

## Examples

### Table of contents

- Example 1: Spring Boot main application class
- Example 2: Layer stereotype annotations
- Example 3: Bean definition, scoping, and lifecycle
- Example 4: Configuration classes and @ConfigurationProperties
- Example 5: Component scanning and package layout
- Example 6: Conditional configuration and profiles
- Example 7: Constructor dependency injection
- Example 8: Bean minimization and composition
- Example 9: Scheduled tasks and async execution

### Example 1: Spring Boot main application class

Title: Use @SpringBootApplication as the entry point and composition root
Description: Every Spring Boot application should have a main class annotated with `@SpringBootApplication`, combining `@Configuration`, `@EnableAutoConfiguration`, and `@ComponentScan`. Use `SpringApplication.run`, optional `scanBasePackages` / `exclude` for advanced setups, and keep business logic out of the main class.

**Good example:**

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}

// Custom scanning and excluding auto-configurations when needed
@SpringBootApplication(
    scanBasePackages = {
        "com.company.app.controller",
        "com.company.app.service",
        "com.company.app.repository",
        "com.company.app.config"
    },
    exclude = {
        DataSourceAutoConfiguration.class,
        SecurityAutoConfiguration.class
    }
)
class CustomizedMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomizedMainApplication.class, args);
    }
}
```

**Bad example:**

```java
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

// Missing @SpringBootApplication; manual context loses Boot conveniences
public class MainApplication {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext();
    }
}

// Verbose and error-prone compared to @SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan
class VerboseMainApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(VerboseMainApplication.class, args);
    }
}

// Poor structure: vague name and business logic in main class
@SpringBootApplication
class App {

    @org.springframework.beans.factory.annotation.Autowired
    private com.company.app.UserService userService;

    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(App.class, args);
        System.out.println("Processing users...");
    }
}
```

### Example 2: Layer stereotype annotations

Title: Match @RestController, @Service, and @Repository to responsibilities
Description: Use web stereotypes for HTTP adapters, `@Service` for transactional application logic, and `@Repository` (often Spring Data) for persistence. Avoid generic `@Component` when a more specific stereotype communicates intent.

**Good example:**

```java
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }
}

@Service
@Transactional
class UserService {

    private final UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new java.util.NoSuchElementException("user: " + id));
    }
}

@Repository
interface UserRepository extends CrudRepository<User, Long> {

    @Query("SELECT * FROM users WHERE email = :email")
    Optional<User> findByEmail(@Param("email") String email);
}

@Table("users")
class User {
    @org.springframework.data.annotation.Id
    private Long id;
    @Column("email")
    private String email;
    @Column("last_login")
    private LocalDateTime lastLogin;
}
```

**Bad example:**

```java
import org.springframework.stereotype.Component;

// Too generic; should be @RestController for HTTP API
@Component
class UserController {
    @jakarta.inject.Inject
    private UserService userService;
}

// Should be @Service; missing typical transaction boundary for data work
@Component
class UserService { }

// Should be @Repository / Spring Data, not a generic component
@Component
class UserRepository { }
```

### Example 3: Bean definition, scoping, and lifecycle

Title: Use explicit scopes, constructor injection, and lifecycle hooks thoughtfully
Description: Define `@Bean` methods with appropriate scope (singleton vs prototype), prefer constructor injection in `@Service` components, use `@EventListener` for startup hooks, and avoid heavy work in `@PostConstruct` when it blocks startup.

**Good example:**

```java
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import jakarta.annotation.PreDestroy;

@Configuration
class AppConfig {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    AuditLogger auditLogger() {
        return new AuditLogger();
    }
}

@Service
class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
}

@Component
class DatabaseMigration {

    @EventListener
    void onApplicationReady(ApplicationReadyEvent event) {
        performMigration();
    }

    @PreDestroy
    void cleanup() {
        // release resources
    }

    private void performMigration() { }
}

class AuditLogger { }
interface UserRepository { }
```

**Bad example:**

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

@Configuration
class AppConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // new instance semantics unclear without scope
    }
}

@Service
class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
}

@Component
class DatabaseMigration {

    @PostConstruct
    void init() {
        performHeavyMigration(); // can block startup
    }

    private void performHeavyMigration() { }
}
interface UserRepository { }
```

### Example 4: Configuration classes and @ConfigurationProperties

Title: Group settings with immutable property beans and @ConditionalOnProperty
Description: Organize beans in `@Configuration` classes, bind structured configuration with `@ConfigurationProperties` (constructor binding where appropriate), and avoid scattering many `@Value` fields and hardcoded secrets across the codebase.

**Good example:**

```java
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.CacheManager;
import javax.sql.DataSource;
import java.time.Duration;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableConfigurationProperties(DatabaseProperties.class)
class AppConfig {

    @Bean
    @ConditionalOnProperty(name = "app.cache.enabled", havingValue = "true")
    CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("users", "products");
    }
}

@ConfigurationProperties(prefix = "app.database")
record DatabaseProperties(
    String url,
    String username,
    int maxConnections,
    Duration connectionTimeout
) {}

@Configuration
@Profile("!test")
class ProductionConfig {

    @Bean
    DataSource dataSource(DatabaseProperties properties) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(properties.url());
        config.setUsername(properties.username());
        config.setMaximumPoolSize(properties.maxConnections());
        return new HikariDataSource(config);
    }
}
```

**Bad example:**

```java
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;

@Configuration
class AppConfig {

    @Value("${database.url}")
    private String databaseUrl;

    @Value("${database.username}")
    private String username;

    @Bean
    DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(databaseUrl);
        config.setUsername(username);
        config.setPassword("hardcoded-password");
        config.setMaximumPoolSize(10);
        return new HikariDataSource(config);
    }
}
```

### Example 5: Component scanning and package layout

Title: Keep base packages focused and structure code by layer or feature
Description: Prefer a clear package hierarchy under your application root; use `scanBasePackages` / `@ComponentScan` with explicit packages rather than scanning huge prefixes like `"com"`. Name beans explicitly when multiple implementations exist.

**Good example:**

```java
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.company.app.controller",
    "com.company.app.service",
    "com.company.app.repository",
    "com.company.app.config"
})
@EnableJdbcRepositories("com.company.app.repository")
class Application {

    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(Application.class, args);
    }
}

@Component("userService")
class UserService { }
```

**Bad example:**

```java
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ComponentScan("com")
class Application {

    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(Application.class, args);
    }
}

@Component
class UserService {
    void handleUser() { }
    void sendEmail() { }
    void generateReport() { }
}
```

### Example 6: Conditional configuration and profiles

Title: Use @Profile and @Conditional* instead of manual environment branching
Description: Model environment differences with `@Profile`, `@ConditionalOnProperty`, `@ConditionalOnClass`, and related annotations. Keep environment checks in configuration layers, not inside domain services.

**Good example:**

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import java.time.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@Profile("development")
class DevConfig {

    @Bean
    @ConditionalOnMissingBean
    Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    DataSource devDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/devdb");
        config.setMaximumPoolSize(5);
        return new HikariDataSource(config);
    }
}

@Configuration
@Profile("production")
class ProdConfig {

    @Bean
    @ConditionalOnProperty(name = "app.monitoring.enabled", havingValue = "true", matchIfMissing = true)
    MeterRegistry meterRegistry() {
        return new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
    }

    @Bean
    @ConditionalOnClass(name = "redis.clients.jedis.Jedis")
    RedisTemplate<String, Object> redisTemplate() {
        return new RedisTemplate<>();
    }
}

@Service
@ConditionalOnProperty(name = "features.advanced-analytics", havingValue = "true")
class AdvancedAnalyticsService { }
```

**Bad example:**

```java
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;

@Configuration
class AppConfig {

    @Value("${spring.profiles.active:}")
    private String activeProfile;

    @Bean
    DataSource dataSource() {
        if ("development".equals(activeProfile)) {
            return createDevDataSource();
        } else if ("production".equals(activeProfile)) {
            return createProdDataSource();
        }
        return createDefaultDataSource();
    }

    private DataSource createDevDataSource() { return null; }
    private DataSource createProdDataSource() { return null; }
    private DataSource createDefaultDataSource() { return null; }
}

@Service
class NotificationService {

    @Value("${app.env}")
    private String environment;

    void sendNotification(String message) {
        if ("prod".equals(environment)) {
            sendReal(message);
        } else {
            System.out.println("DEV: " + message);
        }
    }

    private void sendReal(String message) { }
}
```

### Example 7: Constructor dependency injection

Title: Prefer one constructor and final fields for required dependencies
Description: Use constructor injection for required dependencies (`@Autowired` optional on a single constructor since Spring 4.3). Avoid field and setter injection for required collaborators; validate non-null invariants when useful.

**Good example:**

```java
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import java.util.Objects;
import org.springframework.boot.jdbc.DataSourceBuilder;

@Service
class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final AuditService auditService;

    UserService(UserRepository userRepository, EmailService emailService, AuditService auditService) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.emailService = Objects.requireNonNull(emailService);
        this.auditService = Objects.requireNonNull(auditService);
    }
}

@Configuration
class ServiceConfig {

    private final DatabaseProperties databaseProperties;

    ServiceConfig(DatabaseProperties databaseProperties) {
        this.databaseProperties = databaseProperties;
    }

    @Bean
    DataSource dataSource() {
        return DataSourceBuilder.create()
            .url(databaseProperties.url())
            .username(databaseProperties.username())
            .password(databaseProperties.password())
            .build();
    }
}

record DatabaseProperties(String url, String username, String password) {}
interface UserRepository {}
interface EmailService {}
interface AuditService {}
```

**Bad example:**

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import javax.sql.DataSource;

@Service
class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;
}

@Service
class OrderService {

    private UserService userService;
    private PaymentService paymentService;

    @Autowired
    void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}

@Configuration
class BadConfig {

    @Autowired
    private Environment environment;

    @Bean
    DataSource dataSource() {
        return null;
    }
}
interface UserRepository {}
interface EmailService {}
interface PaymentService {}
```

### Example 8: Bean minimization and composition

Title: Group related behavior; avoid a bean for every trivial helper
Description: Prefer cohesive services, private factory methods on `@Configuration`, nested types for tightly coupled helpers, and grouped `@ConfigurationProperties` over dozens of single-purpose beans and `@Value` provider components.

**Good example:**

```java
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.ArrayList;
import java.util.List;

@Service
class UserManagementService {

    private final UserRepository userRepository;
    private final UserValidator userValidator = new UserValidator();
    private final UserNotificationService notifications;

    UserManagementService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.notifications = new UserNotificationService();
    }

    User createUser(CreateUserRequest request) {
        userValidator.validate(request);
        return userRepository.save(new User(request.email()));
    }
}

@Configuration
class CommunicationConfig {

    @Bean
    CommunicationService communicationService(
            @Value("${app.email.enabled:true}") boolean emailEnabled,
            @Value("${app.sms.enabled:false}") boolean smsEnabled) {

        List<NotificationChannel> channels = new ArrayList<>();
        if (emailEnabled) {
            channels.add(new EmailChannel());
        }
        if (smsEnabled) {
            channels.add(new SmsChannel());
        }
        return new CommunicationService(channels);
    }
}

@ConfigurationProperties(prefix = "app")
class ApplicationProperties {

    private final Database database = new Database();

    static class Database {
        private String url;
        private int maxConnections = 10;
    }
}

record CreateUserRequest(String email) {}
class User { User(String email) { } }
interface UserRepository { User save(User u); }
class UserValidator { void validate(CreateUserRequest r) { } }
class UserNotificationService { }
interface NotificationChannel { }
class EmailChannel implements NotificationChannel { }
class SmsChannel implements NotificationChannel { }
class CommunicationService {
    CommunicationService(List<NotificationChannel> channels) { }
}
```

**Bad example:**

```java
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Component
class EmailValidator {
    boolean isValid(String email) { return email.contains("@"); }
}

@Component
class PasswordValidator {
    boolean isValid(String password) { return password.length() >= 8; }
}

@Component
class UserValidator {
    @Autowired EmailValidator emailValidator;
    @Autowired PasswordValidator passwordValidator;
}

@Component
class DatabaseUrlProvider {
    @Value("${database.url}") private String url;
}

@Component
class DatabaseUsernameProvider {
    @Value("${database.username}") private String username;
}
```

### Example 9: Scheduled tasks and async execution

Title: Configure schedulers, externalize intervals, and handle errors safely
Description: Put `@EnableScheduling` on configuration classes, provide a `TaskScheduler` with a sized pool and `ErrorHandler`, externalize delays/cron via properties, catch errors inside tasks so one failure does not break the scheduler, and use `@Async` or dedicated executors for long work.

**Good example:**

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@EnableScheduling
@EnableAsync
class SchedulingConfig {

    @Bean
    @Primary
    TaskScheduler taskScheduler(CustomErrorHandler errorHandler) {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5);
        scheduler.setThreadNamePrefix("scheduled-task-");
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setErrorHandler(errorHandler);
        scheduler.initialize();
        return scheduler;
    }

    @Bean
    TaskExecutor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("async-task-");
        executor.initialize();
        return executor;
    }
}

@Component
class CustomErrorHandler implements ErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomErrorHandler.class);

    @Override
    public void handleError(Throwable t) {
        log.error("Scheduled task failed", t);
    }
}

@Component
class DataMaintenanceScheduler {

    private static final Logger log = LoggerFactory.getLogger(DataMaintenanceScheduler.class);

    private final UserRepository userRepository;

    DataMaintenanceScheduler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Scheduled(fixedRateString = "${app.cleanup.rate:1800000}")
    void cleanupExpiredSessions() {
        try {
            log.info("Starting session cleanup");
            userRepository.deleteExpired();
        } catch (Exception e) {
            log.error("Cleanup failed", e);
        }
    }
}

interface UserRepository {
    void deleteExpired();
}
```

**Bad example:**

```java
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

@Component
@EnableScheduling
class BadScheduler {

    @Autowired
    private UserRepository userRepository;

    @Scheduled(fixedRate = 30000)
    void cleanupUsers() {
        userRepository.deleteInactiveUsers();
        sendEmailNotifications();
    }

    @Scheduled(cron = "0 0 2 * * *")
    void heavyProcessing() {
        for (int i = 0; i < 1_000_000; i++) {
            performComplexCalculation();
        }
        riskyOperation();
    }

    private void performComplexCalculation() { }
    private void riskyOperation() { throw new RuntimeException("breaks scheduling"); }

    private void sendEmailNotifications() { }

    private RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedDelay = 1000)
    void constantPolling() {
        checkForNewMessages();
    }

    private void checkForNewMessages() { }
}

interface UserRepository {
    void deleteInactiveUsers();
}
```

## Output Format

- **ANALYZE** the Spring Boot codebase for core concerns: main class setup, stereotype usage, bean definitions and scopes, configuration properties, component scanning, profiles and conditionals, injection style, bean granularity, and scheduling/async configuration
- **CATEGORIZE** findings by impact (CRITICAL, MAINTAINABILITY, OPERATIONAL) and area (entry point, stereotypes, IoC, configuration, environment, scheduling)
- **APPLY** improvements aligned with these guidelines: fix main class and scanning, use layer-appropriate annotations, consolidate configuration with `@ConfigurationProperties`, replace manual env branching with `@Profile` / `@Conditional*`, migrate to constructor injection, reduce unnecessary beans through composition, and harden schedulers with pools and error handling
- **IMPLEMENT** changes incrementally with compiling steps: prefer small edits that preserve behavior, then run tests; document notable trade-offs (e.g., profile splits, feature flags)
- **EXPLAIN** what changed and why: clearer structure, safer configuration, better testability, or more predictable scheduling
- **VALIDATE** with `./mvnw compile` before and `./mvnw clean verify` after substantive edits

## Safeguards

- **BLOCKING SAFETY CHECK**: ALWAYS run `./mvnw compile` or `mvn compile` before ANY Spring Boot refactoring — compilation failure is a HARD STOP
- **CRITICAL VALIDATION**: Execute `./mvnw clean verify` or `mvn clean verify` after applying changes
- **MANDATORY VERIFICATION**: Confirm application context still starts and critical flows behave as before when changing beans, profiles, or configuration binding
- **SAFETY PROTOCOL**: If ANY compilation error occurs, cease recommendations and require user intervention
- **CONFIGURATION SAFETY**: Do not commit secrets; prefer externalized config and environment variables for sensitive values
- **SCHEDULING SAFETY**: Changing thread pools or `@Scheduled` methods can alter load and ordering — validate under realistic profiles
- **INCREMENTAL SAFETY**: Apply core container and configuration changes in small steps with verification between steps