package info.jab.ms.controller;

import info.jab.ms.dto.SumRequest;
import info.jab.ms.dto.SumResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/v1")
public class SumController {

    @POST
    @Path("/sum")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public SumResponse sum(@Valid @NotNull SumRequest request) {
        return new SumResponse(request.param1() + request.param2());
    }

}
