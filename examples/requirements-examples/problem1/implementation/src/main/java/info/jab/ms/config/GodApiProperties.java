package info.jab.ms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "god-api")
public record GodApiProperties(Sources sources, Timeout timeout) {

    public record Sources(Source greek, Source roman, Source nordic) {}

    public record Source(String url) {}

    public record Timeout(
        @DefaultValue("5000") int connect,
        @DefaultValue("5000") int read
    ) {}
}
