package info.jab.ms.client;

import info.jab.ms.api.model.PantheonSource;

public interface OutboundCallObserver {

    void onStart(PantheonSource source);

    void onSuccess(PantheonSource source, int payloadSize);

    void onFailure(PantheonSource source, Exception exception);
}
