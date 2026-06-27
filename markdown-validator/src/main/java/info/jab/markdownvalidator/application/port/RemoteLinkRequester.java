package info.jab.markdownvalidator.application.port;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;

public interface RemoteLinkRequester {
    RemoteLinkResponse request(URI uri, String method, Duration timeout) throws IOException, InterruptedException;
}
