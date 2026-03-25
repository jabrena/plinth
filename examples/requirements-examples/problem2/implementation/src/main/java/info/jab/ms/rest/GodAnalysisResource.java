package info.jab.ms.rest;

import info.jab.ms.model.GodAnalysisResponse;
import info.jab.ms.service.GodAnalysisService;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

@Path("/api/v1/gods")
public class GodAnalysisResource {

    private static final Logger LOG = Logger.getLogger(GodAnalysisResource.class);

    @Inject
    GodAnalysisService service;

    @GET
    @Path("/wikipedia/most-literature")
    @Produces(MediaType.APPLICATION_JSON)
    @RunOnVirtualThread
    public Response getMostLiterature() {
        LOG.info("Received request for most-literature");
        try {
            GodAnalysisResponse result = service.findGodsWithMostLiterature();
            LOG.infof("Returning result: %s", result);
            return Response.ok(result).build();
        } catch (Exception e) {
            LOG.errorf("Greek API failure: %s", e.getMessage());
            return Response.status(503).build();
        }
    }
}
