package info.jab.http;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.RegisterExtension;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

abstract class BaseIntegrationTest {

    @RegisterExtension
    protected static final WireMockExtension WIREMOCK = WireMockExtension.newInstance()
        .options(wireMockConfig()
            .dynamicPort()
            .usingFilesUnderClasspath("wiremock"))
        .build();

    @BeforeAll
    static void propagateContainerCoordinates() {
        System.setProperty("external.example-api.base-url", WIREMOCK.baseUrl());
    }
}
