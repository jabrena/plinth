package info.jab.ms.gods.repository;

import java.util.List;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class GreekGodRepository {

	private static final String FIND_ALL_ORDERED_BY_NAME =
			"""
			SELECT id, name FROM greek_god ORDER BY name ASC
			""";

	private final JdbcClient jdbcClient;

	public GreekGodRepository(JdbcClient jdbcClient) {
		this.jdbcClient = jdbcClient;
	}

	public List<GreekGod> findAllByOrderByNameAsc() {
		return jdbcClient.sql(FIND_ALL_ORDERED_BY_NAME).query(GreekGod.class).list();
	}
}
