package info.jab.ms.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import java.util.List;

@RegisterRestClient(configKey = "greek-gods-api")
@Path("/jabrena/latency-problems/greek")
public interface GreekGodsClient {
    @GET
    List<String> getGreekGods();
}
