package info.jab.ms.gods.testsupport;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import java.util.Map;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresWireMockTestResource implements QuarkusTestResourceLifecycleManager {

    public static volatile WireMockServer wireMock;

    private PostgreSQLContainer<?> postgres;
    private WireMockServer server;

    @Override
    public Map<String, String> start() {
        postgres = new PostgreSQLContainer<>("postgres:16-alpine");
        postgres.start();

        server = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        server.start();
        wireMock = server;

        String upstreamBase = "http://localhost:" + server.port();
        return Map.of(
                "quarkus.datasource.jdbc.url", postgres.getJdbcUrl(),
                "quarkus.datasource.username", postgres.getUsername(),
                "quarkus.datasource.password", postgres.getPassword(),
                "quarkus.datasource.devservices.enabled", "false",
                "quarkus.rest-client.greek-gods-upstream-api.url", upstreamBase);
    }

    @Override
    public void stop() {
        wireMock = null;
        if (server != null) {
            server.stop();
        }
        if (postgres != null) {
            postgres.stop();
        }
    }
}
