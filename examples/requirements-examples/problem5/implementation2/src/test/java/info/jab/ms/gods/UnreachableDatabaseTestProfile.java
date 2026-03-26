package info.jab.ms.gods;

import io.quarkus.test.junit.QuarkusTestProfile;
import java.util.Map;

public class UnreachableDatabaseTestProfile implements QuarkusTestProfile {

    @Override
    public Map<String, String> getConfigOverrides() {
        return Map.of(
                "quarkus.datasource.devservices.enabled", "false",
                "quarkus.flyway.enabled", "false",
                "quarkus.scheduler.enabled", "false",
                "quarkus.datasource.jdbc.url", "jdbc:postgresql://127.0.0.1:1/nodb",
                "quarkus.datasource.username", "u",
                "quarkus.datasource.password", "p");
    }
}
