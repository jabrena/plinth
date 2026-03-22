package info.jab.ms.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Global exception handler for the God Analysis API.
 * Provides consistent error responses and logging across all controllers.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handle missing required request parameters.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingParameter(MissingServletRequestParameterException ex) {
        var requestId = MDC.get("requestId");

        logger.warn("Missing required parameter - requestId: {}, parameter: {}, type: {}",
            requestId, ex.getParameterName(), ex.getParameterType());

        var errorResponse = createErrorResponse(
            HttpStatus.BAD_REQUEST,
            "Missing required parameter: " + ex.getParameterName(),
            requestId
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handle method argument type mismatch (e.g., invalid parameter types).
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        var requestId = MDC.get("requestId");

        logger.warn("Invalid parameter type - requestId: {}, parameter: {}, value: {}, expectedType: {}",
            requestId, ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

        var errorResponse = createErrorResponse(
            HttpStatus.BAD_REQUEST,
            String.format("Invalid value '%s' for parameter '%s'", ex.getValue(), ex.getName()),
            requestId
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handle validation exceptions for business logic.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleValidationError(IllegalArgumentException ex) {
        var requestId = MDC.get("requestId");

        logger.warn("Validation error - requestId: {}, error: {}", requestId, ex.getMessage());

        var errorResponse = createErrorResponse(
            HttpStatus.BAD_REQUEST,
            ex.getMessage(),
            requestId
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handle unexpected server errors.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericError(Exception ex) {
        var requestId = MDC.get("requestId");

        logger.error("Unexpected error - requestId: {}, error: {}", requestId, ex.getMessage(), ex);

        var errorResponse = createErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "An unexpected error occurred",
            requestId
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * Create a consistent error response structure.
     */
    private Map<String, Object> createErrorResponse(HttpStatus status, String message, String requestId) {
        var errorResponse = new LinkedHashMap<String, Object>();
        errorResponse.put("timestamp", Instant.now().toString());
        errorResponse.put("status", status.value());
        errorResponse.put("error", status.getReasonPhrase());
        errorResponse.put("message", message);
        Optional.ofNullable(requestId).ifPresent(id -> errorResponse.put("requestId", id));
        return errorResponse;
    }
}