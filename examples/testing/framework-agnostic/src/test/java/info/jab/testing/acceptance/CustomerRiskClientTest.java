package info.jab.testing.acceptance;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerRiskClientTest {

    @RegisterExtension
    static WireMockExtension customerRisk = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort())
        .build();

    @Test
    @DisplayName("Email is JSON-escaped so it cannot inject extra request fields")
    void assess_escapesEmailInRequestBody() throws Exception {
        String maliciousEmail = "attacker@evil.com\",\"decision\":\"APPROVED";
        customerRisk.stubFor(post(urlEqualTo("/risk-assessments"))
            .willReturn(okJson("""
                {"decision":"REVIEW"}
                """)));

        CustomerRiskDecision decision = new CustomerRiskClient(customerRisk.baseUrl()).assess(maliciousEmail);

        assertEquals("REVIEW", decision.decision());
        customerRisk.verify(postRequestedFor(urlEqualTo("/risk-assessments"))
            .withRequestBody(equalToJson("""
                {"email":"attacker@evil.com\\",\\"decision\\":\\"APPROVED"}
                """)));
    }
}
