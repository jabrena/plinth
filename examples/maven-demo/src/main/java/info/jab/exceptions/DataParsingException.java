package info.jab.exceptions;

/**
 * Base exception for data parsing operations.
 *
 * This exception hierarchy demonstrates:
 * - Proper exception inheritance structure
 * - Specific exception types for different parsing errors
 * - Context preservation through exception chaining
 */
public class DataParsingException extends Exception {

    private static final long serialVersionUID = 1L;
    private final int lineNumber;
    private final String fieldName;

    /**
     * Constructs a DataParsingException with context information.
     *
     * @param message the error message
     * @param lineNumber the line number where the error occurred (or -1 if unknown)
     * @param fieldName the field name that caused the error (or null if unknown)
     */
    public DataParsingException(String message, int lineNumber, String fieldName) {
        super(message);
        this.lineNumber = lineNumber;
        this.fieldName = fieldName;
    }

    /**
     * Constructs a DataParsingException with context information and a cause.
     *
     * @param message the error message
     * @param cause the underlying cause
     * @param lineNumber the line number where the error occurred (or -1 if unknown)
     * @param fieldName the field name that caused the error (or null if unknown)
     */
    public DataParsingException(String message, Throwable cause, int lineNumber, String fieldName) {
        super(message, cause);
        this.lineNumber = lineNumber;
        this.fieldName = fieldName;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getFieldName() {
        return fieldName;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder(super.getMessage());

        if (lineNumber > 0) {
            sb.append(" (line ").append(lineNumber).append(")");
        }

        if (fieldName != null && !fieldName.trim().isEmpty()) {
            sb.append(" (field: ").append(fieldName).append(")");
        }

        return sb.toString();
    }

    /**
     * Exception thrown when data format is invalid.
     */
    public static class InvalidFormatException extends DataParsingException {
        private static final long serialVersionUID = 1L;

        public InvalidFormatException(String message, int lineNumber, String fieldName) {
            super(message, lineNumber, fieldName);
        }

        public InvalidFormatException(String message, Throwable cause, int lineNumber, String fieldName) {
            super(message, cause, lineNumber, fieldName);
        }
    }

    /**
     * Exception thrown when required data is missing.
     */
    public static class MissingDataException extends DataParsingException {
        private static final long serialVersionUID = 1L;

        public MissingDataException(String message, int lineNumber, String fieldName) {
            super(message, lineNumber, fieldName);
        }

        public MissingDataException(String message, Throwable cause, int lineNumber, String fieldName) {
            super(message, cause, lineNumber, fieldName);
        }
    }

    /**
     * Exception thrown when data exceeds allowed limits.
     */
    public static class DataConstraintException extends DataParsingException {
        private static final long serialVersionUID = 1L;
        private final Object actualValue;
        private final Object constraintValue;

        public DataConstraintException(String message, int lineNumber, String fieldName,
                                     Object actualValue, Object constraintValue) {
            super(message, lineNumber, fieldName);
            this.actualValue = actualValue;
            this.constraintValue = constraintValue;
        }

        public Object getActualValue() {
            return actualValue;
        }

        public Object getConstraintValue() {
            return constraintValue;
        }

        @Override
        public String getMessage() {
            return super.getMessage() +
                String.format(" (actual: %s, constraint: %s)", actualValue, constraintValue);
        }
    }
}
