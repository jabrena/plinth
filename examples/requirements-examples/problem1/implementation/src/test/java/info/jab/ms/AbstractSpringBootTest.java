package info.jab.ms;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

/**
 * Shared Spring Boot test setup: one WireMock instance and dynamic god-api URLs so subclasses
 * participate in the same {@link SpringBootTest} context definition pattern.
 * <p>
 * Subclasses add their own {@link org.springframework.test.context.TestPropertySource} or
 * other annotations; different property sets still produce separate cached contexts.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractSpringBootTest {

    @RegisterExtension
    protected static final WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @DynamicPropertySource
    static void wireMockProperties(DynamicPropertyRegistry registry) {
        registry.add("god-api.sources.greek.url", () -> wireMock.baseUrl() + "/greek");
        registry.add("god-api.sources.roman.url", () -> wireMock.baseUrl() + "/roman");
        registry.add("god-api.sources.nordic.url", () -> wireMock.baseUrl() + "/nordic");
    }

    @LocalServerPort
    protected int port;
}
