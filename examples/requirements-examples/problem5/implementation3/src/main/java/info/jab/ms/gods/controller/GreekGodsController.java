package info.jab.ms.gods.controller;

import info.jab.ms.gods.repository.GreekGodRepository;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/api/v1/gods")
@Tag(name = "Greek Gods", description = "Greek mythology name catalogue")
public final class GreekGodsController {

    private static final Logger LOG = LoggerFactory.getLogger(GreekGodsController.class);

    private final GreekGodRepository repository;

    public GreekGodsController(GreekGodRepository repository) {
        this.repository = repository;
    }

    @Get(uri = "/greek", produces = MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Get all Greek gods",
            description = "Returns a list of Greek god names from the synchronized database",
            operationId = "getGreekGods")
    @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            array = @ArraySchema(schema = @Schema(type = "string", example = "Zeus"))))
    @ApiResponse(
            responseCode = "500",
            description = "Internal server error (RFC 7807 Problem Details)",
            content = @Content(mediaType = "application/problem+json"))
    public List<String> getGreekGods() {
        LOG.debug("Serving Greek gods list from database");
        return repository.findAllNamesOrdered();
    }
}
