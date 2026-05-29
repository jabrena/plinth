package info.jab.ms.controller;

import info.jab.ms.dto.SumRequest;
import info.jab.ms.dto.SumResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.validation.Validated;
import jakarta.validation.Valid;

@Controller("/api/v1")
@Validated
public class SumController {

    @Post("/sum")
    public SumResponse sum(@Body @Valid SumRequest request) {
        return new SumResponse(request.param1() + request.param2());
    }

}
