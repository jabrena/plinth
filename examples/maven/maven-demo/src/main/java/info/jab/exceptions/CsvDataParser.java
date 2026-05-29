package info.jab.exceptions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * CSV data parser demonstrating comprehensive exception handling patterns.
 *
 * This class showcases:
 * - Custom exception hierarchy usage
 * - Exception chaining to preserve context
 * - Input validation with specific error types
 * - Graceful error recovery strategies
 * - Detailed error context preservation
 * - Security-conscious error handling
 */
public class CsvDataParser {

    private static final Logger LOGGER = Logger.getLogger(CsvDataParser.class.getName());
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int MAX_STRING_LENGTH = 255;
    private static final BigDecimal MAX_AMOUNT = new BigDecimal("1000000.00");

    /**
     * Parses a CSV line into a Person record.
     *
     * Expected format: id,name,email,birthDate,salary
     * Example: 1,John Doe,john@example.com,1990-05-15,75000.50
     *
     * @param csvLine the CSV line to parse (must not be null)
     * @param lineNumber the line number for error reporting
     * @return parsed Person object
     * @throws DataParsingException if parsing fails
     * @throws IllegalArgumentException if csvLine is null
     */
    public Person parsePerson(String csvLine, int lineNumber) throws DataParsingException {
        if (csvLine == null) {
            throw new IllegalArgumentException("CSV line cannot be null");
        }

        if (csvLine.trim().isEmpty()) {
            throw new DataParsingException.MissingDataException(
                "CSV line is empty", lineNumber, null);
        }

        String[] fields = csvLine.split(",", -1); // -1 to include empty trailing fields

        if (fields.length != 5) {
            throw new DataParsingException.InvalidFormatException(
                String.format("Expected 5 fields but found %d", fields.length),
                lineNumber, null);
        }

        try {
            Long id = parseId(fields[0], lineNumber, "id");
            String name = parseName(fields[1], lineNumber, "name");
            String email = parseEmail(fields[2], lineNumber, "email");
            LocalDate birthDate = parseBirthDate(fields[3], lineNumber, "birthDate");
            BigDecimal salary = parseSalary(fields[4], lineNumber, "salary");

            return new Person(id, name, email, birthDate, salary);

        } catch (DataParsingException e) {
            // Re-throw parsing exceptions as-is (they already have context)
            throw e;
        } catch (Exception e) {
            // Wrap unexpected exceptions with context
            String message = "Unexpected error parsing CSV line";
            LOGGER.log(Level.SEVERE, message + " at line " + lineNumber, e);
            throw new DataParsingException(message, e, lineNumber, null);
        }
    }

    /**
     * Parses multiple CSV lines with error recovery.
     *
     * @param csvLines the lines to parse
     * @param skipErrors whether to skip lines with errors and continue parsing
     * @return parsing results with success/error information for each line
     */
    public ParseResult parsePersons(List<String> csvLines, boolean skipErrors) {
        if (csvLines == null) {
            throw new IllegalArgumentException("CSV lines list cannot be null");
        }

        List<Person> successfulParsing = new ArrayList<>();
        List<ParseError> errors = new ArrayList<>();

        for (int i = 0; i < csvLines.size(); i++) {
            int lineNumber = i + 1;
            String line = csvLines.get(i);

            try {
                Person person = parsePerson(line, lineNumber);
                successfulParsing.add(person);

            } catch (DataParsingException e) {
                ParseError error = new ParseError(lineNumber, line, e);
                errors.add(error);

                LOGGER.log(Level.WARNING, "Failed to parse line " + lineNumber + ": " + e.getMessage());

                if (!skipErrors) {
                    // Stop processing and return partial results
                    break;
                }
            } catch (Exception e) {
                // Handle unexpected exceptions
                ParseError error = new ParseError(lineNumber, line,
                    new DataParsingException("Unexpected parsing error", e, lineNumber, null));
                errors.add(error);

                LOGGER.log(Level.SEVERE, "Unexpected error parsing line " + lineNumber, e);

                if (!skipErrors) {
                    break;
                }
            }
        }

        return new ParseResult(successfulParsing, errors);
    }

