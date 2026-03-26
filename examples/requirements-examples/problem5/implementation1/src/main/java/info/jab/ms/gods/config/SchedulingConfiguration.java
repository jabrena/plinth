package info.jab.ms.gods.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ConditionalOnProperty(prefix = "greek-gods.sync", name = "enabled", havingValue = "true", matchIfMissing = true)
public class SchedulingConfiguration {
}
