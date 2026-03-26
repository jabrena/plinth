package info.jab.ms.gods.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

class GodsRestClientConfigTest {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withUserConfiguration(GodsRestClientConfig.class, GreekGodsPropertiesConfig.class)
			.withPropertyValues("greek-gods.sync.base-url=https://example.test/api");

	@Test
	void registersGreekGodsSyncRestClient() {
		contextRunner.run(ctx -> assertThat(ctx.getBeanNamesForType(RestClient.class)).hasSize(1));
	}
}
