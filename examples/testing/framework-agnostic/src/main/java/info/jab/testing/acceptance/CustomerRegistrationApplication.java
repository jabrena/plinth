package info.jab.testing.acceptance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.Executors;

public final class CustomerRegistrationApplication implements AutoCloseable {

    private static final ObjectMapper JSON = new ObjectMapper();

    private final HttpServer server;

    private CustomerRegistrationApplication(HttpServer server) {
        this.server = server;
    }

    public static CustomerRegistrationApplication start(int requestedPort) throws IOException {
        String datasourceUrl = requiredProperty("datasource.url");
        String datasourceUsername = System.getProperty("datasource.username", "sa");
        String datasourcePassword = System.getProperty("datasource.password", "");
        String customerRiskBaseUrl = requiredProperty("customer-risk.base-url");

        CustomerRepository repository =
            new CustomerRepository(datasourceUrl, datasourceUsername, datasourcePassword);
        CustomerRiskClient riskClient = new CustomerRiskClient(customerRiskBaseUrl);

        HttpServer server = HttpServer.create(
            new InetSocketAddress(InetAddress.getLoopbackAddress(), requestedPort),
            0);
        server.createContext("/api/customers", exchange -> handleCustomerRegistration(exchange, repository, riskClient));
        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        server.start();
        return new CustomerRegistrationApplication(server);
    }

    public int port() {
        return server.getAddress().getPort();
    }

    @Override
    public void close() {
        server.stop(0);
    }

    private static void handleCustomerRegistration(
        HttpExchange exchange,
        CustomerRepository repository,
        CustomerRiskClient riskClient)
        throws IOException {

        if (!Objects.equals("POST", exchange.getRequestMethod())) {
            sendJson(exchange, 405, """
                {"error":"method_not_allowed"}
                """);
            return;
        }

        try (exchange) {
            String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            CustomerRegistrationRequest request = JSON.readValue(requestBody, CustomerRegistrationRequest.class);
            CustomerRiskDecision riskDecision = riskClient.assess(request.email());
            Customer customer = repository.create(request.email(), request.name(), riskDecision.decision());

            sendJson(exchange, 201, JSON.writeValueAsString(customer));
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            sendJson(exchange, 503, """
                {"error":"risk_service_interrupted"}
                """);
        } catch (SQLException exception) {
            sendJson(exchange, 500, """
                {"error":"customer_storage_failed"}
                """);
        }
    }

    private static void sendJson(HttpExchange exchange, int statusCode, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        exchange.getResponseBody().write(bytes);
    }

    private static String requiredProperty(String key) {
        String value = System.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required system property: " + key);
        }
        return value;
    }
}
