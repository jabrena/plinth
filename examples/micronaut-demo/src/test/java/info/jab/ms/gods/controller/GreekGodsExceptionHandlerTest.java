package info.jab.ms.gods.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import info.jab.ms.gods.repository.GreekGodsDataAccessException;
import io.micronaut.http.HttpRequest;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GreekGodsExceptionHandlerTest {

    @Mock
    HttpRequest<?> request;

    GreekGodsExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GreekGodsExceptionHandler();
        when(request.getPath()).thenReturn("/api/v1/gods/greek");
    }

    @Test
    void handle_returnsProblemJsonWithNormativeFields() {
        var ex = new GreekGodsDataAccessException("connection failed", new SQLException("boom"));
        var response = handler.handle(request, ex);
        assertThat(response.getStatus().getCode()).isEqualTo(500);
        assertThat(response.getBody()).isPresent();
        var body = response.getBody().get();
        assertThat(body.getType()).isEqualTo(GreekGodsExceptionHandler.PROBLEM_TYPE);
        assertThat(body.getTitle()).isEqualTo("Database access failed");
        assertThat(body.getStatus()).isEqualTo(500);
        assertThat(body.getDetail()).contains("connection failed");
        assertThat(body.getInstance()).isEqualTo("/api/v1/gods/greek");
    }
}
