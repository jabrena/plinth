package info.jab.ms.gods.controller;

import info.jab.ms.gods.api.AbstractGreekGodsController;
import info.jab.ms.gods.repository.GreekGodRepository;
import io.micronaut.http.annotation.Controller;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements the contract from {@code src/main/resources/openapi/greekController-oas.yaml} via the
 * generated {@link AbstractGreekGodsController}.
 */
@Controller
public final class GreekGodsController extends AbstractGreekGodsController {

    private static final Logger LOG = LoggerFactory.getLogger(GreekGodsController.class);

    private final GreekGodRepository repository;

    public GreekGodsController(GreekGodRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<String> getGreekGods() {
        LOG.debug("Serving Greek gods list from database");
        return repository.findAllNamesOrdered();
    }
}
