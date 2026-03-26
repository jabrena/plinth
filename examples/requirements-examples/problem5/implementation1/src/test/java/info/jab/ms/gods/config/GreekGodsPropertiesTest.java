package info.jab.ms.gods.config;

import java.time.Duration;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GreekGodsPropertiesTest {

	@Test
	void defaultSyncSettings() {
		GreekGodsProperties props = new GreekGodsProperties();

		assertThat(props.getSync().getBaseUrl()).contains("my-json-server.typicode.com");
		assertThat(props.getSync().isEnabled()).isTrue();
		assertThat(props.getSync().getFixedDelayMs()).isEqualTo(3_600_000L);
		assertThat(props.getSync().getInitialDelayMs()).isEqualTo(60_000L);
	}

	@Test
	void defaultRestClientTimeouts() {
		GreekGodsProperties props = new GreekGodsProperties();

		assertThat(props.getRestClient().getConnectTimeout()).isEqualTo(Duration.ofSeconds(5));
		assertThat(props.getRestClient().getReadTimeout()).isEqualTo(Duration.ofSeconds(30));
	}

	@Test
	void settersOverrideNestedValues() {
		GreekGodsProperties props = new GreekGodsProperties();
		props.getSync().setBaseUrl("https://custom.example");
		props.getSync().setEnabled(false);
		props.getRestClient().setConnectTimeout(Duration.ofMillis(100));

		assertThat(props.getSync().getBaseUrl()).isEqualTo("https://custom.example");
		assertThat(props.getSync().isEnabled()).isFalse();
		assertThat(props.getRestClient().getConnectTimeout()).isEqualTo(Duration.ofMillis(100));
	}
}