    /**
     * Parses and validates ID field.
     */
    private Long parseId(String value, int lineNumber, String fieldName) throws DataParsingException {
        if (value == null || value.trim().isEmpty()) {
            throw new DataParsingException.MissingDataException(
                "ID cannot be empty", lineNumber, fieldName);
        }

        try {
            long id = Long.parseLong(value.trim());
            if (id <= 0) {
                throw new DataParsingException.DataConstraintException(
                    "ID must be positive", lineNumber, fieldName, id, "> 0");
            }
            return id;
        } catch (NumberFormatException e) {
            throw new DataParsingException.InvalidFormatException(
                "Invalid ID format: " + sanitizeForLogging(value), e, lineNumber, fieldName);
        }
    }

    /**
     * Parses and validates name field.
     */
    private String parseName(String value, int lineNumber, String fieldName) throws DataParsingException {
        if (value == null || value.trim().isEmpty()) {
            throw new DataParsingException.MissingDataException(
                "Name cannot be empty", lineNumber, fieldName);
        }

        String trimmedName = value.trim();

        if (trimmedName.length() > MAX_STRING_LENGTH) {
            throw new DataParsingException.DataConstraintException(
                "Name exceeds maximum length", lineNumber, fieldName,
                trimmedName.length(), MAX_STRING_LENGTH);
        }

        // Basic validation for suspicious characters
        if (trimmedName.matches(".*[<>\"'&].*")) {
            throw new DataParsingException.InvalidFormatException(
                "Name contains invalid characters", lineNumber, fieldName);
        }

        return trimmedName;
    }

    /**
     * Parses and validates email field.
     */
    private String parseEmail(String value, int lineNumber, String fieldName) throws DataParsingException {
        if (value == null || value.trim().isEmpty()) {
            throw new DataParsingException.MissingDataException(
                "Email cannot be empty", lineNumber, fieldName);
        }

        String trimmedEmail = value.trim().toLowerCase();

        if (!EMAIL_PATTERN.matcher(trimmedEmail).matches()) {
            throw new DataParsingException.InvalidFormatException(
                "Invalid email format", lineNumber, fieldName);
        }

        if (trimmedEmail.length() > MAX_STRING_LENGTH) {
            throw new DataParsingException.DataConstraintException(
                "Email exceeds maximum length", lineNumber, fieldName,
                trimmedEmail.length(), MAX_STRING_LENGTH);
        }

        return trimmedEmail;
    }

    /**
     * Parses and validates birth date field.
     */
    private LocalDate parseBirthDate(String value, int lineNumber, String fieldName) throws DataParsingException {
        if (value == null || value.trim().isEmpty()) {
            throw new DataParsingException.MissingDataException(
                "Birth date cannot be empty", lineNumber, fieldName);
        }

        try {
            LocalDate birthDate = LocalDate.parse(value.trim(), DATE_FORMATTER);

            // Validate reasonable date range
            LocalDate minDate = LocalDate.of(1900, 1, 1);
            LocalDate maxDate = LocalDate.now().minusYears(16); // Minimum age requirement

            if (birthDate.isBefore(minDate)) {
                throw new DataParsingException.DataConstraintException(
                    "Birth date is too old", lineNumber, fieldName, birthDate, ">= " + minDate);
            }

            if (birthDate.isAfter(maxDate)) {
                throw new DataParsingException.DataConstraintException(
                    "Birth date indicates person is too young", lineNumber, fieldName,
                    birthDate, "<= " + maxDate);
            }

            return birthDate;

        } catch (DateTimeParseException e) {
            throw new DataParsingException.InvalidFormatException(
                "Invalid date format, expected yyyy-MM-dd", e, lineNumber, fieldName);
        }
    }

