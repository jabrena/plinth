package info.jab.ms.gods.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	private static final URI DATA_ACCESS_TYPE = URI.create("urn:problem:greek-gods:data-access");

	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<ProblemDetail> handleDataAccess(DataAccessException ex, HttpServletRequest request) {
		return persistenceProblem(ex, request);
	}

	@ExceptionHandler(CannotCreateTransactionException.class)
	public ResponseEntity<ProblemDetail> handleCannotCreateTransaction(
			CannotCreateTransactionException ex, HttpServletRequest request) {
		return persistenceProblem(ex, request);
	}

	private static ResponseEntity<ProblemDetail> persistenceProblem(Exception ex, HttpServletRequest request) {
		log.error("Data access failure on {}", request.getRequestURI(), ex);
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(
				HttpStatus.INTERNAL_SERVER_ERROR,
				"The Greek gods catalog could not be read from the database.");
		problem.setType(DATA_ACCESS_TYPE);
		problem.setTitle("Database access failed");
		problem.setInstance(URI.create(request.getRequestURI()));
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.contentType(MediaType.parseMediaType("application/problem+json"))
				.body(problem);
	}
}
