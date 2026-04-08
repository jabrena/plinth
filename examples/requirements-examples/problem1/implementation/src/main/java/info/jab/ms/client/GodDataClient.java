package info.jab.ms.client;

import info.jab.ms.algorithm.UnicodeAggregator;
import info.jab.ms.api.model.PantheonSource;
import info.jab.ms.config.GodOutboundProperties;
import info.jab.ms.service.GodData;
import info.jab.ms.service.PantheonDataSource;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class GodDataClient implements PantheonDataSource {

    private static final ParameterizedTypeReference<List<String>> GOD_NAMES_TYPE = new ParameterizedTypeReference<>() {
    };

    private final RestClient restClient;
    private final GodOutboundProperties properties;
    private final UnicodeAggregator unicodeAggregator;
    private final OutboundCallObserver outboundCallObserver;

    public GodDataClient(
        @Qualifier("godOutboundRestClient") RestClient restClient,
        GodOutboundProperties properties,
        UnicodeAggregator unicodeAggregator,
        OutboundCallObserver outboundCallObserver
    ) {
        this.restClient = restClient;
        this.properties = properties;
        this.unicodeAggregator = unicodeAggregator;
        this.outboundCallObserver = outboundCallObserver;
    }

    @Override
    public List<GodData> fetch(PantheonSource source) {
        outboundCallObserver.onStart(source);
        try {
            var names = restClient.get()
                .uri(properties.getUrlFor(source))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(GOD_NAMES_TYPE);
            var result = names.stream()
                .map(name -> new GodData(name, unicodeAggregator.toBigInteger(name)))
                .toList();
            outboundCallObserver.onSuccess(source, result.size());
            return result;
        } catch (RestClientException exception) {
            outboundCallObserver.onFailure(source, exception);
            throw new OutboundSourceException(source, exception);
        }
    }
}
