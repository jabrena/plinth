package info.jab.ms.gods.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class SchedulingConfigurationTest {

	@Test
	void schedulingEnabledByDefault() {
		new ApplicationContextRunner()
				.withUserConfiguration(SchedulingConfiguration.class)
				.run(ctx -> assertThat(ctx).hasSingleBean(SchedulingConfiguration.class));
	}

	@Test
	void schedulingDisabledWhenSyncPropertyFalse() {
		new ApplicationContextRunner()
				.withUserConfiguration(SchedulingConfiguration.class)
				.withPropertyValues("greek-gods.sync.enabled=false")
				.run(ctx -> assertThat(ctx).doesNotHaveBean(SchedulingConfiguration.class));
	}
}
