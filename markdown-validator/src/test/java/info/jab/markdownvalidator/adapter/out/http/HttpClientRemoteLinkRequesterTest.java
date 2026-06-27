package info.jab.markdownvalidator.adapter.out.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.time.Duration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HttpClientRemoteLinkRequesterTest {

    private HttpServer server;

    @AfterEach
    void stopServer() {
        if (server != null) {
            server.stop(0);
        }
    }

    @Test
    void request_followsRedirects() throws Exception {
        server = HttpServer.create(new InetSocketAddress(InetAddress.getLoopbackAddress(), 0), 0);
        server.createContext("/redirect", this::redirectToOk);
        server.createContext("/ok", this::ok);
        server.start();

        HttpClientRemoteLinkRequester requester = new HttpClientRemoteLinkRequester(Duration.ofSeconds(2));
        URI redirectUri = URI.create("http://127.0.0.1:" + server.getAddress().getPort() + "/redirect");

        var response = requester.request(redirectUri, "GET", Duration.ofSeconds(2));

        assertThat(response.statusCode()).isEqualTo(200);
    }

    private void redirectToOk(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Location", "/ok");
        exchange.sendResponseHeaders(302, -1);
        exchange.close();
    }

    private void ok(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(200, -1);
        exchange.close();
    }
}
