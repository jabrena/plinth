package info.jab.generics.examples;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("SelfBoundedGenerics Tests")
class SelfBoundedGenericsTest {

    @Nested
    @DisplayName("Fluent Builder Tests")
    class FluentBuilderTests {

        @Nested
        @DisplayName("UserBuilder Tests")
        class UserBuilderTests {

            private SelfBoundedGenerics.UserBuilder userBuilder;
            private LocalDateTime testTime;

            @BeforeEach
            void setUp() {
                userBuilder = new SelfBoundedGenerics.UserBuilder();
                testTime = LocalDateTime.of(2023, 1, 1, 12, 0);
            }

            @Test
            @DisplayName("Should build user with all properties")
            void build_allProperties_createsCorrectUser() {
                // When
                SelfBoundedGenerics.User user = userBuilder
                    .withName("John Doe")
                    .withDescription("Software Engineer")
                    .withTimestamp(testTime)
                    .withAge(30)
                    .withEmail("john.doe@example.com")
                    .withRole("developer")
                    .withRole("admin")
                    .build();

                // Then
                assertThat(user.name()).isEqualTo("John Doe");
                assertThat(user.description()).isEqualTo("Software Engineer");
                assertThat(user.timestamp()).isEqualTo(testTime);
                assertThat(user.age()).isEqualTo(30);
                assertThat(user.email()).isEqualTo("john.doe@example.com");
                assertThat(user.roles()).containsExactlyInAnyOrder("developer", "admin");
            }

            @Test
            @DisplayName("Should build user with roles collection")
            void withRoles_collection_addsAllRoles() {
                // Given
                Collection<String> roles = Arrays.asList("user", "moderator", "admin");

                // When
                SelfBoundedGenerics.User user = userBuilder
                    .withName("Jane Doe")
                    .withEmail("jane@example.com")
                    .withRoles(roles)
                    .build();

                // Then
                assertThat(user.roles()).containsExactlyInAnyOrder("user", "moderator", "admin");
            }

