title=Module 3: Secure Coding - Security & Best Practices
type=course
status=published
date=2025-09-17
author=MyRobot
version=0.11.0-SNAPSHOT
tags=java, security, concurrency, logging, exception-handling, owasp, system-prompts
~~~~~~

## 🎯 Learning Objectives

By the end of this module, you will:

- **Implement security best practices** using automated system prompts
- **Handle concurrency safely** with modern Java patterns
- **Apply proper logging strategies** for production applications
- **Master exception handling** techniques for robust applications
- **Create secure, resilient code** that follows industry standards

## 📚 Module Overview

**Duration:** 4 hours
**Difficulty:** Intermediate to Advanced
**Prerequisites:** Module 2 completed, basic security awareness

This module focuses on creating production-ready, secure applications. You'll learn to use system prompts that automatically apply security best practices, handle concurrency correctly, and implement robust error handling.

## 🗺️ Learning Path

### **Lesson 3.1: Security Best Practices** (75 minutes)

#### 🎯 **Learning Objectives:**
- Apply secure coding practices using `@124-java-secure-coding`
- Understand common security vulnerabilities
- Implement input validation and sanitization
- Apply the principle of least privilege

#### 📖 **Core Concepts:**

**OWASP Top 10 for Java Applications:**

1. **Injection Attacks**: SQL injection, command injection
2. **Broken Authentication**: Session management, password storage
3. **Sensitive Data Exposure**: Encryption, data protection
4. **XML External Entities (XXE)**: XML parsing vulnerabilities
5. **Broken Access Control**: Authorization flaws
6. **Security Misconfiguration**: Default settings, unnecessary features
7. **Cross-Site Scripting (XSS)**: Input validation, output encoding
8. **Insecure Deserialization**: Object serialization risks
9. **Known Vulnerabilities**: Dependency management
10. **Insufficient Logging**: Security monitoring

#### 💡 **Knowledge Check:**
*What's the difference between authentication and authorization?*

**Answer:** Authentication verifies who you are (identity), while authorization determines what you can do (permissions).

#### 🔧 **Hands-on Exercise 3.1:**

**Scenario:** Secure a vulnerable user authentication service.

**Step 1: Analyze Vulnerable Code**
```java
public class UserAuthService {
    private Connection connection;

    public boolean authenticate(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = '" + username +
                    "' AND password = '" + password + "'";
        // Direct SQL concatenation - SQL injection risk!

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace(); // Sensitive information leakage!
            return false;
        }
    }

    public void updatePassword(String username, String newPassword) {
        // Storing plain text password - security risk!
        String sql = "UPDATE users SET password = '" + newPassword +
                    "' WHERE username = '" + username + "'";
        // More SQL injection risks...
    }
}
```

**Security Issues Identified:**
- SQL injection vulnerabilities
- Plain text password storage
- Information leakage through stack traces
- No input validation
- No rate limiting or brute force protection

**Step 2: Apply Security System Prompt**
Use: `Improve the class/classes added in the context applying the system prompt @124-java-secure-coding`

**Step 3: Interactive Security Review**
Try: `Improve the class/classes added in the context applying the system prompt @124-java-secure-coding with the behaviour @behaviour-consultative-interaction`

**Expected Security Improvements:**