    /**
     * Parses and validates salary field.
     */
    private BigDecimal parseSalary(String value, int lineNumber, String fieldName) throws DataParsingException {
        if (value == null || value.trim().isEmpty()) {
            throw new DataParsingException.MissingDataException(
                "Salary cannot be empty", lineNumber, fieldName);
        }

        try {
            BigDecimal salary = new BigDecimal(value.trim());

            if (salary.compareTo(BigDecimal.ZERO) < 0) {
                throw new DataParsingException.DataConstraintException(
                    "Salary cannot be negative", lineNumber, fieldName, salary, ">= 0");
            }

            if (salary.compareTo(MAX_AMOUNT) > 0) {
                throw new DataParsingException.DataConstraintException(
                    "Salary exceeds maximum allowed", lineNumber, fieldName, salary, "<= " + MAX_AMOUNT);
            }

            // Check decimal places
            if (salary.scale() > 2) {
                throw new DataParsingException.InvalidFormatException(
                    "Salary cannot have more than 2 decimal places", lineNumber, fieldName);
            }

            return salary;

        } catch (NumberFormatException e) {
            throw new DataParsingException.InvalidFormatException(
                "Invalid salary format", e, lineNumber, fieldName);
        }
    }

    /**
     * Sanitizes values for logging to prevent log injection.
     */
    private String sanitizeForLogging(String value) {
        if (value == null) {
            return "null";
        }

        // Remove control characters and limit length
        String sanitized = value.replaceAll("[\r\n\t]", "_")
                               .replaceAll("[\\p{Cntrl}]", "_");

        return sanitized.length() > 50 ? sanitized.substring(0, 50) + "..." : sanitized;
    }

    /**
     * Data class representing a person.
     */
    public static class Person {
        private final Long id;
        private final String name;
        private final String email;
        private final LocalDate birthDate;
        private final BigDecimal salary;

        public Person(Long id, String name, String email, LocalDate birthDate, BigDecimal salary) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.birthDate = birthDate;
            this.salary = salary;
        }

        // Getters
        public Long getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public LocalDate getBirthDate() { return birthDate; }
        public BigDecimal getSalary() { return salary; }

        @Override
        public String toString() {
            return String.format("Person{id=%d, name='%s', email='%s', birthDate=%s, salary=%s}",
                id, name, email, birthDate, salary);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Person person = (Person) o;
            return Objects.equals(id, person.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }

    /**
     * Represents a parsing error with context.
     */
    public static class ParseError {
        private final int lineNumber;
        private final String originalLine;
        private final DataParsingException exception;

        public ParseError(int lineNumber, String originalLine, DataParsingException exception) {
            this.lineNumber = lineNumber;
            this.originalLine = originalLine;
            this.exception = exception;
        }

        public int getLineNumber() { return lineNumber; }
        public String getOriginalLine() { return originalLine; }
        public DataParsingException getException() { return exception; }

        @Override
        public String toString() {
            return String.format("ParseError{line=%d, error='%s'}", lineNumber, exception.getMessage());
        }
    }

    /**
     * Result of parsing operation with success and error information.
     */
    public static class ParseResult {
        private final List<Person> successfulParsing;
        private final List<ParseError> errors;

        public ParseResult(List<Person> successfulParsing, List<ParseError> errors) {
            this.successfulParsing = Collections.unmodifiableList(new ArrayList<>(successfulParsing));
            this.errors = Collections.unmodifiableList(new ArrayList<>(errors));
        }

        public List<Person> getSuccessfulParsing() { return successfulParsing; }
        public List<ParseError> getErrors() { return errors; }
        public boolean hasErrors() { return !errors.isEmpty(); }
        public int getSuccessCount() { return successfulParsing.size(); }
        public int getErrorCount() { return errors.size(); }

        @Override
        public String toString() {
            return String.format("ParseResult{success=%d, errors=%d}",
                getSuccessCount(), getErrorCount());
        }
    }
}
