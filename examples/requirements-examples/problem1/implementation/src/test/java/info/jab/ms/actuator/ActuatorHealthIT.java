package info.jab.ms.actuator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag("integration-test")
class ActuatorHealthIT {

    @LocalServerPort
    private int port;

    private RestClient client;

    @BeforeEach
    void setUp() {
        client = RestClient.builder().baseUrl("http://localhost:" + port).build();
    }

    @Test
    void healthEndpointShouldReturn404AfterActuatorRemoval() {
        // When/Then - Verify that /actuator/health returns 404 Not Found
        assertThatThrownBy(() -> client.get()
                .uri("/actuator/health")
                .retrieve()
                .body(String.class))
                .isInstanceOf(RestClientResponseException.class)
                .satisfies(ex -> {
                    RestClientResponseException restEx = (RestClientResponseException) ex;
                    assertThat(restEx.getStatusCode().value()).isEqualTo(404);
                });
    }
}