```java
@Service
public class SecureUserAuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RateLimitService rateLimitService;
    private static final Logger logger = LoggerFactory.getLogger(SecureUserAuthService.class);

    public SecureUserAuthService(UserRepository userRepository,
                               PasswordEncoder passwordEncoder,
                               RateLimitService rateLimitService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.rateLimitService = rateLimitService;
    }

    public AuthResult authenticate(@Valid @NotBlank String username,
                                 @Valid @NotBlank String password) {
        // Input validation
        if (!isValidInput(username) || !isValidInput(password)) {
            logger.warn("Invalid input detected for authentication attempt");
            return AuthResult.INVALID_INPUT;
        }

        // Rate limiting
        if (!rateLimitService.isAllowed(username)) {
            logger.warn("Rate limit exceeded for user: {}", username);
            return AuthResult.RATE_LIMITED;
        }

        try {
            Optional<User> userOpt = userRepository.findByUsername(username);
            if (userOpt.isEmpty()) {
                logger.info("Authentication failed: user not found");
                return AuthResult.AUTHENTICATION_FAILED;
            }

            User user = userOpt.get();
            if (passwordEncoder.matches(password, user.getHashedPassword())) {
                logger.info("Successful authentication for user: {}", username);
                return AuthResult.SUCCESS;
            } else {
                logger.warn("Authentication failed: incorrect password for user: {}", username);
                return AuthResult.AUTHENTICATION_FAILED;
            }

        } catch (Exception e) {
            logger.error("Authentication error occurred", e);
            return AuthResult.SYSTEM_ERROR;
        }
    }

    public void updatePassword(@Valid @NotBlank String username,
                             @Valid @NotBlank String newPassword) {
        // Password strength validation
        if (!isStrongPassword(newPassword)) {
            throw new WeakPasswordException("Password does not meet security requirements");
        }

        String hashedPassword = passwordEncoder.encode(newPassword);
        userRepository.updatePassword(username, hashedPassword);

        logger.info("Password updated for user: {}", username);
    }

    private boolean isValidInput(String input) {
        return input != null &&
               input.length() <= 100 &&
               input.matches("^[a-zA-Z0-9@._-]+$");
    }

    private boolean isStrongPassword(String password) {
        return password.length() >= 12 &&
               password.matches(".*[A-Z].*") &&
               password.matches(".*[a-z].*") &&
               password.matches(".*[0-9].*") &&
               password.matches(".*[!@#$%^&*].*");
    }
}
```

#### 🔍 **Security Improvements Analysis:**
- **SQL Injection Prevention**: Using parameterized queries through repository
- **Password Security**: Bcrypt hashing instead of plain text
- **Input Validation**: Sanitization and length limits
- **Rate Limiting**: Brute force attack prevention
- **Secure Logging**: No sensitive data in logs
- **Error Handling**: Generic error responses to prevent information leakage

---

### **Lesson 3.2: Concurrency Mastery** (60 minutes)

#### 🎯 **Learning Objectives:**
- Implement thread-safe code using `@125-java-concurrency`
- Understand modern Java concurrency utilities
- Apply concurrent design patterns safely

#### 📖 **Core Concepts:**

**Concurrency Challenges:**

1. **Race Conditions**: Multiple threads accessing shared data
2. **Deadlocks**: Circular waiting for resources
3. **Memory Visibility**: Changes not visible across threads
4. **Performance**: Balancing safety with efficiency

**Modern Java Concurrency Tools:**
- **CompletableFuture**: Asynchronous programming
- **Virtual Threads**: Lightweight threading (Java 21+)
- **Concurrent Collections**: Thread-safe data structures
- **Locks and Synchronizers**: Advanced synchronization

#### 🔧 **Hands-on Exercise 3.2:**

**Scenario:** Fix concurrency issues in a shared counter service.

**Step 1: Problematic Concurrent Code**
```java
public class CounterService {
    private int counter = 0;
    private List<String> operations = new ArrayList<>();

    public void increment(String operation) {
        counter++; // Race condition!
        operations.add(operation); // Not thread-safe!
    }

    public int getCounter() {
        return counter; // Visibility issue!
    }

    public List<String> getOperations() {
        return operations; // Returning mutable reference!
    }
}
```

**Step 2: Apply Concurrency System Prompt**
Use: `Improve the class/classes added in the context applying the system prompt @125-java-concurrency`

**Expected Improvements:**

```java
@Service
public class ThreadSafeCounterService {
    private final AtomicInteger counter = new AtomicInteger(0);
    private final ConcurrentLinkedQueue<String> operations = new ConcurrentLinkedQueue<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public void increment(String operation) {
        counter.incrementAndGet();
        operations.offer(operation);
    }

    public int getCounter() {
        return counter.get();
    }

    public List<String> getOperations() {
        return List.copyOf(operations);
    }

    // Advanced: Batch operations with proper synchronization
    public CompletableFuture<Void> batchIncrement(List<String> operationBatch) {
        return CompletableFuture.runAsync(() -> {
            operationBatch.forEach(this::increment);
        });
    }
}
```

