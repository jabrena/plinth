package info.jab.ms.controller;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import io.micronaut.validation.exceptions.ConstraintExceptionHandler;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolationException;
import java.net.URI;
import java.util.List;

@Singleton
@Replaces(ConstraintExceptionHandler.class)
public class ConstraintViolationHandler
        implements ExceptionHandler<ConstraintViolationException, HttpResponse<ValidationProblemBody>> {

    private static final URI VALIDATION_TYPE = URI.create("https://example.com/problems/validation-error");

    @Override
    public HttpResponse<ValidationProblemBody> handle(HttpRequest request, ConstraintViolationException ex) {
        List<String> violations = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .toList();
        ValidationProblemBody body = new ValidationProblemBody(
                VALIDATION_TYPE,
                "Validation Error",
                400,
                "One or more request fields failed validation.",
                request.getUri(),
                violations);
        return HttpResponse.status(io.micronaut.http.HttpStatus.BAD_REQUEST)
                .contentType(new MediaType("application/problem+json"))
                .body(body);
    }

}
