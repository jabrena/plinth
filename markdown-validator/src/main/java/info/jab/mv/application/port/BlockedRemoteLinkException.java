package info.jab.mv.application.port;

import java.io.IOException;
import java.io.Serial;
import java.net.URI;

/** Signals that a remote link was not requested because it targets a non-public network address. */
public final class BlockedRemoteLinkException extends IOException {

    @Serial
    private static final long serialVersionUID = 1L;

    public BlockedRemoteLinkException(URI uri) {
        super("Remote link targets a non-public address: " + uri);
    }
}
