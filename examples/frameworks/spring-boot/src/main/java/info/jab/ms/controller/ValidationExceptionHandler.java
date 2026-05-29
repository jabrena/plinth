package info.jab.ms.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.net.URI;
import java.util.List;
import java.util.stream.StreamSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@ControllerAdvice
public class ValidationExceptionHandler {

	private static final URI VALIDATION_TYPE = URI.create("https://example.com/problems/validation-error");

	private ProblemDetail buildValidationProblem(HttpServletRequest request, List<String> violations) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(
				HttpStatus.BAD_REQUEST, "One or more request fields failed validation.");
		problem.setType(VALIDATION_TYPE);
		problem.setTitle("Validation Error");
		problem.setInstance(URI.create(request.getRequestURI()));
		problem.setProperty("violations", violations);
		return problem;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<ProblemDetail> handleBody(MethodArgumentNotValidException ex, HttpServletRequest request) {
		List<String> violations = ex.getBindingResult().getFieldErrors().stream()
				.map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
				.toList();
		return ResponseEntity.badRequest().body(buildValidationProblem(request, violations));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	ResponseEntity<ProblemDetail> handleConstraints(ConstraintViolationException ex, HttpServletRequest request) {
		List<String> violations = ex.getConstraintViolations().stream()
				.map(v -> v.getPropertyPath() + ": " + v.getMessage())
				.toList();
		return ResponseEntity.badRequest().body(buildValidationProblem(request, violations));
	}

	@ExceptionHandler(HandlerMethodValidationException.class)
	ResponseEntity<ProblemDetail> handleMethodValidation(HandlerMethodValidationException ex,
			HttpServletRequest request) {
		List<String> violations = ex.getParameterValidationResults().stream()
				.flatMap((ParameterValidationResult result) -> StreamSupport.stream(
						result.getResolvableErrors().spliterator(), false)
						.map(error -> result.getMethodParameter().getParameterName() + ": "
								+ error.getDefaultMessage()))
				.toList();
		return ResponseEntity.badRequest().body(buildValidationProblem(request, violations));
	}

}
