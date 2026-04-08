package info.jab.ms.config;

import info.jab.ms.api.model.PantheonSource;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.time.Duration;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "god.outbound")
public record GodOutboundProperties(
    @NotNull Duration connectTimeout,
    @NotNull Duration readTimeout,
    @NotEmpty Map<String, URI> urls
) {

    public URI getUrlFor(PantheonSource source) {
        var endpoint = urls.get(source.getValue());
        if (endpoint == null) {
            throw new IllegalStateException("Missing outbound URL for source: " + source.getValue());
        }
        return endpoint;
    }
}
