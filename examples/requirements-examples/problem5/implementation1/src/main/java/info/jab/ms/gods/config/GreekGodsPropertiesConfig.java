package info.jab.ms.gods.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(GreekGodsProperties.class)
public class GreekGodsPropertiesConfig {
}
