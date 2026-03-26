package info.jab.ms.gods.repository;

import info.jab.ms.gods.model.GreekGod;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import java.util.List;
import org.jboss.logging.Logger;

@ApplicationScoped
public class GreekGodRepository implements PanacheRepository<GreekGod> {

    private static final Logger LOG = Logger.getLogger(GreekGodRepository.class);

    @Transactional
    public List<String> findAllNamesOrdered() {
        try {
            List<String> names = listAll(Sort.ascending("name")).stream().map(g -> g.name).toList();
            LOG.debugf("Read %d Greek god name(s) from database", names.size());
            return names;
        } catch (PersistenceException e) {
            throw new GreekGodsDataAccessException("Failed to read Greek god names", e);
        }
    }

    @Transactional
    public void upsertByName(String name) {
        if (name == null || name.isBlank()) {
            return;
        }
        String trimmed = name.trim();
        try {
            getEntityManager()
                    .createNativeQuery(
                            """
                            INSERT INTO greek_god (name) VALUES (?1)
                            ON CONFLICT (name) DO NOTHING
                            """)
                    .setParameter(1, trimmed)
                    .executeUpdate();
        } catch (PersistenceException e) {
            throw new GreekGodsDataAccessException("Failed to upsert Greek god name", e);
        }
    }
}
