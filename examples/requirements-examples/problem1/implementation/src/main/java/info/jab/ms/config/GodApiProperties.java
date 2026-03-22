package info.jab.ms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "god-api")
public class GodApiProperties {

    private Map<String, SourceConfig> sources = new HashMap<>();
    private TimeoutConfig timeout = new TimeoutConfig();

    public Map<String, SourceConfig> getSources() { return sources; }
    public void setSources(Map<String, SourceConfig> sources) { this.sources = sources; }
    public TimeoutConfig getTimeout() { return timeout; }
    public void setTimeout(TimeoutConfig timeout) { this.timeout = timeout; }

    public static class SourceConfig {
        private String url;
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
    }

    public static class TimeoutConfig {
        private long connect = 5000;
        private long read = 5000;
        public long getConnect() { return connect; }
        public void setConnect(long connect) { this.connect = connect; }
        public long getRead() { return read; }
        public void setRead(long read) { this.read = read; }
    }
}
