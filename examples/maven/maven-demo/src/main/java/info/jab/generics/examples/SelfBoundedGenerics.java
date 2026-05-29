package info.jab.generics.examples;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * Self-bounded generics (CRTP - Curiously Recurring Template Pattern) examples
 * showcasing fluent builders, hierarchical type systems, and advanced inheritance patterns.
 */
public class SelfBoundedGenerics {

    // CRTP for fluent builder pattern
    public abstract static class BaseBuilder<T extends BaseBuilder<T>> {
        protected String name;
        protected String description;
        protected LocalDateTime timestamp;

        @SuppressWarnings("unchecked")
        protected T self() {
            return (T) this;
        }

        public T withName(String name) {
            this.name = name;
            return self();
        }

        public T withDescription(String description) {
            this.description = description;
            return self();
        }

        public T withTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return self();
        }

        protected void validate() {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalStateException("Name is required");
            }
        }
    }

    // Specific builder implementations
    public static class UserBuilder extends BaseBuilder<UserBuilder> {
        private int age;
        private String email;
        private Set<String> roles = new HashSet<>();

        public UserBuilder withAge(int age) {
            if (age < 0 || age > 150) {
                throw new IllegalArgumentException("Invalid age: " + age);
            }
            this.age = age;
            return self();
        }

        public UserBuilder withEmail(String email) {
            this.email = email;
            return self();
        }

        public UserBuilder withRole(String role) {
            this.roles.add(role);
            return self();
        }

        public UserBuilder withRoles(Collection<String> roles) {
            this.roles.addAll(roles);
            return self();
        }

        public User build() {
            validate();
            if (email == null) {
                throw new IllegalStateException("Email is required for User");
            }
            return new User(name, description, timestamp, age, email, Set.copyOf(roles));
        }
    }

    public static class ProductBuilder extends BaseBuilder<ProductBuilder> {
        private double price;
        private String category;
        private Map<String, String> attributes = new HashMap<>();

        public ProductBuilder withPrice(double price) {
            if (price < 0) {
                throw new IllegalArgumentException("Price cannot be negative");
            }
            this.price = price;
            return self();
        }

        public ProductBuilder withCategory(String category) {
            this.category = category;
            return self();
        }

        public ProductBuilder withAttribute(String key, String value) {
            this.attributes.put(key, value);
            return self();
        }

        public ProductBuilder withAttributes(Map<String, String> attributes) {
            this.attributes.putAll(attributes);
            return self();
        }

        public Product build() {
            validate();
            if (category == null) {
                throw new IllegalStateException("Category is required for Product");
            }
            return new Product(name, description, timestamp, price, category, Map.copyOf(attributes));
        }
    }

    // Result classes
    public static record User(
        String name,
        String description,
        LocalDateTime timestamp,
        int age,
        String email,
        Set<String> roles
    ) {}

    public static record Product(
        String name,
        String description,
        LocalDateTime timestamp,
        double price,
        String category,
        Map<String, String> attributes
    ) {}

    // Self-bounded comparable hierarchy
    public abstract static class ComparableEntity<T extends ComparableEntity<T>>
            implements Comparable<T> {

        protected final String id;
        protected final LocalDateTime createdAt;

            protected ComparableEntity(String id) {
        this.id = id;
        this.createdAt = LocalDateTime.now();
    }

    protected ComparableEntity(String id, LocalDateTime createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

        public String getId() {
            return id;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        // Default comparison by creation time
        @Override
        public int compareTo(T other) {
            return this.createdAt.compareTo(other.createdAt);
        }

        // Self-bounded method that returns specific type
        public abstract T clone();

        // Method that benefits from self-bounded generics
        public final boolean isNewerThan(T other) {
            return this.createdAt.isAfter(other.createdAt);
        }
    }

    // Specific implementations
    public static class TimestampedEvent extends ComparableEntity<TimestampedEvent> {
        private final String eventType;
        private final Map<String, Object> data;

        public TimestampedEvent(String id, String eventType, Map<String, Object> data) {
            super(id);
            this.eventType = eventType;
            this.data = Map.copyOf(data);
        }

        private TimestampedEvent(String id, String eventType, Map<String, Object> data, LocalDateTime createdAt) {
            super(id, createdAt);
            this.eventType = eventType;
            this.data = Map.copyOf(data);
        }

        public String getEventType() {
            return eventType;
        }

        public Map<String, Object> getData() {
            return data;
        }

        @Override
        public TimestampedEvent clone() {
            return new TimestampedEvent(this.id, this.eventType, this.data, this.createdAt);
        }

        // Additional method specific to TimestampedEvent
        public TimestampedEvent withAdditionalData(String key, Object value) {
            Map<String, Object> newData = new HashMap<>(this.data);
            newData.put(key, value);
            return new TimestampedEvent(this.id, this.eventType, newData);
        }
    }

    public static class AuditRecord extends ComparableEntity<AuditRecord> {
        private final String action;
        private final String userId;
        private final String details;

        public AuditRecord(String id, String action, String userId, String details) {
            super(id);
            this.action = action;
            this.userId = userId;
            this.details = details;
        }

        private AuditRecord(String id, String action, String userId, String details, LocalDateTime createdAt) {
            super(id, createdAt);
            this.action = action;
            this.userId = userId;
            this.details = details;
        }

        public String getAction() {
            return action;
        }

        public String getUserId() {
            return userId;
        }

        public String getDetails() {
            return details;
        }

        @Override
        public AuditRecord clone() {
            return new AuditRecord(this.id, this.action, this.userId, this.details, this.createdAt);
        }

        // Comparison override for audit records (by action first, then time)
        @Override
        public int compareTo(AuditRecord other) {
            int actionComparison = this.action.compareTo(other.action);
            return actionComparison != 0 ? actionComparison : super.compareTo(other);
        }
    }

    // Self-bounded processor pattern
    public abstract static class ChainableProcessor<T, R, P extends ChainableProcessor<T, R, P>> {
        protected Function<T, R> processor;

        protected ChainableProcessor(Function<T, R> processor) {
            this.processor = processor;
        }

        @SuppressWarnings("unchecked")
        protected P self() {
            return (P) this;
        }

        public abstract P addPreProcessor(Function<T, T> preProcessor);
        public abstract P addPostProcessor(Function<R, R> postProcessor);

        public R process(T input) {
            return processor.apply(input);
        }

        // Method that benefits from self-bounded return type
        public P withLogging(String logPrefix) {
            Function<T, R> originalProcessor = this.processor;
            this.processor = input -> {
                System.out.println(logPrefix + " Processing: " + input);
                R result = originalProcessor.apply(input);
                System.out.println(logPrefix + " Result: " + result);
                return result;
            };
            return self();
        }
    }

    public static class StringProcessor extends ChainableProcessor<String, String, StringProcessor> {
        public StringProcessor(Function<String, String> processor) {
            super(processor);
        }

        @Override
        public StringProcessor addPreProcessor(Function<String, String> preProcessor) {
            this.processor = this.processor.compose(preProcessor);
            return self();
        }

        @Override
        public StringProcessor addPostProcessor(Function<String, String> postProcessor) {
            this.processor = this.processor.andThen(postProcessor);
            return self();
        }

        // String-specific methods
        public StringProcessor withTrimming() {
            return addPreProcessor(String::trim);
        }

        public StringProcessor withUpperCase() {
            return addPostProcessor(String::toUpperCase);
        }

        public StringProcessor withPrefixSuffix(String prefix, String suffix) {
            return addPostProcessor(s -> prefix + s + suffix);
        }
    }

    // Generic repository pattern with self-bounded types
    public abstract static class BaseRepository<E, ID, R extends BaseRepository<E, ID, R>> {
        protected final Map<ID, E> storage = new HashMap<>();

        @SuppressWarnings("unchecked")
        protected R self() {
            return (R) this;
        }

        public R save(ID id, E entity) {
            storage.put(id, entity);
            return self();
        }

        public Optional<E> findById(ID id) {
            return Optional.ofNullable(storage.get(id));
        }

        public Collection<E> findAll() {
            return new ArrayList<>(storage.values());
        }

        public R delete(ID id) {
            storage.remove(id);
            return self();
        }

        public abstract R withValidation(Function<E, Boolean> validator);
        public abstract R withTransformation(Function<E, E> transformer);
    }

    public static class UserRepository extends BaseRepository<User, String, UserRepository> {
        private Function<User, Boolean> validator = user -> true;
        private Function<User, User> transformer = Function.identity();

        @Override
        public UserRepository withValidation(Function<User, Boolean> validator) {
            this.validator = validator;
            return self();
        }

        @Override
        public UserRepository withTransformation(Function<User, User> transformer) {
            this.transformer = transformer;
            return self();
        }

        @Override
        public UserRepository save(String id, User user) {
            if (!validator.apply(user)) {
                throw new IllegalArgumentException("User validation failed");
            }
            User transformedUser = transformer.apply(user);
            return super.save(id, transformedUser);
        }

        // User-specific methods
        public List<User> findByRole(String role) {
            return storage.values().stream()
                .filter(user -> user.roles().contains(role))
                .toList();
        }

        public UserRepository withEmailValidation() {
            return withValidation(user ->
                user.email() != null && user.email().contains("@"));
        }
    }

    // Demonstration methods
    public static void demonstrateSelfBoundedPatterns() {
        // Fluent builder demonstration
        User user = new UserBuilder()
            .withName("John Doe")
            .withDescription("Software Engineer")
            .withTimestamp(LocalDateTime.now())
            .withAge(30)
            .withEmail("john.doe@example.com")
            .withRole("developer")
            .withRole("admin")
            .build();

        Product product = new ProductBuilder()
            .withName("Laptop")
            .withDescription("High-performance laptop")
            .withPrice(1299.99)
            .withCategory("Electronics")
            .withAttribute("brand", "TechCorp")
            .withAttribute("warranty", "2 years")
            .build();

        // Comparable entities demonstration
        TimestampedEvent event1 = new TimestampedEvent("evt1", "LOGIN", Map.of("userId", "123"));
        TimestampedEvent event2 = new TimestampedEvent("evt2", "LOGOUT", Map.of("userId", "123"));

        boolean event2IsNewer = event2.isNewerThan(event1);

        // Chainable processor demonstration
        StringProcessor processor = new StringProcessor(Function.identity())
            .withTrimming()
            .withUpperCase()
            .withPrefixSuffix("[", "]")
            .withLogging("StringProcessor");

        String result = processor.process("  hello world  ");

        // Repository demonstration
        UserRepository userRepo = new UserRepository()
            .withEmailValidation()
            .withTransformation(userData -> new User(
                userData.name().toUpperCase(),
                userData.description(),
                userData.timestamp(),
                userData.age(),
                userData.email().toLowerCase(),
                userData.roles()
            ));

        userRepo.save("user1", user);
        List<User> developers = userRepo.findByRole("developer");

        System.out.println("Self-bounded patterns demonstration completed");
        System.out.println("User: " + user);
        System.out.println("Product: " + product);
        System.out.println("Event2 is newer: " + event2IsNewer);
        System.out.println("Processed string: " + result);
        System.out.println("Developers found: " + developers.size());
    }
}
