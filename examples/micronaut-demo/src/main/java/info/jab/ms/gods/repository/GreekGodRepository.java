package info.jab.ms.gods.repository;

import jakarta.inject.Singleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class GreekGodRepository {

    private static final Logger LOG = LoggerFactory.getLogger(GreekGodRepository.class);

    private static final String SELECT_ORDERED = "SELECT name FROM greek_god ORDER BY name";
    private static final String UPSERT =
            """
            INSERT INTO greek_god (name) VALUES (?)
            ON CONFLICT (name) DO NOTHING
            """;

    private final DataSource dataSource;

    public GreekGodRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<String> findAllNamesOrdered() {
        try (Connection c = dataSource.getConnection();
                PreparedStatement ps = c.prepareStatement(SELECT_ORDERED);
                ResultSet rs = ps.executeQuery()) {
            List<String> names = new ArrayList<>();
            while (rs.next()) {
                names.add(rs.getString(1));
            }
            LOG.debug("Loaded {} Greek god names from database", names.size());
            return names;
        } catch (SQLException e) {
            throw new GreekGodsDataAccessException("Failed to load Greek god names", e);
        }
    }

    public void upsertByName(String name) {
        try (Connection c = dataSource.getConnection();
                PreparedStatement ps = c.prepareStatement(UPSERT)) {
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new GreekGodsDataAccessException("Failed to upsert Greek god: " + name, e);
        }
    }
}
