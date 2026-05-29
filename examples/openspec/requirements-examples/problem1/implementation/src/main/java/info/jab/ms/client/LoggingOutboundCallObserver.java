package info.jab.ms.client;

import info.jab.ms.api.model.PantheonSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
final class LoggingOutboundCallObserver implements OutboundCallObserver {

    private static final Logger log = LoggerFactory.getLogger(LoggingOutboundCallObserver.class);

    @Override
    public void onStart(PantheonSource source) {
        log.info("outbound_call_started source={}", source.getValue());
    }

    @Override
    public void onSuccess(PantheonSource source, int payloadSize) {
        log.info("outbound_call_completed source={} size={}", source.getValue(), payloadSize);
    }

    @Override
    public void onFailure(PantheonSource source, Exception exception) {
        log.warn("outbound_call_failed source={} reason={}", source.getValue(), exception.getMessage());
    }
}
