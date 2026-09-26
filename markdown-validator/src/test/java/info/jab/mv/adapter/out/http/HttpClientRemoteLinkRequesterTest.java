package info.jab.mv.adapter.out.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import info.jab.mv.application.port.BlockedRemoteLinkException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

        HttpClientRemoteLinkRequester requester = new HttpClientRemoteLinkRequester(Duration.ofSeconds(2), uri -> true);
        URI redirectUri = URI.create("http://127.0.0.1:" + server.getAddress().getPort() + "/redirect");

        var response = requester.request(redirectUri, "GET", Duration.ofSeconds(2));

        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    void request_blocksLoopbackDestinationWithoutConnecting() throws Exception {
        AtomicInteger hits = new AtomicInteger();
        server = HttpServer.create(new InetSocketAddress(InetAddress.getLoopbackAddress(), 0), 0);
        server.createContext("/admin", exchange -> {
            hits.incrementAndGet();
            ok(exchange);
        });
        server.start();

        HttpClientRemoteLinkRequester requester = new HttpClientRemoteLinkRequester(Duration.ofSeconds(2));
        URI internalUri = URI.create("http://127.0.0.1:" + server.getAddress().getPort() + "/admin");

        assertThatThrownBy(() -> requester.request(internalUri, "HEAD", Duration.ofSeconds(2)))
                .isInstanceOf(BlockedRemoteLinkException.class);
        assertThat(hits).hasValue(0);
    }

    @Test
    void request_revalidatesRedirectTargets() throws Exception {
        AtomicInteger internalHits = new AtomicInteger();
        server = HttpServer.create(new InetSocketAddress(InetAddress.getLoopbackAddress(), 0), 0);
        server.createContext("/looks-fine", this::redirectToInternal);
        server.createContext("/internal", exchange -> {
            internalHits.incrementAndGet();
            ok(exchange);
        });
        server.start();

        HttpClientRemoteLinkRequester requester = new HttpClientRemoteLinkRequester(
                Duration.ofSeconds(2), uri -> !uri.getPath().startsWith("/internal"));
        URI entryUri = URI.create("http://127.0.0.1:" + server.getAddress().getPort() + "/looks-fine");

        assertThatThrownBy(() -> requester.request(entryUri, "GET", Duration.ofSeconds(2)))
                .isInstanceOf(BlockedRemoteLinkException.class)
                .hasMessageContaining("/internal");
        assertThat(internalHits).hasValue(0);
    }

    @Test
    void request_stopsAfterTooManyRedirects() throws Exception {
        server = HttpServer.create(new InetSocketAddress(InetAddress.getLoopbackAddress(), 0), 0);
        server.createContext("/loop", exchange -> {
            exchange.getResponseHeaders().add("Location", "/loop");
            exchange.sendResponseHeaders(302, -1);
            exchange.close();
        });
        server.start();

        HttpClientRemoteLinkRequester requester = new HttpClientRemoteLinkRequester(Duration.ofSeconds(2), uri -> true);
        URI loopUri = URI.create("http://127.0.0.1:" + server.getAddress().getPort() + "/loop");

        assertThatThrownBy(() -> requester.request(loopUri, "GET", Duration.ofSeconds(2)))
                .hasMessageContaining("Too many redirects");
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "127.0.0.1", "0.0.0.0", "10.1.2.3", "172.16.0.1", "192.168.1.1", "169.254.169.254",
        "100.64.0.1", "::1", "fe80::1", "fd00::1"
    })
    void isPublic_rejectsInternalAddresses(String address) throws Exception {
        assertThat(PublicAddressPolicy.isPublic(InetAddress.getByName(address))).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = { "8.8.8.8", "140.82.112.3", "2606:4700:4700::1111" })
    void isPublic_acceptsPublicAddresses(String address) throws Exception {
        assertThat(PublicAddressPolicy.isPublic(InetAddress.getByName(address))).isTrue();
    }

    private void redirectToInternal(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Location", "/internal/secret-status");
        exchange.sendResponseHeaders(302, -1);
        exchange.close();
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
