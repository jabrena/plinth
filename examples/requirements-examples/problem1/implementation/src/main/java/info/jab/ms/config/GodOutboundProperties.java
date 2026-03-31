package info.jab.ms.config;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "god.outbound")
public record GodOutboundProperties(
		@DefaultValue("5s") Duration connectTimeout,
		@DefaultValue("5s") Duration readTimeout,
		Urls urls) {

	public record Urls(String greek, String roman, String nordic) {}
}