#### 💡 **Concurrency Benefits:**
- **Atomic Operations**: Thread-safe counter updates
- **Concurrent Collections**: Safe multi-threaded access
- **Immutable Returns**: Defensive copying prevents external modification
- **Asynchronous Processing**: Non-blocking batch operations

---

### **Lesson 3.3: Logging Excellence** (45 minutes)

#### 🎯 **Learning Objectives:**
- Implement proper logging using `@126-java-logging`
- Understand logging levels and structured logging
- Apply security considerations in logging

#### 📖 **Core Concepts:**

**Logging Best Practices:**

1. **Appropriate Levels**: ERROR, WARN, INFO, DEBUG, TRACE
2. **Structured Logging**: Consistent format, searchable
3. **Security**: No sensitive data in logs
4. **Performance**: Lazy evaluation, proper configuration
5. **Monitoring**: Integration with observability tools

#### 🔧 **Hands-on Exercise 3.3:**

**Step 1: Poor Logging Example**
```java
public class PaymentService {
    public void processPayment(String cardNumber, double amount) {
        System.out.println("Processing payment: " + cardNumber + " for $" + amount);
        // Sensitive data in logs!

        try {
            // Payment processing logic
        } catch (Exception e) {
            e.printStackTrace(); // Poor error handling!
        }
    }
}
```

**Step 2: Apply Logging System Prompt**
Use: `Improve the class/classes added in the context applying the system prompt @126-java-logging`

**Expected Improvements:**

```java
@Service
public class SecurePaymentService {
    private static final Logger logger = LoggerFactory.getLogger(SecurePaymentService.class);
    private static final Marker SECURITY_MARKER = MarkerFactory.getMarker("SECURITY");

    public PaymentResult processPayment(String cardNumber, BigDecimal amount) {
        String maskedCardNumber = maskCardNumber(cardNumber);

        logger.info("Processing payment for card ending in {} for amount {}",
                   maskedCardNumber, amount);

        try {
            validatePaymentRequest(cardNumber, amount);
            PaymentResult result = executePayment(cardNumber, amount);

            logger.info("Payment processed successfully for card ending in {}, transaction ID: {}",
                       maskedCardNumber, result.getTransactionId());

            return result;

        } catch (PaymentValidationException e) {
            logger.warn("Payment validation failed for card ending in {}: {}",
                       maskedCardNumber, e.getMessage());
            throw e;

        } catch (PaymentProcessingException e) {
            logger.error("Payment processing failed for card ending in {}: {}",
                        maskedCardNumber, e.getMessage(), e);
            throw e;

        } catch (Exception e) {
            logger.error(SECURITY_MARKER, "Unexpected error during payment processing", e);
            throw new PaymentSystemException("Payment processing unavailable", e);
        }
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber.length() < 4) return "****";
        return "****-****-****-" + cardNumber.substring(cardNumber.length() - 4);
    }
}
```

---

### **Lesson 3.4: Exception Handling Mastery** (60 minutes)

#### 🎯 **Learning Objectives:**
- Implement robust exception handling using `@127-java-exception-handling`
- Design exception hierarchies effectively
- Apply error recovery strategies

#### 📖 **Core Concepts:**

**Exception Handling Principles:**

1. **Fail Fast**: Detect errors early
2. **Specific Exceptions**: Meaningful exception types
3. **Recovery Strategies**: Graceful degradation
4. **Resource Management**: try-with-resources
5. **Documentation**: Clear exception contracts

#### 🔧 **Hands-on Exercise 3.4:**

**Step 1: Poor Exception Handling**
```java
public class FileProcessor {
    public String processFile(String filename) {
        try {
            // File processing logic
            return "processed";
        } catch (Exception e) {
            return null; // Lost error information!
        }
    }
}
```

**Step 2: Apply Exception Handling System Prompt**
Use: `Improve the class/classes added in the context applying the system prompt @127-java-exception-handling`

