package info.jab.ms.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(GodOutboundProperties.class)
public class HttpClientConfig {

	@Bean
	public RestClient.Builder godRestClientBuilder(GodOutboundProperties props) {
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setConnectTimeout((int) props.connectTimeout().toMillis());
		factory.setReadTimeout((int) props.readTimeout().toMillis());
		return RestClient.builder().requestFactory(factory);
	}

	@Bean
	public RestClient godRestClient(RestClient.Builder godRestClientBuilder) {
		return godRestClientBuilder.build();
	}

	@Bean(name = "godAnalysisExecutor")
	public Executor godAnalysisExecutor() {
		return Executors.newVirtualThreadPerTaskExecutor();
	}
}
