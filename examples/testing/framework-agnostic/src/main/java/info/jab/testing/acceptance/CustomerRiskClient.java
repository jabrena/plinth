package info.jab.testing.acceptance;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

final class CustomerRiskClient {

    private static final ObjectMapper JSON = new ObjectMapper();

    private final HttpClient httpClient;
    private final URI baseUri;

    CustomerRiskClient(String baseUrl) {
        this.httpClient = HttpClient.newHttpClient();
        this.baseUri = URI.create(baseUrl);
    }

    CustomerRiskDecision assess(String email) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(baseUri.resolve("/risk-assessments"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString("""
                {"email":"%s"}
                """.formatted(email)))
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("Customer risk service returned status " + response.statusCode());
        }

        JsonNode responseJson = JSON.readTree(response.body());
        return new CustomerRiskDecision(responseJson.path("decision").asText("REVIEW"));
    }
}
