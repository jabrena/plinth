package info.jab.ms.gods.service;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "greek-gods-upstream-api")
@Path("")
public interface GreekGodsUpstreamClient {

    @GET
    @Path("/greek")
    @Produces(MediaType.APPLICATION_JSON)
    List<String> fetchGreek();
}
