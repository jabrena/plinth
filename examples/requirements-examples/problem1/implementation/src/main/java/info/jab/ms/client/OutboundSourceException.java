package info.jab.ms.client;

import info.jab.ms.api.model.PantheonSource;

public final class OutboundSourceException extends RuntimeException {

    public OutboundSourceException(PantheonSource source, Throwable cause) {
        super("Outbound source fetch failed for source: " + source.getValue(), cause);
    }
}