            @Test
            @DisplayName("Should throw exception for invalid age")
            void withAge_invalidAge_throwsException() {
                // When & Then
                assertThatThrownBy(() -> userBuilder.withAge(-1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Invalid age: -1");

                assertThatThrownBy(() -> userBuilder.withAge(200))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Invalid age: 200");
            }

            @Test
            @DisplayName("Should accept valid age boundaries")
            void withAge_validBoundaries_acceptsAge() {
                // When & Then
                assertThatCode(() -> {
                    userBuilder.withAge(0);
                    userBuilder.withAge(150);
                }).doesNotThrowAnyException();
            }

            @Test
            @DisplayName("Should throw exception when name is missing")
            void build_missingName_throwsException() {
                // When & Then
                assertThatThrownBy(() -> userBuilder
                    .withEmail("test@example.com")
                    .build())
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Name is required");
            }

            @Test
            @DisplayName("Should throw exception when email is missing")
            void build_missingEmail_throwsException() {
                // When & Then
                assertThatThrownBy(() -> userBuilder
                    .withName("John Doe")
                    .build())
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Email is required for User");
            }

            @Test
            @DisplayName("Should throw exception when name is empty")
            void build_emptyName_throwsException() {
                // When & Then
                assertThatThrownBy(() -> userBuilder
                    .withName("   ")
                    .withEmail("test@example.com")
                    .build())
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Name is required");
            }

            @Test
            @DisplayName("Should return same builder instance for fluent chaining")
            void fluentMethods_chaining_returnsSameInstance() {
                // When
                SelfBoundedGenerics.UserBuilder result1 = userBuilder.withName("Test");
                SelfBoundedGenerics.UserBuilder result2 = userBuilder.withAge(25);
                SelfBoundedGenerics.UserBuilder result3 = userBuilder.withEmail("test@example.com");

                // Then
                assertThat(result1).isSameAs(userBuilder);
                assertThat(result2).isSameAs(userBuilder);
                assertThat(result3).isSameAs(userBuilder);
            }
        }

        @Nested
        @DisplayName("ProductBuilder Tests")
        class ProductBuilderTests {

            private SelfBoundedGenerics.ProductBuilder productBuilder;
            private LocalDateTime testTime;

            @BeforeEach
            void setUp() {
                productBuilder = new SelfBoundedGenerics.ProductBuilder();
                testTime = LocalDateTime.of(2023, 1, 1, 12, 0);
            }

            @Test
            @DisplayName("Should build product with all properties")
            void build_allProperties_createsCorrectProduct() {
                // Given
                Map<String, String> attributes = Map.of("brand", "TechCorp", "warranty", "2 years");

                // When
                SelfBoundedGenerics.Product product = productBuilder
                    .withName("Laptop")
                    .withDescription("High-performance laptop")
                    .withTimestamp(testTime)
                    .withPrice(1299.99)
                    .withCategory("Electronics")
                    .withAttribute("color", "silver")
                    .withAttributes(attributes)
                    .build();

                // Then
                assertThat(product.name()).isEqualTo("Laptop");
                assertThat(product.description()).isEqualTo("High-performance laptop");
                assertThat(product.timestamp()).isEqualTo(testTime);
                assertThat(product.price()).isEqualTo(1299.99);
                assertThat(product.category()).isEqualTo("Electronics");
                assertThat(product.attributes())
                    .containsEntry("color", "silver")
                    .containsEntry("brand", "TechCorp")
                    .containsEntry("warranty", "2 years");
            }

            @Test
            @DisplayName("Should throw exception for negative price")
            void withPrice_negativePrice_throwsException() {
                // When & Then
                assertThatThrownBy(() -> productBuilder.withPrice(-100.0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Price cannot be negative");
            }

            @Test
            @DisplayName("Should accept zero price")
            void withPrice_zeroPrice_acceptsPrice() {
                // When & Then
                assertThatCode(() -> productBuilder.withPrice(0.0))
                    .doesNotThrowAnyException();
            }

            @Test
            @DisplayName("Should throw exception when category is missing")
            void build_missingCategory_throwsException() {
                // When & Then
                assertThatThrownBy(() -> productBuilder
                    .withName("Test Product")
                    .withPrice(100.0)
                    .build())
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Category is required for Product");
            }

            @Test
            @DisplayName("Should add single attributes correctly")
            void withAttribute_singleAttribute_addsCorrectly() {
                // When
                SelfBoundedGenerics.Product product = productBuilder
                    .withName("Test")
                    .withCategory("Test")
                    .withAttribute("key1", "value1")
                    .withAttribute("key2", "value2")
                    .build();

                // Then
                assertThat(product.attributes())
                    .containsEntry("key1", "value1")
                    .containsEntry("key2", "value2");
            }

            @Test
            @DisplayName("Should return same builder instance for fluent chaining")
            void fluentMethods_chaining_returnsSameInstance() {
                // When
                SelfBoundedGenerics.ProductBuilder result1 = productBuilder.withName("Test");
                SelfBoundedGenerics.ProductBuilder result2 = productBuilder.withPrice(100.0);
                SelfBoundedGenerics.ProductBuilder result3 = productBuilder.withCategory("Test");

                // Then
                assertThat(result1).isSameAs(productBuilder);
                assertThat(result2).isSameAs(productBuilder);
                assertThat(result3).isSameAs(productBuilder);
            }
        }
    }

    @Nested
    @DisplayName("Comparable Entity Tests")
    class ComparableEntityTests {

        @Nested
        @DisplayName("TimestampedEvent Tests")
        class TimestampedEventTests {

            @Test
            @DisplayName("Should create timestamped event with correct properties")
            void constructor_validParameters_createsCorrectEvent() {
                // Given
                Map<String, Object> data = Map.of("userId", "123", "action", "login");

                // When
                SelfBoundedGenerics.TimestampedEvent event = new SelfBoundedGenerics.TimestampedEvent(
                    "evt1", "LOGIN", data);

                // Then
                assertThat(event.getId()).isEqualTo("evt1");
                assertThat(event.getEventType()).isEqualTo("LOGIN");
                assertThat(event.getData()).containsAllEntriesOf(data);
                assertThat(event.getCreatedAt()).isNotNull();
            }

            @Test
            @DisplayName("Should compare events by creation time")
            void compareTo_differentCreationTimes_comparesCorrectly() throws InterruptedException {
                // Given
                SelfBoundedGenerics.TimestampedEvent event1 = new SelfBoundedGenerics.TimestampedEvent(
                    "evt1", "LOGIN", Collections.emptyMap());

                Thread.sleep(1); // Ensure different timestamps

                SelfBoundedGenerics.TimestampedEvent event2 = new SelfBoundedGenerics.TimestampedEvent(
                    "evt2", "LOGOUT", Collections.emptyMap());

                // When
                int comparison = event1.compareTo(event2);
                boolean event2IsNewer = event2.isNewerThan(event1);

                // Then
                assertThat(comparison).isLessThan(0); // event1 < event2
                assertThat(event2IsNewer).isTrue();
            }

            @Test
            @DisplayName("Should clone event correctly")
            void clone_validEvent_createsIdenticalCopy() {
                // Given
                Map<String, Object> data = Map.of("key", "value");
                SelfBoundedGenerics.TimestampedEvent original = new SelfBoundedGenerics.TimestampedEvent(
                    "evt1", "TEST", data);

                // When
                SelfBoundedGenerics.TimestampedEvent cloned = original.clone();

                // Then
                assertThat(cloned)
                    .isNotSameAs(original)
                    .usingRecursiveComparison()
                    .isEqualTo(original);
            }

            @Test
            @DisplayName("Should create event with additional data")
            void withAdditionalData_validData_createsNewEventWithMergedData() {
                // Given
                Map<String, Object> originalData = Map.of("key1", "value1");
                SelfBoundedGenerics.TimestampedEvent original = new SelfBoundedGenerics.TimestampedEvent(
                    "evt1", "TEST", originalData);

                // When
                SelfBoundedGenerics.TimestampedEvent enhanced = original.withAdditionalData("key2", "value2");

                // Then
                assertThat(enhanced.getData())
                    .containsEntry("key1", "value1")
                    .containsEntry("key2", "value2");
                assertThat(enhanced.getId()).isEqualTo(original.getId());
                assertThat(enhanced.getEventType()).isEqualTo(original.getEventType());
            }
        }

        @Nested
        @DisplayName("AuditRecord Tests")
        class AuditRecordTests {

            @Test
            @DisplayName("Should create audit record with correct properties")
            void constructor_validParameters_createsCorrectRecord() {
                // When
                SelfBoundedGenerics.AuditRecord record = new SelfBoundedGenerics.AuditRecord(
                    "audit1", "CREATE", "user123", "Created new resource");

                // Then
                assertThat(record.getId()).isEqualTo("audit1");
                assertThat(record.getAction()).isEqualTo("CREATE");
                assertThat(record.getUserId()).isEqualTo("user123");
                assertThat(record.getDetails()).isEqualTo("Created new resource");
                assertThat(record.getCreatedAt()).isNotNull();
            }

            @Test
            @DisplayName("Should compare records by action first, then by time")
            void compareTo_differentActions_comparesByAction() throws InterruptedException {
                // Given
                SelfBoundedGenerics.AuditRecord createRecord = new SelfBoundedGenerics.AuditRecord(
                    "audit1", "CREATE", "user1", "details");

                Thread.sleep(1);

                SelfBoundedGenerics.AuditRecord deleteRecord = new SelfBoundedGenerics.AuditRecord(
                    "audit2", "DELETE", "user1", "details");

                // When
                int comparison = createRecord.compareTo(deleteRecord);

                // Then
                assertThat(comparison).isLessThan(0); // CREATE < DELETE alphabetically
            }

            @Test
            @DisplayName("Should compare records by time when actions are same")
            void compareTo_sameAction_comparesByTime() throws InterruptedException {
                // Given
                SelfBoundedGenerics.AuditRecord record1 = new SelfBoundedGenerics.AuditRecord(
                    "audit1", "CREATE", "user1", "details");

                Thread.sleep(1);

                SelfBoundedGenerics.AuditRecord record2 = new SelfBoundedGenerics.AuditRecord(
                    "audit2", "CREATE", "user2", "details");

                // When
                int comparison = record1.compareTo(record2);

                // Then
                assertThat(comparison).isLessThan(0); // record1 < record2 by time
            }

            @Test
            @DisplayName("Should clone audit record correctly")
            void clone_validRecord_createsIdenticalCopy() {
                // Given
                SelfBoundedGenerics.AuditRecord original = new SelfBoundedGenerics.AuditRecord(
                    "audit1", "UPDATE", "user1", "Updated resource");

                // When
                SelfBoundedGenerics.AuditRecord cloned = original.clone();

                // Then
                assertThat(cloned)
                    .isNotSameAs(original)
                    .usingRecursiveComparison()
                    .isEqualTo(original);
            }
        }
    }

    @Nested
    @DisplayName("Chainable Processor Tests")
    class ChainableProcessorTests {

        @Nested
        @DisplayName("StringProcessor Tests")
        class StringProcessorTests {

            @Test
            @DisplayName("Should process string with identity function")
            void constructor_identityFunction_processesCorrectly() {
                // Given
                SelfBoundedGenerics.StringProcessor processor =
                    new SelfBoundedGenerics.StringProcessor(Function.identity());

                // When
                String result = processor.process("test");

                // Then
                assertThat(result).isEqualTo("test");
            }

            @Test
            @DisplayName("Should add pre-processor correctly")
            void addPreProcessor_validFunction_modifiesProcessing() {
                // Given
                SelfBoundedGenerics.StringProcessor processor =
                    new SelfBoundedGenerics.StringProcessor(Function.identity());

                // When
                String result = processor
                    .addPreProcessor(String::toUpperCase)
                    .process("hello");

                // Then
                assertThat(result).isEqualTo("HELLO");
            }

            @Test
            @DisplayName("Should add post-processor correctly")
            void addPostProcessor_validFunction_modifiesProcessing() {
                // Given
                SelfBoundedGenerics.StringProcessor processor =
                    new SelfBoundedGenerics.StringProcessor(Function.identity());

                // When
                String result = processor
                    .addPostProcessor(s -> s + "!")
                    .process("hello");

                // Then
                assertThat(result).isEqualTo("hello!");
            }

            @Test
            @DisplayName("Should chain multiple processors")
            void chainProcessors_multipleProcessors_appliesInOrder() {
                // Given
                SelfBoundedGenerics.StringProcessor processor =
                    new SelfBoundedGenerics.StringProcessor(Function.identity());

                // When
                String result = processor
                    .addPreProcessor(String::trim)
                    .addPostProcessor(String::toUpperCase)
                    .addPostProcessor(s -> "[" + s + "]")
                    .process("  hello  ");

                // Then
                assertThat(result).isEqualTo("[HELLO]");
            }

            @Test
            @DisplayName("Should add trimming processor")
            void withTrimming_validInput_trimsInput() {
                // Given
                SelfBoundedGenerics.StringProcessor processor =
                    new SelfBoundedGenerics.StringProcessor(Function.identity());

                // When
                String result = processor
                    .withTrimming()
                    .process("  hello world  ");

                // Then
                assertThat(result).isEqualTo("hello world");
            }

            @Test
            @DisplayName("Should add upper case processor")
            void withUpperCase_validInput_convertsToUpperCase() {
                // Given
                SelfBoundedGenerics.StringProcessor processor =
                    new SelfBoundedGenerics.StringProcessor(Function.identity());

                // When
                String result = processor
                    .withUpperCase()
                    .process("hello");

                // Then
                assertThat(result).isEqualTo("HELLO");
            }

            @Test
            @DisplayName("Should add prefix and suffix")
            void withPrefixSuffix_validInput_addsCorrectly() {
                // Given
                SelfBoundedGenerics.StringProcessor processor =
                    new SelfBoundedGenerics.StringProcessor(Function.identity());

                // When
                String result = processor
                    .withPrefixSuffix("<<", ">>")
                    .process("hello");

                // Then
                assertThat(result).isEqualTo("<<hello>>");
            }

            @Test
            @DisplayName("Should add logging without affecting result")
            void withLogging_validInput_processesSameButLogs() {
                // Given
                SelfBoundedGenerics.StringProcessor processor =
                    new SelfBoundedGenerics.StringProcessor(String::toUpperCase);

                // When
                String result = processor
                    .withLogging("TEST")
                    .process("hello");

                // Then
                assertThat(result).isEqualTo("HELLO");
            }

            @Test
            @DisplayName("Should return same instance for fluent chaining")
            void fluentMethods_chaining_returnsSameInstance() {
                // Given
                SelfBoundedGenerics.StringProcessor processor =
                    new SelfBoundedGenerics.StringProcessor(Function.identity());

                // When
                SelfBoundedGenerics.StringProcessor result1 = processor.withTrimming();
                SelfBoundedGenerics.StringProcessor result2 = processor.withUpperCase();
                SelfBoundedGenerics.StringProcessor result3 = processor.withLogging("test");

                // Then
                assertThat(result1).isSameAs(processor);
                assertThat(result2).isSameAs(processor);
                assertThat(result3).isSameAs(processor);
            }
        }
    }

    @Nested
    @DisplayName("Repository Pattern Tests")
    class RepositoryPatternTests {

        @Nested
        @DisplayName("UserRepository Tests")
        class UserRepositoryTests {

            private SelfBoundedGenerics.UserRepository repository;
            private SelfBoundedGenerics.User testUser;

            @BeforeEach
            void setUp() {
                repository = new SelfBoundedGenerics.UserRepository();
                testUser = new SelfBoundedGenerics.User(
                    "John Doe",
                    "Test user",
                    LocalDateTime.now(),
                    30,
                    "john@example.com",
                    Set.of("developer", "admin")
                );
            }

            @Test
            @DisplayName("Should save and retrieve user")
            void save_validUser_storesAndRetrievesCorrectly() {
                // When
                repository.save("user1", testUser);
                Optional<SelfBoundedGenerics.User> retrieved = repository.findById("user1");

                // Then
                assertThat(retrieved).isPresent().contains(testUser);
            }

            @Test
            @DisplayName("Should return empty for non-existent user")
            void findById_nonExistentUser_returnsEmpty() {
                // When
                Optional<SelfBoundedGenerics.User> result = repository.findById("nonexistent");

                // Then
                assertThat(result).isEmpty();
            }

            @Test
            @DisplayName("Should find all users")
            void findAll_multipleUsers_returnsAllUsers() {
                // Given
                SelfBoundedGenerics.User user2 = new SelfBoundedGenerics.User(
                    "Jane Doe", "Test user 2", LocalDateTime.now(), 25, "jane@example.com", Set.of("user"));

                // When
                repository.save("user1", testUser);
                repository.save("user2", user2);
                Collection<SelfBoundedGenerics.User> allUsers = repository.findAll();

                // Then
                assertThat(allUsers).containsExactlyInAnyOrder(testUser, user2);
            }

            @Test
            @DisplayName("Should delete user")
            void delete_existingUser_removesUser() {
                // Given
                repository.save("user1", testUser);

                // When
                repository.delete("user1");
                Optional<SelfBoundedGenerics.User> result = repository.findById("user1");

                // Then
                assertThat(result).isEmpty();
            }

            @Test
            @DisplayName("Should find users by role")
            void findByRole_existingRole_returnsUsersWithRole() {
                // Given
                SelfBoundedGenerics.User user2 = new SelfBoundedGenerics.User(
                    "Jane Doe", "Test user 2", LocalDateTime.now(), 25, "jane@example.com", Set.of("user"));

                repository.save("user1", testUser);
                repository.save("user2", user2);

                // When
                List<SelfBoundedGenerics.User> developers = repository.findByRole("developer");
                List<SelfBoundedGenerics.User> users = repository.findByRole("user");

                // Then
                assertThat(developers).containsExactly(testUser);
                assertThat(users).containsExactly(user2);
            }

            @Test
            @DisplayName("Should apply validation during save")
            void save_withValidation_appliesValidation() {
                // Given
                SelfBoundedGenerics.User invalidUser = new SelfBoundedGenerics.User(
                    "Invalid", "Test", LocalDateTime.now(), 30, "invalid-email", Set.of());

                repository.withValidation(user -> user.email().contains("@"));

                // When & Then
                assertThatThrownBy(() -> repository.save("invalid", invalidUser))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("User validation failed");
            }

            @Test
            @DisplayName("Should apply transformation during save")
            void save_withTransformation_appliesTransformation() {
                // Given
                Function<SelfBoundedGenerics.User, SelfBoundedGenerics.User> nameUpperCase = user ->
                    new SelfBoundedGenerics.User(
                        user.name().toUpperCase(),
                        user.description(),
                        user.timestamp(),
                        user.age(),
                        user.email(),
                        user.roles()
                    );

                repository.withTransformation(nameUpperCase);

                // When
                repository.save("user1", testUser);
                Optional<SelfBoundedGenerics.User> retrieved = repository.findById("user1");

                // Then
                assertThat(retrieved).isPresent();
                assertThat(retrieved.get().name()).isEqualTo("JOHN DOE");
            }

            @Test
            @DisplayName("Should configure email validation")
            void withEmailValidation_invalidEmail_rejectsUser() {
                // Given
                SelfBoundedGenerics.User invalidUser = new SelfBoundedGenerics.User(
                    "Test", "Test", LocalDateTime.now(), 30, "invalid-email", Set.of());

                repository.withEmailValidation();

                // When & Then
                assertThatThrownBy(() -> repository.save("invalid", invalidUser))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("User validation failed");
            }

            @Test
            @DisplayName("Should return same instance for fluent chaining")
            void fluentMethods_chaining_returnsSameInstance() {
                // When
                SelfBoundedGenerics.UserRepository result1 = repository.withEmailValidation();
                SelfBoundedGenerics.UserRepository result2 = repository.withValidation(user -> true);

                // Then
                assertThat(result1).isSameAs(repository);
                assertThat(result2).isSameAs(repository);
            }
        }
    }

    @ParameterizedTest(name = "Should validate {0} correctly")
    @MethodSource("provideSelfBoundedScenarios")
    @DisplayName("Should handle various self-bounded scenarios")
    void selfBoundedScenarios_variousInputs_handlesCorrectly(String description, Runnable scenario) {
        // When & Then
        assertThatCode(scenario::run).doesNotThrowAnyException();
    }

    static Stream<Arguments> provideSelfBoundedScenarios() {
        return Stream.of(
            Arguments.of("Builder pattern demonstration", (Runnable) () -> {
                new SelfBoundedGenerics.UserBuilder()
                    .withName("Test User")
                    .withEmail("test@example.com")
                    .withAge(25)
                    .build();
            }),
            Arguments.of("Processor chaining", (Runnable) () -> {
                new SelfBoundedGenerics.StringProcessor(Function.identity())
                    .withTrimming()
                    .withUpperCase()
                    .process("  test  ");
            }),
            Arguments.of("Repository operations", (Runnable) () -> {
                SelfBoundedGenerics.UserRepository repo = new SelfBoundedGenerics.UserRepository();
                SelfBoundedGenerics.User user = new SelfBoundedGenerics.User(
                    "Test", "Test", LocalDateTime.now(), 30, "test@example.com", Set.of());
                repo.save("test", user);
                repo.findById("test");
            }),
            Arguments.of("Complete demonstration", (Runnable) () -> {
                SelfBoundedGenerics.demonstrateSelfBoundedPatterns();
            })
        );
    }

    @Test
    @DisplayName("Should demonstrate self-bounded patterns without error")
    void demonstrateSelfBoundedPatterns_execution_completesWithoutError() {
        // When & Then
        assertThatCode(() -> SelfBoundedGenerics.demonstrateSelfBoundedPatterns())
            .doesNotThrowAnyException();
    }
}
