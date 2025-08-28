package info.jab.generics;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * Examples showing the problems solved by self-bounded generics (CRTP).
 * This demonstrates why we need the Curiously Recurring Template Pattern.
 *
 * PROBLEMS DEMONSTRATED:
 * 1. Loss of fluent API type safety in inheritance
 * 2. Cannot return specific subtype from base methods
 * 3. Broken method chaining in subclasses
 * 4. Need for explicit casting everywhere
 * 5. No compile-time guarantee of method availability
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class RawBuilderPattern {

    // PROBLEM: Base builder returns Object/BaseBuilder, breaking fluent API
    public abstract static class RawBaseBuilder {
        protected String name;
        protected String description;
        protected LocalDateTime timestamp;

        // PROBLEM: Returns RawBaseBuilder, not the specific subtype
        public RawBaseBuilder withName(String name) {
            this.name = name;
            return this; // PROBLEM: This breaks fluent API for subclasses!
        }

        public RawBaseBuilder withDescription(String description) {
            this.description = description;
            return this; // PROBLEM: Subclass-specific methods not available after this
        }

        public RawBaseBuilder withTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this; // PROBLEM: Chain is broken for subclass methods
        }

        protected void validate() {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalStateException("Name is required");
            }
        }
    }

    // PROBLEM: UserBuilder inheritance breaks fluent API
    public static class RawUserBuilder extends RawBaseBuilder {
        private int age;
        private String email;
        private Set roles = new HashSet(); // PROBLEM: Raw Set, no type safety

        // PROBLEM: These methods return RawUserBuilder, but base methods return RawBaseBuilder
        public RawUserBuilder withAge(int age) {
            if (age < 0 || age > 150) {
                throw new IllegalArgumentException("Invalid age: " + age);
            }
            this.age = age;
            return this;
        }

        public RawUserBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public RawUserBuilder withRole(String role) {
            this.roles.add(role); // PROBLEM: No type safety
            return this;
        }

        public RawUserBuilder withRoles(Collection roles) { // PROBLEM: Raw Collection
            this.roles.addAll(roles); // PROBLEM: Could add wrong types
            return this;
        }

        // PROBLEM: This won't work! withName() returns RawBaseBuilder, not RawUserBuilder
        // So you can't chain: new RawUserBuilder().withName("John").withAge(30)
        // The withAge method won't be available after withName()

        public Object build() { // PROBLEM: Returns Object, no type safety
            validate();
            if (email == null) {
                throw new IllegalStateException("Email is required for User");
            }
            return new RawUser(name, description, timestamp, age, email, new HashSet(roles));
        }
    }

    // PROBLEM: Product builder has the same inheritance issues
    public static class RawProductBuilder extends RawBaseBuilder {
        private double price;
        private String category;
        private Map attributes = new HashMap(); // PROBLEM: Raw Map, no type safety

        public RawProductBuilder withPrice(double price) {
            if (price < 0) {
                throw new IllegalArgumentException("Price cannot be negative");
            }
            this.price = price;
            return this;
        }

        public RawProductBuilder withCategory(String category) {
            this.category = category;
            return this;
        }

        public RawProductBuilder withAttribute(String key, Object value) { // PROBLEM: Object value
            this.attributes.put(key, value); // PROBLEM: No type safety
            return this;
        }

        public RawProductBuilder withAttributes(Map attributes) { // PROBLEM: Raw Map
            this.attributes.putAll(attributes); // PROBLEM: Could add wrong types
            return this;
        }

        // PROBLEM: Same issue - can't chain base methods with subclass methods
        public Object build() { // PROBLEM: Returns Object
            validate();
            if (category == null) {
                throw new IllegalStateException("Category is required for Product");
            }
            return new RawProduct(name, description, timestamp, price, category, new HashMap(attributes));
        }
    }

    // PROBLEM: Result classes without type safety
    public static class RawUser {
        private final String name;
        private final String description;
        private final LocalDateTime timestamp;
        private final int age;
        private final String email;
        private final Set roles; // PROBLEM: Raw Set

        public RawUser(String name, String description, LocalDateTime timestamp,
                      int age, String email, Set roles) {
            this.name = name;
            this.description = description;
            this.timestamp = timestamp;
            this.age = age;
            this.email = email;
            this.roles = roles; // PROBLEM: No defensive copy or type checking
        }

        // Getters without type safety
        public String getName() { return name; }
        public String getDescription() { return description; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public int getAge() { return age; }
        public String getEmail() { return email; }
        public Set getRoles() { return roles; } // PROBLEM: Raw Set returned
    }

    public static class RawProduct {
        private final String name;
        private final String description;
        private final LocalDateTime timestamp;
        private final double price;
        private final String category;
        private final Map attributes; // PROBLEM: Raw Map

        public RawProduct(String name, String description, LocalDateTime timestamp,
                         double price, String category, Map attributes) {
            this.name = name;
            this.description = description;
            this.timestamp = timestamp;
            this.price = price;
            this.category = category;
            this.attributes = attributes; // PROBLEM: No defensive copy or type checking
        }

        // Getters without type safety
        public String getName() { return name; }
        public String getDescription() { return description; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public double getPrice() { return price; }
        public String getCategory() { return category; }
        public Map getAttributes() { return attributes; } // PROBLEM: Raw Map returned
    }

    // PROBLEM: Comparable hierarchy without self-bounded generics
    public abstract static class RawComparableEntity implements Comparable {
        protected final String id;
        protected final LocalDateTime createdAt;

        protected RawComparableEntity(String id) {
            this.id = id;
            this.createdAt = LocalDateTime.now();
        }

        public String getId() { return id; }
        public LocalDateTime getCreatedAt() { return createdAt; }

        // PROBLEM: compareTo takes Object, no type safety
        @Override
        public int compareTo(Object other) {
            // PROBLEM: Have to check type at runtime
            if (!(other instanceof RawComparableEntity)) {
                throw new ClassCastException("Cannot compare with " + other.getClass());
            }
            RawComparableEntity otherEntity = (RawComparableEntity) other;
            return this.createdAt.compareTo(otherEntity.createdAt);
        }

        // PROBLEM: clone() returns Object, caller must cast
        public abstract Object cloneEntity();

        // PROBLEM: isNewerThan takes Object, no type safety
        public final boolean isNewerThan(Object other) {
            if (!(other instanceof RawComparableEntity)) {
                throw new ClassCastException("Cannot compare with " + other.getClass());
            }
            RawComparableEntity otherEntity = (RawComparableEntity) other;
            return this.createdAt.isAfter(otherEntity.createdAt);
        }
    }

    // PROBLEM: Specific implementations lose type safety
    public static class RawTimestampedEvent extends RawComparableEntity {
        private final String eventType;
        private final Map data; // PROBLEM: Raw Map

        public RawTimestampedEvent(String id, String eventType, Map data) {
            super(id);
            this.eventType = eventType;
            this.data = new HashMap(data); // PROBLEM: No type checking on copy
        }

        public String getEventType() { return eventType; }
        public Map getData() { return data; } // PROBLEM: Raw Map returned

        @Override
        public Object cloneEntity() { // PROBLEM: Returns Object
            return new RawTimestampedEvent(this.id, this.eventType, this.data);
        }

        // PROBLEM: Method returns Object, caller must cast
        public Object withAdditionalData(String key, Object value) {
            Map newData = new HashMap(this.data);
            newData.put(key, value); // PROBLEM: No type safety
            return new RawTimestampedEvent(this.id, this.eventType, newData);
        }
    }

    // PROBLEM: Processor pattern without type safety
    public abstract static class RawChainableProcessor {
        protected Object processor; // PROBLEM: Raw Function reference

        protected RawChainableProcessor(Object processor) {
            this.processor = processor;
        }

        // PROBLEM: Methods return RawChainableProcessor, not specific subtype
        public abstract RawChainableProcessor addPreProcessor(Object preProcessor);
        public abstract RawChainableProcessor addPostProcessor(Object postProcessor);

        public Object process(Object input) { // PROBLEM: All Object types
            // PROBLEM: Have to check if processor is actually a Function
            if (processor instanceof Function) {
                Function func = (Function) processor;
                try {
                    return func.apply(input);
                } catch (ClassCastException e) {
                    throw new RuntimeException("Processor failed", e);
                }
            } else {
                throw new RuntimeException("Processor is not a Function");
            }
        }

        // PROBLEM: Returns RawChainableProcessor, breaks fluent API for subclasses
        public RawChainableProcessor withLogging(String logPrefix) {
            Object originalProcessor = this.processor;
            if (originalProcessor instanceof Function) {
                Function func = (Function) originalProcessor;
                this.processor = (Function) input -> {
                    System.out.println(logPrefix + " Processing: " + input);
                    Object result = func.apply(input);
                    System.out.println(logPrefix + " Result: " + result);
                    return result;
                };
            }
            return this; // PROBLEM: Breaks fluent API for subclasses
        }
    }

    public static class RawStringProcessor extends RawChainableProcessor {
        public RawStringProcessor(Object processor) {
            super(processor);
        }

        @Override
        public RawStringProcessor addPreProcessor(Object preProcessor) {
            // PROBLEM: Complex runtime type checking needed
            if (this.processor instanceof Function && preProcessor instanceof Function) {
                Function currentFunc = (Function) this.processor;
                Function preFunc = (Function) preProcessor;
                this.processor = (Function) input -> currentFunc.apply(preFunc.apply(input));
            } else {
                throw new RuntimeException("Invalid processor types");
            }
            return this;
        }

        @Override
        public RawStringProcessor addPostProcessor(Object postProcessor) {
            // PROBLEM: Complex runtime type checking needed
            if (this.processor instanceof Function && postProcessor instanceof Function) {
                Function currentFunc = (Function) this.processor;
                Function postFunc = (Function) postProcessor;
                this.processor = (Function) input -> postFunc.apply(currentFunc.apply(input));
            } else {
                throw new RuntimeException("Invalid processor types");
            }
            return this;
        }

        // PROBLEM: These methods can't be chained with base methods due to return type issues
        public RawStringProcessor withTrimming() {
            return addPreProcessor((Function<String, String>) String::trim);
        }

        public RawStringProcessor withUpperCase() {
            return addPostProcessor((Function<String, String>) String::toUpperCase);
        }

        // PROBLEM: After withLogging(), these methods are not available!
    }

    // PROBLEM: Repository pattern without type safety
    public abstract static class RawBaseRepository {
        protected final Map storage = new HashMap(); // PROBLEM: Raw Map

        // PROBLEM: Methods return RawBaseRepository, not specific subtype
        public RawBaseRepository save(Object id, Object entity) {
            storage.put(id, entity); // PROBLEM: No type safety
            return this; // PROBLEM: Breaks fluent API for subclasses
        }

        public Object findById(Object id) { // PROBLEM: Returns Object
            return storage.get(id);
        }

        public Collection findAll() { // PROBLEM: Raw Collection
            return new ArrayList(storage.values());
        }

        public RawBaseRepository delete(Object id) {
            storage.remove(id);
            return this; // PROBLEM: Breaks fluent API for subclasses
        }

        public abstract RawBaseRepository withValidation(Object validator);
        public abstract RawBaseRepository withTransformation(Object transformer);
    }

    public static class RawUserRepository extends RawBaseRepository {
        private Object validator = (Function<Object, Boolean>) user -> true; // PROBLEM: Raw types
        private Object transformer = Function.identity(); // PROBLEM: Raw types

        @Override
        public RawUserRepository withValidation(Object validator) {
            this.validator = validator;
            return this;
        }

        @Override
        public RawUserRepository withTransformation(Object transformer) {
            this.transformer = transformer;
            return this;
        }

        @Override
        public RawUserRepository save(Object id, Object entity) {
            // PROBLEM: Complex runtime type checking
            if (validator instanceof Function) {
                Function validatorFunc = (Function) validator;
                try {
                    if (!(Boolean) validatorFunc.apply(entity)) {
                        throw new IllegalArgumentException("User validation failed");
                    }
                } catch (ClassCastException e) {
                    throw new RuntimeException("Validation failed", e);
                }
            }

            Object transformedEntity = entity;
            if (transformer instanceof Function) {
                Function transformerFunc = (Function) transformer;
                try {
                    transformedEntity = transformerFunc.apply(entity);
                } catch (ClassCastException e) {
                    throw new RuntimeException("Transformation failed", e);
                }
            }

            return (RawUserRepository) super.save(id, transformedEntity); // PROBLEM: Unsafe cast
        }

        // PROBLEM: Method signature unclear, returns raw types
        public List findByRole(String role) {
            List result = new ArrayList();
            for (Object userObj : storage.values()) {
                // PROBLEM: Have to check type and cast at runtime
                if (userObj instanceof RawUser) {
                    RawUser user = (RawUser) userObj;
                    Set roles = user.getRoles(); // PROBLEM: Raw Set
                    if (roles.contains(role)) {
                        result.add(user);
                    }
                }
            }
            return result; // PROBLEM: Raw List returned
        }

        // PROBLEM: Method chaining broken - can't use with base methods
        public RawUserRepository withEmailValidation() {
            return withValidation((Function<Object, Boolean>) user -> {
                if (user instanceof RawUser) {
                    RawUser rawUser = (RawUser) user;
                    String email = rawUser.getEmail();
                    return email != null && email.contains("@");
                }
                return false;
            });
        }
    }

    // Demonstration of all the problems
    public static void demonstrateBuilderProblems() {
        System.out.println("=== Demonstrating Builder Pattern Problems Without Self-Bounded Generics ===");

        try {
            // PROBLEM: Fluent API is broken - this won't compile!
            /*
            RawUser user = new RawUserBuilder()
                .withName("John Doe")      // Returns RawBaseBuilder
                .withAge(30);              // ERROR: withAge() not available on RawBaseBuilder!
            */

            // WORKAROUND: Must use ugly casting or separate method calls
            RawUserBuilder userBuilder = new RawUserBuilder();
            userBuilder.withName("John Doe");  // Can't chain
            userBuilder.withDescription("Software Engineer");
            userBuilder.withTimestamp(LocalDateTime.now());
            userBuilder.withAge(30);           // Must call separately
            userBuilder.withEmail("john.doe@example.com");
            userBuilder.withRole("developer");
            userBuilder.withRole("admin");

            Object userObj = userBuilder.build(); // PROBLEM: Returns Object
            RawUser user = (RawUser) userObj;      // PROBLEM: Must cast

            // PROBLEM: Same issues with ProductBuilder
            RawProductBuilder productBuilder = new RawProductBuilder();
            productBuilder.withName("Laptop");
            productBuilder.withDescription("High-performance laptop");
            productBuilder.withPrice(1299.99);
            productBuilder.withCategory("Electronics");
            productBuilder.withAttribute("brand", "TechCorp");
            productBuilder.withAttribute("warranty", "2 years");

            Object productObj = productBuilder.build(); // PROBLEM: Returns Object
            RawProduct product = (RawProduct) productObj; // PROBLEM: Must cast

            // PROBLEM: Comparable entities lose type safety
            Map eventData1 = new HashMap();
            eventData1.put("userId", "123");
            RawTimestampedEvent event1 = new RawTimestampedEvent("evt1", "LOGIN", eventData1);

            Map eventData2 = new HashMap();
            eventData2.put("userId", "123");
            RawTimestampedEvent event2 = new RawTimestampedEvent("evt2", "LOGOUT", eventData2);

            // PROBLEM: Comparison is unsafe
            try {
                boolean event2IsNewer = event2.isNewerThan(event1);
                System.out.println("Event2 is newer: " + event2IsNewer);

                // This could cause ClassCastException:
                event2.isNewerThan("not an event"); // Runtime error!
            } catch (ClassCastException e) {
                System.out.println("ERROR in comparison: " + e.getMessage());
            }

            // PROBLEM: Processor chaining is broken
            RawStringProcessor processor = new RawStringProcessor(Function.identity());

            // PROBLEM: Can't chain methods properly
            processor.withTrimming();      // Returns RawStringProcessor
            processor.withUpperCase();     // Must call separately
            processor.withLogging("StringProcessor"); // Returns RawChainableProcessor - loses string methods!

            Object result = processor.process("  hello world  "); // PROBLEM: Returns Object
            String stringResult = (String) result; // PROBLEM: Must cast

            // PROBLEM: Repository is unsafe and verbose
            RawUserRepository userRepo = new RawUserRepository();
            userRepo.withEmailValidation();
            userRepo.withTransformation((Function<Object, Object>) userData -> {
                if (userData instanceof RawUser) {
                    RawUser rawUser = (RawUser) userData;
                    // PROBLEM: Complex casting and type checking required
                    return new RawUser(
                        rawUser.getName().toUpperCase(),
                        rawUser.getDescription(),
                        rawUser.getTimestamp(),
                        rawUser.getAge(),
                        rawUser.getEmail().toLowerCase(),
                        rawUser.getRoles()
                    );
                }
                return userData;
            });

            userRepo.save("user1", user);
            List developers = userRepo.findByRole("developer"); // PROBLEM: Raw List

            System.out.println("Raw builder patterns demonstration completed (with workarounds)");
            System.out.println("User: " + user.getName() + " (" + user.getEmail() + ")");
            System.out.println("Product: " + product.getName() + " - $" + product.getPrice());
            System.out.println("Processed string: " + stringResult);
            System.out.println("Developers found: " + developers.size());

        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n=== Summary of Builder Pattern Problems ===");
        System.out.println("1. BROKEN FLUENT API: Can't chain base class methods with subclass methods");
        System.out.println("2. UNSAFE CASTING: Must cast Object returns everywhere");
        System.out.println("3. RUNTIME TYPE CHECKING: No compile-time guarantees");
        System.out.println("4. VERBOSE CODE: Must use separate method calls instead of chaining");
        System.out.println("5. NO TYPE SAFETY: Raw collections and maps throughout");
        System.out.println("6. UNCLEAR CONTRACTS: Method signatures don't indicate expected types");
        System.out.println("7. IDE SUPPORT LOSS: No auto-completion after method calls");
        System.out.println("8. MAINTENANCE NIGHTMARE: Type errors only discovered at runtime");
        System.out.println("9. PERFORMANCE IMPACT: Boxing/unboxing and type checking overhead");
        System.out.println("10. INHERITANCE ISSUES: Can't properly extend builders");
    }
}
