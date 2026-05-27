package info.jab.ms.config;

import java.net.http.HttpClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(GodOutboundProperties.class)
class HttpClientConfig {

    @Bean
    RestClient godOutboundRestClient(GodOutboundProperties properties) {
        var httpClient = HttpClient.newBuilder()
            .connectTimeout(properties.connectTimeout())
            .build();

        var requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(properties.readTimeout());

        return RestClient.builder()
            .requestFactory(requestFactory)
            .build();
    }
}
