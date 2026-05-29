package info.jab.ms.controller;

import info.jab.ms.dto.SumResponse;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
class SumControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void sumReturns400WhenParam1IsMissing() {
        HttpClientResponseException ex = assertThrows(HttpClientResponseException.class, () ->
                client.toBlocking().exchange(
                        HttpRequest.POST("/api/v1/sum", "{\"param2\": 1}")
                                .contentType(MediaType.APPLICATION_JSON),
                        ValidationProblemBody.class));

        assertEquals(400, ex.getStatus().getCode());
        assertTrue(ex.getResponse().getContentType().orElseThrow().getName().contains("application/problem+json"));

        ValidationProblemBody body = ex.getResponse().getBody(ValidationProblemBody.class).orElseThrow();
        assertEquals("https://example.com/problems/validation-error", body.type().toString());
        assertEquals("Validation Error", body.title());
        assertEquals(400, body.status());
        assertEquals("One or more request fields failed validation.", body.detail());
        assertEquals("/api/v1/sum", body.instance().toString());
        assertEquals(1, body.violations().size());
        assertTrue(body.violations().getFirst().contains("param1"));
        assertTrue(body.violations().getFirst().contains("must not be null"));
    }

    @Test
    void sumReturns400WhenParam2IsNull() {
        HttpClientResponseException ex = assertThrows(HttpClientResponseException.class, () ->
                client.toBlocking().exchange(
                        HttpRequest.POST("/api/v1/sum", "{\"param1\": 1, \"param2\": null}")
                                .contentType(MediaType.APPLICATION_JSON),
                        ValidationProblemBody.class));

        assertEquals(400, ex.getStatus().getCode());
        assertTrue(ex.getResponse().getContentType().orElseThrow().getName().contains("application/problem+json"));

        ValidationProblemBody body = ex.getResponse().getBody(ValidationProblemBody.class).orElseThrow();
        assertEquals("https://example.com/problems/validation-error", body.type().toString());
        assertEquals("Validation Error", body.title());
        assertEquals(400, body.status());
        assertEquals(1, body.violations().size());
        assertTrue(body.violations().getFirst().contains("param2"));
        assertTrue(body.violations().getFirst().contains("must not be null"));
    }

    @Test
    void sumReturnsResultWhenRequestIsValid() {
        HttpResponse<SumResponse> response = client.toBlocking().exchange(
                HttpRequest.POST("/api/v1/sum", "{\"param1\": 10, \"param2\": 32}")
                        .contentType(MediaType.APPLICATION_JSON),
                SumResponse.class);

        assertEquals(200, response.getStatus().getCode());
        assertEquals(42, response.body().result());
    }

    @Test
    void sumReturnsZeroWhenBothParamsAreZero() {
        HttpResponse<SumResponse> response = client.toBlocking().exchange(
                HttpRequest.POST("/api/v1/sum", "{\"param1\": 0, \"param2\": 0}")
                        .contentType(MediaType.APPLICATION_JSON),
                SumResponse.class);

        assertEquals(200, response.getStatus().getCode());
        assertEquals(0, response.body().result());
    }

    @Test
    void sumHandlesNegativeValues() {
        HttpResponse<SumResponse> response = client.toBlocking().exchange(
                HttpRequest.POST("/api/v1/sum", "{\"param1\": -5, \"param2\": 8}")
                        .contentType(MediaType.APPLICATION_JSON),
                SumResponse.class);

        assertEquals(200, response.getStatus().getCode());
        assertEquals(3, response.body().result());
    }

}
