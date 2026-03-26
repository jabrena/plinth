package info.jab.ms.gods.config;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "greek-gods")
public class GreekGodsProperties {

	private Sync sync = new Sync();

	private RestClientSettings restClient = new RestClientSettings();

	public Sync getSync() {
		return sync;
	}

	public void setSync(Sync sync) {
		this.sync = sync;
	}

	public RestClientSettings getRestClient() {
		return restClient;
	}

	public void setRestClient(RestClientSettings restClient) {
		this.restClient = restClient;
	}

	public static class Sync {

		private String baseUrl = "https://my-json-server.typicode.com/jabrena/latency-problems";

		private boolean enabled = true;

		private long fixedDelayMs = 3_600_000L;

		private long initialDelayMs = 60_000L;

		public String getBaseUrl() {
			return baseUrl;
		}

		public void setBaseUrl(String baseUrl) {
			this.baseUrl = baseUrl;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		public long getFixedDelayMs() {
			return fixedDelayMs;
		}

		public void setFixedDelayMs(long fixedDelayMs) {
			this.fixedDelayMs = fixedDelayMs;
		}

		public long getInitialDelayMs() {
			return initialDelayMs;
		}

		public void setInitialDelayMs(long initialDelayMs) {
			this.initialDelayMs = initialDelayMs;
		}
	}

	public static class RestClientSettings {

		private Duration connectTimeout = Duration.ofSeconds(5);

		private Duration readTimeout = Duration.ofSeconds(30);

		public Duration getConnectTimeout() {
			return connectTimeout;
		}

		public void setConnectTimeout(Duration connectTimeout) {
			this.connectTimeout = connectTimeout;
		}

		public Duration getReadTimeout() {
			return readTimeout;
		}

		public void setReadTimeout(Duration readTimeout) {
			this.readTimeout = readTimeout;
		}
	}
}
