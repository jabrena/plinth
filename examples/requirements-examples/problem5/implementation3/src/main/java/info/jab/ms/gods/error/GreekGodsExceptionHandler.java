package info.jab.ms.gods.error;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Produces("application/problem+json")
@Requires(classes = GreekGodsDataAccessException.class)
public final class GreekGodsExceptionHandler
        implements ExceptionHandler<GreekGodsDataAccessException, HttpResponse<ProblemDetailBody>> {

    private static final Logger LOG = LoggerFactory.getLogger(GreekGodsExceptionHandler.class);

    private static final MediaType PROBLEM_JSON = new MediaType("application/problem+json");

    static final String PROBLEM_TYPE = "urn:problem:greek-gods:data-access";

    @Override
    public HttpResponse<ProblemDetailBody> handle(HttpRequest request, GreekGodsDataAccessException exception) {
        LOG.warn("Greek gods read path failed: path={}", request.getPath(), exception);
        var body = new ProblemDetailBody(PROBLEM_TYPE, "Database access failed", 500);
        return HttpResponse.<ProblemDetailBody>serverError().contentType(PROBLEM_JSON).body(body);
    }
}
