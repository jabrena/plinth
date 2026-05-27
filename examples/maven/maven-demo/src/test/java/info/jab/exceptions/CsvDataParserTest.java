package info.jab.exceptions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("CSV Data Parser Tests")
class CsvDataParserTest {

    private CsvDataParser parser;

    @BeforeEach
    void setUp() {
        parser = new CsvDataParser();
    }

    @Nested
    @DisplayName("parsePerson Happy Path Tests")
    class ParsePersonHappyPathTests {

        @Test
        @DisplayName("Should parse valid CSV line with all fields correctly")
        void should_parseValidCsvLine_when_allFieldsAreValid() throws DataParsingException {
            // Given
            String csvLine = "1,John Doe,john.doe@example.com,1990-05-15,75000.50";
            int lineNumber = 1;

            // When
            CsvDataParser.Person result = parser.parsePerson(csvLine, lineNumber);

            // Then
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getName()).isEqualTo("John Doe");
            assertThat(result.getEmail()).isEqualTo("john.doe@example.com");
            assertThat(result.getBirthDate()).isEqualTo(LocalDate.of(1990, 5, 15));
            assertThat(result.getSalary()).isEqualTo(new BigDecimal("75000.50"));
        }

        @ParameterizedTest(name = "{index} Should parse person with ID: {0}")
        @ValueSource(longs = {1, 999, 1000, 999999})
        @DisplayName("Should parse various valid ID values")
        void should_parseValidIds_when_idsArePositive(long id) throws DataParsingException {
            // Given
            String csvLine = id + ",Jane Smith,jane@example.com,1985-12-25,60000.00";
            int lineNumber = 1;

            // When
            CsvDataParser.Person result = parser.parsePerson(csvLine, lineNumber);

            // Then
            assertThat(result.getId()).isEqualTo(id);
        }

        @ParameterizedTest(name = "{index} Should parse email: {0}")
        @ValueSource(strings = {
            "user@example.com",
            "user.name@example.com",
            "user+tag@example.co.uk",
            "test123@sub.domain.com"
        })
        @DisplayName("Should parse various valid email formats")
        void should_parseValidEmails_when_emailFormatsAreCorrect(String email) throws DataParsingException {
            // Given
            String csvLine = "1,John Doe," + email + ",1990-05-15,75000.50";
            int lineNumber = 1;

            // When
            CsvDataParser.Person result = parser.parsePerson(csvLine, lineNumber);

            // Then
            assertThat(result.getEmail()).isEqualTo(email.toLowerCase());
        }

        @ParameterizedTest(name = "{index} Should parse salary: {0}")
        @CsvSource({
            "0.00",
            "1.00",
            "999999.99",
            "50000",
            "75000.5"
        })
        @DisplayName("Should parse various valid salary values")
        void should_parseValidSalaries_when_salariesAreInValidRange(String salary) throws DataParsingException {
            // Given
            String csvLine = "1,John Doe,john@example.com,1990-05-15," + salary;
            int lineNumber = 1;

            // When
            CsvDataParser.Person result = parser.parsePerson(csvLine, lineNumber);

            // Then
            assertThat(result.getSalary()).isEqualTo(new BigDecimal(salary));
        }

