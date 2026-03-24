package info.jab.ms.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(GodApiProperties.class)
public class HttpClientConfig {

    @Bean
    public RestClient restClient(GodApiProperties properties) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(properties.timeout().connect());
        factory.setReadTimeout(properties.timeout().read());
        return RestClient.builder()
            .requestFactory(factory)
            .build();
    }
}
