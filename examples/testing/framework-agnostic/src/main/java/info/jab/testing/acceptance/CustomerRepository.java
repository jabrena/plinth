package info.jab.testing.acceptance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

final class CustomerRepository {

    private final String url;
    private final String username;
    private final String password;

    CustomerRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    Customer create(String email, String name, String riskDecision) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(
                "insert into customers(email, name, risk_decision) values (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, email);
            statement.setString(2, name);
            statement.setString(3, riskDecision);
            statement.executeUpdate();

            try (var keys = statement.getGeneratedKeys()) {
                if (!keys.next()) {
                    throw new SQLException("Customer insert did not return a generated id");
                }
                return new Customer(keys.getLong(1), email, name, riskDecision);
            }
        }
    }
}
