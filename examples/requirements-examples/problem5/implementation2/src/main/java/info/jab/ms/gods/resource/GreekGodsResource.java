package info.jab.ms.gods.resource;

import info.jab.ms.gods.repository.GreekGodRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/v1/gods/greek")
@Tag(name = "Greek Gods", description = "Greek mythology catalog")
public class GreekGodsResource {

    @Inject
    GreekGodRepository repository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "getGreekGods",
            summary = "Get all Greek gods",
            description = "Returns a list of Greek god names")
    @APIResponse(
            responseCode = "200",
            description = "Successful operation",
            content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @APIResponse(
            responseCode = "500",
            description = "Internal server error (RFC 7807 Problem Details)",
            content = @Content(mediaType = "application/problem+json"))
    public List<String> list() {
        return repository.findAllNamesOrdered();
    }
}
