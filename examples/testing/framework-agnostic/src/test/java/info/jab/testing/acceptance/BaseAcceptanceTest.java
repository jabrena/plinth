package info.jab.testing.acceptance;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import java.io.IOException;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

abstract class BaseAcceptanceTest {

    @RegisterExtension
    static WireMockExtension customerRisk = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort())
        .build();

    protected static int port;
    protected static TestDatabaseFixture database;

    private static CustomerRegistrationApplication application;

    @BeforeAll
    static void setUpInfrastructure() throws IOException, SQLException {
        database = TestDatabaseFixture.start();

        System.setProperty("datasource.url", database.jdbcUrl());
        System.setProperty("datasource.username", database.username());
        System.setProperty("datasource.password", database.password());
        System.setProperty("customer-risk.base-url", customerRisk.baseUrl());

        application = CustomerRegistrationApplication.start(0);
        port = application.port();
    }

    @AfterAll
    static void tearDownInfrastructure() {
        if (application != null) {
            application.close();
        }
        if (database != null) {
            database.close();
        }
    }

    @BeforeEach
    void resetFixtures() throws SQLException {
        customerRisk.resetAll();
        database.clearCustomers();
    }
}
