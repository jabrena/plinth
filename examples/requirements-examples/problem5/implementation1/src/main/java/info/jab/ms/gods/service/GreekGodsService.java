package info.jab.ms.gods.service;

import info.jab.ms.gods.repository.GreekGod;
import info.jab.ms.gods.repository.GreekGodRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GreekGodsService {

	private static final Logger log = LoggerFactory.getLogger(GreekGodsService.class);

	private final GreekGodRepository repository;

	public GreekGodsService(GreekGodRepository repository) {
		this.repository = repository;
	}

	@Transactional(readOnly = true)
	public List<String> findAllNamesOrdered() {
		log.debug("Loading all Greek god names from database");
		return repository.findAllByOrderByNameAsc().stream().map(GreekGod::name).toList();
	}
}