        @Test
        @DisplayName("Should handle email case normalization")
        void should_normalizeEmailCase_when_emailHasMixedCase() throws DataParsingException {
            // Given
            String csvLine = "1,John Doe,John.DOE@EXAMPLE.COM,1990-05-15,75000.50";
            int lineNumber = 1;

            // When
            CsvDataParser.Person result = parser.parsePerson(csvLine, lineNumber);

            // Then
            assertThat(result.getEmail()).isEqualTo("john.doe@example.com");
        }
    }

    @Nested
    @DisplayName("parsePerson Input Validation Tests")
    class ParsePersonValidationTests {

        @Test
        @DisplayName("Should throw IllegalArgumentException when CSV line is null")
        void should_throwIllegalArgumentException_when_csvLineIsNull() {
            // Given
            String csvLine = null;
            int lineNumber = 1;

            // When & Then
            assertThatThrownBy(() -> parser.parsePerson(csvLine, lineNumber))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("CSV line cannot be null");
        }

        @ParameterizedTest(name = "{index} Empty line: '{0}'")
        @ValueSource(strings = {"", "   ", "\t", "\n"})
        @DisplayName("Should throw MissingDataException when CSV line is empty or whitespace")
        void should_throwMissingDataException_when_csvLineIsEmptyOrWhitespace(String emptyLine) {
            // Given
            int lineNumber = 1;

            // When & Then
            assertThatThrownBy(() -> parser.parsePerson(emptyLine, lineNumber))
                .isInstanceOf(DataParsingException.MissingDataException.class)
                .hasMessageContaining("CSV line is empty");
        }

        @ParameterizedTest(name = "{index} Field count: {1}")
        @CsvSource({
            "'1,John Doe,john@example.com,1990-05-15', 4",
            "'1,John Doe,john@example.com', 3",
            "'1,John Doe', 2",
            "'1', 1",
            "'1,John Doe,john@example.com,1990-05-15,75000.50,extra', 6"
        })
        @DisplayName("Should throw InvalidFormatException when field count is incorrect")
        void should_throwInvalidFormatException_when_fieldCountIsIncorrect(String csvLine, int fieldCount) {
            // Given
            int lineNumber = 1;

            // When & Then
            assertThatThrownBy(() -> parser.parsePerson(csvLine, lineNumber))
                .isInstanceOf(DataParsingException.InvalidFormatException.class)
                .hasMessageContaining("Expected 5 fields but found " + fieldCount);
        }
    }

    @Nested
    @DisplayName("parsePerson Field-Specific Exception Tests")
    class ParsePersonFieldExceptionTests {

        @Nested
        @DisplayName("ID Field Tests")
        class IdFieldTests {

            @ParameterizedTest(name = "{index} Invalid ID: '{0}'")
            @ValueSource(strings = {"", "   ", "abc", "1.5", "-1", "0"})
            @DisplayName("Should throw exception for invalid ID values")
            void should_throwException_when_idIsInvalid(String invalidId) {
                // Given
                String csvLine = invalidId + ",John Doe,john@example.com,1990-05-15,75000.50";
                int lineNumber = 1;

                // When & Then
                assertThatThrownBy(() -> parser.parsePerson(csvLine, lineNumber))
                    .isInstanceOf(DataParsingException.class)
                    .satisfies(exception -> {
                        if (invalidId.trim().isEmpty()) {
                            assertThat(exception).isInstanceOf(DataParsingException.MissingDataException.class);
                        } else if (invalidId.equals("0") || invalidId.equals("-1")) {
                            assertThat(exception).isInstanceOf(DataParsingException.DataConstraintException.class);
                        } else {
                            assertThat(exception).isInstanceOf(DataParsingException.InvalidFormatException.class);
                        }
                    });
            }
        }

        @Nested
        @DisplayName("Name Field Tests")
        class NameFieldTests {

            @ParameterizedTest(name = "{index} Invalid name: '{0}'")
            @ValueSource(strings = {"", "   "})
            @DisplayName("Should throw MissingDataException for empty names")
            void should_throwMissingDataException_when_nameIsEmpty(String emptyName) {
                // Given
                String csvLine = "1," + emptyName + ",john@example.com,1990-05-15,75000.50";
                int lineNumber = 1;

                // When & Then
                assertThatThrownBy(() -> parser.parsePerson(csvLine, lineNumber))
                    .isInstanceOf(DataParsingException.MissingDataException.class)
                    .hasMessageContaining("Name cannot be empty");
            }

            @Test
            @DisplayName("Should throw DataConstraintException when name exceeds maximum length")
            void should_throwDataConstraintException_when_nameExceedsMaxLength() {
                // Given
                String longName = "A".repeat(256); // Exceeds MAX_STRING_LENGTH (255)
                String csvLine = "1," + longName + ",john@example.com,1990-05-15,75000.50";
                int lineNumber = 1;

                // When & Then
                assertThatThrownBy(() -> parser.parsePerson(csvLine, lineNumber))
                    .isInstanceOf(DataParsingException.DataConstraintException.class)
                    .hasMessageContaining("Name exceeds maximum length");
            }

            @ParameterizedTest(name = "{index} Invalid character in name: '{0}'")
            @ValueSource(strings = {"John<Doe", "Jane>Smith", "Test\"Name", "O'Connor&Co"})
            @DisplayName("Should throw InvalidFormatException for names with invalid characters")
            void should_throwInvalidFormatException_when_nameContainsInvalidCharacters(String invalidName) {
                // Given
                String csvLine = "1," + invalidName + ",john@example.com,1990-05-15,75000.50";
                int lineNumber = 1;

                // When & Then
                assertThatThrownBy(() -> parser.parsePerson(csvLine, lineNumber))
                    .isInstanceOf(DataParsingException.InvalidFormatException.class)
                    .hasMessageContaining("Name contains invalid characters");
            }
        }

        @Nested
        @DisplayName("Email Field Tests")
        class EmailFieldTests {

            @ParameterizedTest(name = "{index} Invalid email: '{0}'")
            @ValueSource(strings = {"", "   "})
            @DisplayName("Should throw MissingDataException for empty emails")
            void should_throwMissingDataException_when_emailIsEmpty(String emptyEmail) {
                // Given
                String csvLine = "1,John Doe," + emptyEmail + ",1990-05-15,75000.50";
                int lineNumber = 1;

                // When & Then
                assertThatThrownBy(() -> parser.parsePerson(csvLine, lineNumber))
                    .isInstanceOf(DataParsingException.MissingDataException.class)
                    .hasMessageContaining("Email cannot be empty");
            }

            @ParameterizedTest(name = "{index} Invalid email format: '{0}'")
            @ValueSource(strings = {
                "invalid-email",
                "user@",
                "@example.com",
                "user.example.com",
                "user@.com",
                "user@com",
                "user@@example.com"
            })
            @DisplayName("Should throw InvalidFormatException for invalid email formats")
            void should_throwInvalidFormatException_when_emailFormatIsInvalid(String invalidEmail) {
                // Given
                String csvLine = "1,John Doe," + invalidEmail + ",1990-05-15,75000.50";
                int lineNumber = 1;

                // When & Then
                assertThatThrownBy(() -> parser.parsePerson(csvLine, lineNumber))
                    .isInstanceOf(DataParsingException.InvalidFormatException.class)
                    .hasMessageContaining("Invalid email format");
            }

            @Test
            @DisplayName("Should throw DataConstraintException when email exceeds maximum length")
            void should_throwDataConstraintException_when_emailExceedsMaxLength() {
                // Given
                String longEmail = "a".repeat(245) + "@example.com"; // Exceeds MAX_STRING_LENGTH (255)
                String csvLine = "1,John Doe," + longEmail + ",1990-05-15,75000.50";
                int lineNumber = 1;

                // When & Then
                assertThatThrownBy(() -> parser.parsePerson(csvLine, lineNumber))
                    .isInstanceOf(DataParsingException.DataConstraintException.class)
                    .hasMessageContaining("Email exceeds maximum length");
            }
        }

        @Nested
        @DisplayName("Birth Date Field Tests")
        class BirthDateFieldTests {

            @ParameterizedTest(name = "{index} Empty date: '{0}'")
            @ValueSource(strings = {"", "   "})
            @DisplayName("Should throw MissingDataException for empty birth dates")
            void should_throwMissingDataException_when_birthDateIsEmpty(String emptyDate) {
                // Given
                String csvLine = "1,John Doe,john@example.com," + emptyDate + ",75000.50";
                int lineNumber = 1;

                // When & Then
                assertThatThrownBy(() -> parser.parsePerson(csvLine, lineNumber))
                    .isInstanceOf(DataParsingException.MissingDataException.class)
                    .hasMessageContaining("Birth date cannot be empty");
            }

            @ParameterizedTest(name = "{index} Invalid date format: '{0}'")
            @ValueSource(strings = {
                "15-05-1990",
                "1990/05/15",
                "15.05.1990",
                "invalid-date"
            })
            @DisplayName("Should throw InvalidFormatException for invalid date formats")
            void should_throwInvalidFormatException_when_dateFormatIsInvalid(String invalidDate) {
                // Given
                String csvLine = "1,John Doe,john@example.com," + invalidDate + ",75000.50";
                int lineNumber = 1;

                // When & Then
                assertThatThrownBy(() -> parser.parsePerson(csvLine, lineNumber))
                    .isInstanceOf(DataParsingException.InvalidFormatException.class)
                    .hasMessageContaining("Invalid date format, expected yyyy-MM-dd");
            }

            @Test
            @DisplayName("Should throw InvalidFormatException for date with comma")
            void should_throwInvalidFormatException_when_dateHasComma() {
                // Given - escape comma by using quotes
                String csvLine = "1,John Doe,john@example.com,\"May 15, 1990\",75000.50";
                int lineNumber = 1;

                // When & Then
                assertThatThrownBy(() -> parser.parsePerson(csvLine, lineNumber))
                    .isInstanceOf(DataParsingException.InvalidFormatException.class);
            }

            @Test
            @DisplayName("Should throw DataConstraintException when birth date is too old")
            void should_throwDataConstraintException_when_birthDateIsTooOld() {
                // Given
                String csvLine = "1,John Doe,john@example.com,1899-12-31,75000.50";
                int lineNumber = 1;

                // When & Then
                assertThatThrownBy(() -> parser.parsePerson(csvLine, lineNumber))
                    .isInstanceOf(DataParsingException.DataConstraintException.class)
                    .hasMessageContaining("Birth date is too old");
            }

            @Test
            @DisplayName("Should throw DataConstraintException when person is too young")
            void should_throwDataConstraintException_when_personIsTooYoung() {
                // Given
                LocalDate futureDate = LocalDate.now().minusYears(10); // Less than 16 years old
                String csvLine = "1,John Doe,john@example.com," + futureDate + ",75000.50";
                int lineNumber = 1;

                // When & Then
                assertThatThrownBy(() -> parser.parsePerson(csvLine, lineNumber))
                    .isInstanceOf(DataParsingException.DataConstraintException.class)
                    .hasMessageContaining("Birth date indicates person is too young");
            }
        }

        @Nested
        @DisplayName("Salary Field Tests")
        class SalaryFieldTests {

            @ParameterizedTest(name = "{index} Empty salary: '{0}'")
            @ValueSource(strings = {"", "   "})
            @DisplayName("Should throw MissingDataException for empty salaries")
            void should_throwMissingDataException_when_salaryIsEmpty(String emptySalary) {
                // Given
                String csvLine = "1,John Doe,john@example.com,1990-05-15," + emptySalary;
                int lineNumber = 1;

                // When & Then
                assertThatThrownBy(() -> parser.parsePerson(csvLine, lineNumber))
                    .isInstanceOf(DataParsingException.MissingDataException.class)
                    .hasMessageContaining("Salary cannot be empty");
            }

            @ParameterizedTest(name = "{index} Invalid salary format: '{0}'")
            @ValueSource(strings = {"abc", "$75000"})
            @DisplayName("Should throw InvalidFormatException for invalid salary formats")
            void should_throwInvalidFormatException_when_salaryFormatIsInvalid(String invalidSalary) {
                // Given
                String csvLine = "1,John Doe,john@example.com,1990-05-15," + invalidSalary;
                int lineNumber = 1;

                // When & Then
                assertThatThrownBy(() -> parser.parsePerson(csvLine, lineNumber))
                    .isInstanceOf(DataParsingException.InvalidFormatException.class)
                    .hasMessageContaining("Invalid salary format");
            }

            @Test
            @DisplayName("Should throw InvalidFormatException for salary with comma separators")
            void should_throwInvalidFormatException_when_salaryHasCommas() {
                // Given - escape comma by using quotes or different approach
                String csvLine = "1,John Doe,john@example.com,1990-05-15,\"75,000.50\"";
                int lineNumber = 1;

                // When & Then
                assertThatThrownBy(() -> parser.parsePerson(csvLine, lineNumber))
                    .isInstanceOf(DataParsingException.InvalidFormatException.class);
            }

            @Test
            @DisplayName("Should throw DataConstraintException for negative salary")
            void should_throwDataConstraintException_when_salaryIsNegative() {
                // Given
                String csvLine = "1,John Doe,john@example.com,1990-05-15,-1000.00";
                int lineNumber = 1;

                // When & Then
                assertThatThrownBy(() -> parser.parsePerson(csvLine, lineNumber))
                    .isInstanceOf(DataParsingException.DataConstraintException.class)
                    .hasMessageContaining("Salary cannot be negative");
            }

            @Test
            @DisplayName("Should throw DataConstraintException when salary exceeds maximum")
            void should_throwDataConstraintException_when_salaryExceedsMaximum() {
                // Given
                String csvLine = "1,John Doe,john@example.com,1990-05-15,1000001.00";
                int lineNumber = 1;

                // When & Then
                assertThatThrownBy(() -> parser.parsePerson(csvLine, lineNumber))
                    .isInstanceOf(DataParsingException.DataConstraintException.class)
                    .hasMessageContaining("Salary exceeds maximum allowed");
            }

            @Test
            @DisplayName("Should throw InvalidFormatException when salary has too many decimal places")
            void should_throwInvalidFormatException_when_salaryHasTooManyDecimalPlaces() {
                // Given
                String csvLine = "1,John Doe,john@example.com,1990-05-15,75000.123";
                int lineNumber = 1;

                // When & Then
                assertThatThrownBy(() -> parser.parsePerson(csvLine, lineNumber))
                    .isInstanceOf(DataParsingException.InvalidFormatException.class)
                    .hasMessageContaining("Salary cannot have more than 2 decimal places");
            }
        }
    }

    @Nested
    @DisplayName("parsePersons Batch Processing Tests")
    class ParsePersonsBatchTests {

        @Test
        @DisplayName("Should throw IllegalArgumentException when CSV lines list is null")
        void should_throwIllegalArgumentException_when_csvLinesIsNull() {
            // Given
            List<String> csvLines = null;
            boolean skipErrors = false;

            // When & Then
            assertThatThrownBy(() -> parser.parsePersons(csvLines, skipErrors))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("CSV lines list cannot be null");
        }

        @Test
        @DisplayName("Should return empty result when CSV lines list is empty")
        void should_returnEmptyResult_when_csvLinesIsEmpty() {
            // Given
            List<String> csvLines = Collections.emptyList();
            boolean skipErrors = false;

            // When
            CsvDataParser.ParseResult result = parser.parsePersons(csvLines, skipErrors);

            // Then
            assertThat(result.getSuccessfulParsing()).isEmpty();
            assertThat(result.getErrors()).isEmpty();
            assertThat(result.hasErrors()).isFalse();
            assertThat(result.getSuccessCount()).isZero();
            assertThat(result.getErrorCount()).isZero();
        }

        @Test
        @DisplayName("Should parse all valid lines successfully")
        void should_parseAllLines_when_allLinesAreValid() {
            // Given
            List<String> csvLines = Arrays.asList(
                "1,John Doe,john@example.com,1990-05-15,75000.50",
                "2,Jane Smith,jane@example.com,1985-12-25,60000.00",
                "3,Bob Johnson,bob@example.com,1992-03-10,80000.75"
            );
            boolean skipErrors = false;

            // When
            CsvDataParser.ParseResult result = parser.parsePersons(csvLines, skipErrors);

            // Then
            assertThat(result.getSuccessfulParsing()).hasSize(3);
            assertThat(result.getErrors()).isEmpty();
            assertThat(result.hasErrors()).isFalse();
            assertThat(result.getSuccessCount()).isEqualTo(3);
            assertThat(result.getErrorCount()).isZero();

            // Verify first person
            CsvDataParser.Person firstPerson = result.getSuccessfulParsing().get(0);
            assertThat(firstPerson.getId()).isEqualTo(1L);
            assertThat(firstPerson.getName()).isEqualTo("John Doe");
        }

        @Test
        @DisplayName("Should stop at first error when skipErrors is false")
        void should_stopAtFirstError_when_skipErrorsIsFalse() {
            // Given
            List<String> csvLines = Arrays.asList(
                "1,John Doe,john@example.com,1990-05-15,75000.50",
                "invalid-line",
                "3,Bob Johnson,bob@example.com,1992-03-10,80000.75"
            );
            boolean skipErrors = false;

            // When
            CsvDataParser.ParseResult result = parser.parsePersons(csvLines, skipErrors);

            // Then
            assertThat(result.getSuccessfulParsing()).hasSize(1); // Only first line processed
            assertThat(result.getErrors()).hasSize(1);
            assertThat(result.hasErrors()).isTrue();
            assertThat(result.getSuccessCount()).isEqualTo(1);
            assertThat(result.getErrorCount()).isEqualTo(1);

            // Verify error details
            CsvDataParser.ParseError error = result.getErrors().get(0);
            assertThat(error.getLineNumber()).isEqualTo(2);
            assertThat(error.getOriginalLine()).isEqualTo("invalid-line");
        }

        @Test
        @DisplayName("Should continue processing when skipErrors is true")
        void should_continueProcessing_when_skipErrorsIsTrue() {
            // Given
            List<String> csvLines = Arrays.asList(
                "1,John Doe,john@example.com,1990-05-15,75000.50",
                "invalid-line",
                "3,Bob Johnson,bob@example.com,1992-03-10,80000.75",
                "",
                "5,Alice Brown,alice@example.com,1988-07-20,65000.00"
            );
            boolean skipErrors = true;

            // When
            CsvDataParser.ParseResult result = parser.parsePersons(csvLines, skipErrors);

            // Then
            assertThat(result.getSuccessfulParsing()).hasSize(3); // Lines 1, 3, and 5
            assertThat(result.getErrors()).hasSize(2); // Lines 2 and 4
            assertThat(result.hasErrors()).isTrue();
            assertThat(result.getSuccessCount()).isEqualTo(3);
            assertThat(result.getErrorCount()).isEqualTo(2);

            // Verify successful parsing includes correct persons
            assertThat(result.getSuccessfulParsing())
                .extracting(CsvDataParser.Person::getId)
                .containsExactly(1L, 3L, 5L);
        }
    }

    @Nested
    @DisplayName("Person Inner Class Tests")
    class PersonInnerClassTests {

        @Test
        @DisplayName("Should create Person with all valid fields")
        void should_createPerson_when_allFieldsAreValid() {
            // Given
            Long id = 1L;
            String name = "John Doe";
            String email = "john@example.com";
            LocalDate birthDate = LocalDate.of(1990, 5, 15);
            BigDecimal salary = new BigDecimal("75000.50");

            // When
            CsvDataParser.Person person = new CsvDataParser.Person(id, name, email, birthDate, salary);

            // Then
            assertThat(person.getId()).isEqualTo(id);
            assertThat(person.getName()).isEqualTo(name);
            assertThat(person.getEmail()).isEqualTo(email);
            assertThat(person.getBirthDate()).isEqualTo(birthDate);
            assertThat(person.getSalary()).isEqualTo(salary);
        }

        @Test
        @DisplayName("Should implement equals correctly based on ID")
        void should_implementEqualsCorrectly_when_comparingPersons() {
            // Given
            CsvDataParser.Person person1 = new CsvDataParser.Person(1L, "John Doe", "john@example.com",
                LocalDate.of(1990, 5, 15), new BigDecimal("75000.50"));
            CsvDataParser.Person person2 = new CsvDataParser.Person(1L, "Jane Smith", "jane@example.com",
                LocalDate.of(1985, 12, 25), new BigDecimal("60000.00"));
            CsvDataParser.Person person3 = new CsvDataParser.Person(2L, "John Doe", "john@example.com",
                LocalDate.of(1990, 5, 15), new BigDecimal("75000.50"));

            // When & Then
            assertThat(person1)
                .isEqualTo(person2) // Same ID
                .isNotEqualTo(person3) // Different ID
                .isEqualTo(person1) // Reflexive
                .isNotEqualTo(null) // Null check
                .isNotEqualTo("not a person"); // Different type
        }

        @Test
        @DisplayName("Should implement hashCode consistently with equals")
        void should_implementHashCodeConsistently_when_objectsAreEqual() {
            // Given
            CsvDataParser.Person person1 = new CsvDataParser.Person(1L, "John Doe", "john@example.com",
                LocalDate.of(1990, 5, 15), new BigDecimal("75000.50"));
            CsvDataParser.Person person2 = new CsvDataParser.Person(1L, "Jane Smith", "jane@example.com",
                LocalDate.of(1985, 12, 25), new BigDecimal("60000.00"));

            // When & Then
            assertThat(person1.hashCode()).isEqualTo(person2.hashCode());
        }

        @Test
        @DisplayName("Should generate meaningful toString representation")
        void should_generateMeaningfulToString_when_callingToString() {
            // Given
            CsvDataParser.Person person = new CsvDataParser.Person(1L, "John Doe", "john@example.com",
                LocalDate.of(1990, 5, 15), new BigDecimal("75000.50"));

            // When
            String result = person.toString();

            // Then
            assertThat(result)
                .contains("Person{")
                .contains("id=1")
                .contains("name='John Doe'")
                .contains("email='john@example.com'")
                .contains("birthDate=1990-05-15")
                .contains("salary=75000.50");
        }
    }

    @Nested
    @DisplayName("ParseError Inner Class Tests")
    class ParseErrorInnerClassTests {

        @Test
        @DisplayName("Should create ParseError with all required fields")
        void should_createParseError_when_allFieldsAreProvided() {
            // Given
            int lineNumber = 5;
            String originalLine = "invalid,line,data";
            DataParsingException exception = new DataParsingException.InvalidFormatException(
                "Test error", lineNumber, "testField");

            // When
            CsvDataParser.ParseError parseError = new CsvDataParser.ParseError(lineNumber, originalLine, exception);

            // Then
            assertThat(parseError.getLineNumber()).isEqualTo(lineNumber);
            assertThat(parseError.getOriginalLine()).isEqualTo(originalLine);
            assertThat(parseError.getException()).isEqualTo(exception);
        }

        @Test
        @DisplayName("Should generate meaningful toString representation")
        void should_generateMeaningfulToString_when_callingToString() {
            // Given
            int lineNumber = 5;
            String originalLine = "invalid,line,data";
            DataParsingException exception = new DataParsingException.InvalidFormatException(
                "Test error", lineNumber, "testField");
            CsvDataParser.ParseError parseError = new CsvDataParser.ParseError(lineNumber, originalLine, exception);

            // When
            String result = parseError.toString();

            // Then
            assertThat(result)
                .contains("ParseError{")
                .contains("line=5")
                .contains("error='Test error");
        }
    }

    @Nested
    @DisplayName("ParseResult Inner Class Tests")
    class ParseResultInnerClassTests {

        @Test
        @DisplayName("Should create ParseResult with successful parsing and no errors")
        void should_createParseResult_when_onlySuccessfulParsing() {
            // Given
            List<CsvDataParser.Person> successfulParsing = Arrays.asList(
                new CsvDataParser.Person(1L, "John Doe", "john@example.com",
                    LocalDate.of(1990, 5, 15), new BigDecimal("75000.50"))
            );
            List<CsvDataParser.ParseError> errors = Collections.emptyList();

            // When
            CsvDataParser.ParseResult result = new CsvDataParser.ParseResult(successfulParsing, errors);

            // Then
            assertThat(result.getSuccessfulParsing()).hasSize(1);
            assertThat(result.getErrors()).isEmpty();
            assertThat(result.hasErrors()).isFalse();
            assertThat(result.getSuccessCount()).isEqualTo(1);
            assertThat(result.getErrorCount()).isZero();
        }

        @Test
        @DisplayName("Should create ParseResult with errors and no successful parsing")
        void should_createParseResult_when_onlyErrors() {
            // Given
            List<CsvDataParser.Person> successfulParsing = Collections.emptyList();
            List<CsvDataParser.ParseError> errors = Arrays.asList(
                new CsvDataParser.ParseError(1, "invalid,line",
                    new DataParsingException.InvalidFormatException("Test error", 1, "field"))
            );

            // When
            CsvDataParser.ParseResult result = new CsvDataParser.ParseResult(successfulParsing, errors);

            // Then
            assertThat(result.getSuccessfulParsing()).isEmpty();
            assertThat(result.getErrors()).hasSize(1);
            assertThat(result.hasErrors()).isTrue();
            assertThat(result.getSuccessCount()).isZero();
            assertThat(result.getErrorCount()).isEqualTo(1);
        }

        @Test
        @DisplayName("Should create immutable collections")
        void should_createImmutableCollections_when_creatingParseResult() {
            // Given
            List<CsvDataParser.Person> successfulParsing = new java.util.ArrayList<>();
            successfulParsing.add(new CsvDataParser.Person(1L, "John Doe", "john@example.com",
                LocalDate.of(1990, 5, 15), new BigDecimal("75000.50")));

            List<CsvDataParser.ParseError> errors = new java.util.ArrayList<>();
            errors.add(new CsvDataParser.ParseError(2, "invalid,line",
                new DataParsingException.InvalidFormatException("Test error", 2, "field")));

            // When
            CsvDataParser.ParseResult result = new CsvDataParser.ParseResult(successfulParsing, errors);

            // Then - Should not affect the result when original lists are modified
            successfulParsing.clear();
            errors.clear();

            assertThat(result.getSuccessfulParsing()).hasSize(1);
            assertThat(result.getErrors()).hasSize(1);

            // Then - Returned collections should be immutable
            assertThatThrownBy(() -> result.getSuccessfulParsing().clear())
                .isInstanceOf(UnsupportedOperationException.class);
            assertThatThrownBy(() -> result.getErrors().clear())
                .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        @DisplayName("Should generate meaningful toString representation")
        void should_generateMeaningfulToString_when_callingToString() {
            // Given
            List<CsvDataParser.Person> successfulParsing = Arrays.asList(
                new CsvDataParser.Person(1L, "John Doe", "john@example.com",
                    LocalDate.of(1990, 5, 15), new BigDecimal("75000.50"))
            );
            List<CsvDataParser.ParseError> errors = Arrays.asList(
                new CsvDataParser.ParseError(2, "invalid,line",
                    new DataParsingException.InvalidFormatException("Test error", 2, "field"))
            );
            CsvDataParser.ParseResult result = new CsvDataParser.ParseResult(successfulParsing, errors);

            // When
            String resultString = result.toString();

            // Then
            assertThat(resultString)
                .contains("ParseResult{")
                .contains("success=1")
                .contains("errors=1");
        }
    }

    @Nested
    @DisplayName("Boundary Condition Tests (CORRECT)")
    class BoundaryConditionTests {

        @Nested
        @DisplayName("Range Tests")
        class RangeTests {

            @Test
            @DisplayName("Should accept minimum valid age (exactly 16 years old)")
            void should_acceptMinimumValidAge_when_personIsExactly16YearsOld() throws DataParsingException {
                // Given
                LocalDate exactlyMinAge = LocalDate.now().minusYears(16);
                String csvLine = "1,John Doe,john@example.com," + exactlyMinAge + ",75000.50";
                int lineNumber = 1;

                // When
                CsvDataParser.Person result = parser.parsePerson(csvLine, lineNumber);

                // Then
                assertThat(result.getBirthDate()).isEqualTo(exactlyMinAge);
            }

            @Test
            @DisplayName("Should accept minimum valid date (1900-01-01)")
            void should_acceptMinimumValidDate_when_dateIsExactly1900() throws DataParsingException {
                // Given
                String csvLine = "1,John Doe,john@example.com,1900-01-01,75000.50";
                int lineNumber = 1;

                // When
                CsvDataParser.Person result = parser.parsePerson(csvLine, lineNumber);

                // Then
                assertThat(result.getBirthDate()).isEqualTo(LocalDate.of(1900, 1, 1));
            }

            @Test
            @DisplayName("Should accept maximum salary boundary")
            void should_acceptMaximumSalary_when_salaryIsExactlyMaximum() throws DataParsingException {
                // Given
                String csvLine = "1,John Doe,john@example.com,1990-05-15,1000000.00";
                int lineNumber = 1;

                // When
                CsvDataParser.Person result = parser.parsePerson(csvLine, lineNumber);

                // Then
                assertThat(result.getSalary()).isEqualTo(new BigDecimal("1000000.00"));
            }

            @Test
            @DisplayName("Should accept zero salary")
            void should_acceptZeroSalary_when_salaryIsZero() throws DataParsingException {
                // Given
                String csvLine = "1,John Doe,john@example.com,1990-05-15,0.00";
                int lineNumber = 1;

                // When
                CsvDataParser.Person result = parser.parsePerson(csvLine, lineNumber);

                // Then
                assertThat(result.getSalary()).isEqualTo(new BigDecimal("0.00"));
            }
        }

        @Nested
        @DisplayName("Cardinality Tests")
        class CardinalityTests {

            @Test
            @DisplayName("Should handle exactly 5 fields")
            void should_handleExactly5Fields_when_csvHasCorrectFieldCount() throws DataParsingException {
                // Given
                String csvLine = "1,John Doe,john@example.com,1990-05-15,75000.50";
                int lineNumber = 1;

                // When
                CsvDataParser.Person result = parser.parsePerson(csvLine, lineNumber);

                // Then
                assertThat(result).isNotNull();
            }

            @Test
            @DisplayName("Should handle empty fields correctly")
            void should_handleEmptyFields_when_fieldsAreEmpty() {
                // Given
                String csvLine = ",,,,";
                int lineNumber = 1;

                // When & Then
                assertThatThrownBy(() -> parser.parsePerson(csvLine, lineNumber))
                    .isInstanceOf(DataParsingException.MissingDataException.class);
            }
        }

        @Nested
        @DisplayName("Conformance Tests")
        class ConformanceTests {

            @Test
            @DisplayName("Should conform to email pattern requirements")
            void should_conformToEmailPattern_when_emailMatchesRegex() throws DataParsingException {
                // Given
                String csvLine = "1,John Doe,valid.email+tag@sub.domain.co.uk,1990-05-15,75000.50";
                int lineNumber = 1;

                // When
                CsvDataParser.Person result = parser.parsePerson(csvLine, lineNumber);

                // Then
                assertThat(result.getEmail()).isEqualTo("valid.email+tag@sub.domain.co.uk");
            }

            @Test
            @DisplayName("Should conform to date format requirements")
            void should_conformToDateFormat_when_dateMatchesYyyyMmDd() throws DataParsingException {
                // Given
                String csvLine = "1,John Doe,john@example.com,2000-12-31,75000.50";
                int lineNumber = 1;

                // When
                CsvDataParser.Person result = parser.parsePerson(csvLine, lineNumber);

                // Then
                assertThat(result.getBirthDate()).isEqualTo(LocalDate.of(2000, 12, 31));
            }
        }

        @Nested
        @DisplayName("Existence Tests")
        class ExistenceTests {

            @Test
            @DisplayName("Should handle existing valid data")
            void should_handleExistingValidData_when_allFieldsExist() throws DataParsingException {
                // Given
                String csvLine = "1,John Doe,john@example.com,1990-05-15,75000.50";
                int lineNumber = 1;

                // When
                CsvDataParser.Person result = parser.parsePerson(csvLine, lineNumber);

                // Then
                assertThat(result.getId()).isNotNull();
                assertThat(result.getName()).isNotNull();
                assertThat(result.getEmail()).isNotNull();
                assertThat(result.getBirthDate()).isNotNull();
                assertThat(result.getSalary()).isNotNull();
            }
        }
    }
}
