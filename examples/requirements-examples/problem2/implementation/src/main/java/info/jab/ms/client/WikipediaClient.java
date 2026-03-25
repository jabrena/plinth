package info.jab.ms.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "wikipedia-api")
@Path("/wiki")
public interface WikipediaClient {
    @GET
    @Path("/{god}")
    String getWikipediaPage(@PathParam("god") String god);
}
