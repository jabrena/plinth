package info.jab.ms.controller;

import info.jab.ms.api.model.ErrorResponse;
import info.jab.ms.exception.BadRequestException;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestControllerAdvice
class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final Pattern UNEXPECTED_VALUE = Pattern.compile("Unexpected value '([^']+)'");

    @ExceptionHandler(BadRequestException.class)
    ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException exception) {
        log.warn("Mapped bad request: code={}, message={}", exception.getCode(), exception.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getCode(), exception.getMessage()));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    ResponseEntity<ErrorResponse> handleValidation(HandlerMethodValidationException exception) {
        var invalidSource = exception.getParameterValidationResults().stream()
            .map(ParameterValidationResult::getResolvableErrors)
            .flatMap(errors -> StreamSupport.stream(errors.spliterator(), false))
            .map(error -> error.getDefaultMessage())
            .filter(Objects::nonNull)
            .anyMatch(message -> message.contains("Unexpected value"));

        if (invalidSource) {
            var message = extractUnexpectedSource(exception);
            log.warn("Mapped source validation error: {}", message);
            return ResponseEntity.badRequest().body(new ErrorResponse("INVALID_SOURCE", message));
        }

        var message = "Query parameter 'filter' must contain exactly one character.";
        log.warn("Mapped filter validation error: {}", message);
        return ResponseEntity.badRequest().body(new ErrorResponse("INVALID_FILTER", message));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> handleUnexpected(Exception exception) {
        log.error("Mapped unexpected exception", exception);
        var error = new ErrorResponse("INTERNAL_ERROR", "Unexpected error while computing aggregate.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    private String extractUnexpectedSource(HandlerMethodValidationException exception) {
        var sourceFromMessage = exception.getParameterValidationResults().stream()
            .map(ParameterValidationResult::getResolvableErrors)
            .flatMap(errors -> StreamSupport.stream(errors.spliterator(), false))
            .map(error -> error.getDefaultMessage())
            .filter(Objects::nonNull)
            .map(UNEXPECTED_VALUE::matcher)
            .filter(matcher -> matcher.find())
            .map(matcher -> matcher.group(1))
            .findFirst();

        return sourceFromMessage
            .map(value -> "Query parameter 'sources' contains unsupported value: '" + value + "'.")
            .orElse("Query parameter 'sources' contains unsupported value: 'unknown'.");
    }
}