**Expected Improvements:**

```java
public class RobustFileProcessor {
    private static final Logger logger = LoggerFactory.getLogger(RobustFileProcessor.class);

    public ProcessingResult processFile(Path filePath) throws FileProcessingException {
        validateInput(filePath);

        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {

            String content = reader.lines()
                .collect(Collectors.joining("\n"));

            ProcessedContent result = processContent(content);

            logger.info("Successfully processed file: {}", filePath);
            return ProcessingResult.success(result);

        } catch (IOException e) {
            logger.error("IO error processing file: {}", filePath, e);
            throw new FileProcessingException("Failed to read file: " + filePath, e);

        } catch (ContentValidationException e) {
            logger.warn("Content validation failed for file: {}", filePath, e);
            throw new FileProcessingException("Invalid file content: " + filePath, e);

        } catch (Exception e) {
            logger.error("Unexpected error processing file: {}", filePath, e);
            throw new FileProcessingException("Unexpected processing error: " + filePath, e);
        }
    }

    private void validateInput(Path filePath) throws FileProcessingException {
        if (filePath == null) {
            throw new FileProcessingException("File path cannot be null");
        }

        if (!Files.exists(filePath)) {
            throw new FileProcessingException("File does not exist: " + filePath);
        }

        if (!Files.isReadable(filePath)) {
            throw new FileProcessingException("File is not readable: " + filePath);
        }
    }
}

// Custom exception hierarchy
public class FileProcessingException extends Exception {
    public FileProcessingException(String message) {
        super(message);
    }

    public FileProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

---

## 🏆 Module Assessment

### **Knowledge Validation Checkpoint**

**Question 1:** What are the top 3 OWASP security risks for Java applications?

**Question 2:** Why should you avoid using `synchronized` keyword for all concurrency needs?

**Question 3:** What sensitive information should never appear in application logs?

**Question 4:** When should you use checked vs. unchecked exceptions?

### **Practical Assessment Project**

**Project: "Secure Banking Service"**

**Scenario:** Build a secure, concurrent banking service that handles multiple account operations safely.

**Requirements:**
1. Implement secure authentication and authorization
2. Handle concurrent account operations safely
3. Apply comprehensive logging without exposing sensitive data
4. Implement robust exception handling with recovery strategies
5. Include security audit trails

**Deliverables:**
- Secure authentication service with rate limiting
- Thread-safe account operations service
- Comprehensive logging configuration
- Custom exception hierarchy
- Security audit and testing report

**Success Criteria:**
- No security vulnerabilities (verified by static analysis)
- Thread-safe under concurrent load testing
- Proper logging without sensitive data exposure
- Graceful error handling and recovery
- System prompts used effectively throughout

### **Time Investment:**
- **Security Implementation**: 2 hours
- **Concurrency Testing**: 1.5 hours
- **Logging & Exception Handling**: 1 hour
- **Security Audit**: 1 hour
- **Total**: 5.5 hours

---

## 🚀 Next Steps

**Outstanding Work!** You've mastered secure coding practices and production-ready development techniques.

**What You've Accomplished:**
- ✅ Implemented comprehensive security measures
- ✅ Created thread-safe, concurrent applications
- ✅ Applied professional logging practices
- ✅ Mastered robust exception handling

**Ready for modern Java features?**

👉 **[Continue to Module 4: Modern Java →](module-4-modern-java.html)**

**In Module 4, you'll learn to:**
- Master Java Generics and type safety
- Apply functional programming patterns
- Leverage modern Java language features
- Create elegant, expressive code

---

## 📚 Additional Resources

- **[OWASP Java Security Guide](https://owasp.org/www-project-java-security-guide/)**
- **[Java Concurrency in Practice](https://www.amazon.com/Java-Concurrency-Practice-Brian-Goetz/dp/0321349601)**
- **[SLF4J Manual](http://www.slf4j.org/manual.html)**
- **[Java Exception Handling Best Practices](https://stackify.com/best-practices-exceptions-java/)**

---

*Continue your journey with advanced Java language features and modern programming paradigms.*
