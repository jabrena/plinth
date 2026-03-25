package info.jab.ms;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import java.util.Map;

public class WireMockQuarkusResource implements QuarkusTestResourceLifecycleManager {

    private static WireMockServer wireMockServer;

    @Override
    public Map<String, String> start() {
        wireMockServer = new WireMockServer(0); // dynamic port
        wireMockServer.start();
        int port = wireMockServer.port();
        System.setProperty("wiremock.port", String.valueOf(port));
        return Map.of("wiremock.port", String.valueOf(port));
    }

    @Override
    public void stop() {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }

    public static WireMockServer getWireMockServer() {
        return wireMockServer;
    }
}
