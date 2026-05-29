package info.jab.lambda;

import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.HttpStatusType;
import java.util.HashMap;
import java.util.Map;

/**
 * Mock implementation of HttpResponseMessage for unit testing Azure Functions.
 * This mock allows for verification of HTTP responses in unit tests without requiring
 * the actual Azure Functions runtime.
 *
 * Following Java unit testing best practices:
 * - Package-private visibility as per Rule 10
 * - Clean, maintainable code structure
 * - Proper encapsulation and immutability
 */
class HttpResponseMessageMock implements HttpResponseMessage {

    private final HttpStatusType httpStatus;
    private final Map<String, String> headers;
    private final Object body;

    HttpResponseMessageMock(HttpStatusType status, Map<String, String> headers, Object body) {
        this.httpStatus = status;
        this.headers = new HashMap<>(headers); // Defensive copy for immutability
        this.body = body;
    }

    @Override
    public HttpStatusType getStatus() {
        return this.httpStatus;
    }

    @Override
    public String getHeader(String key) {
        return this.headers.get(key);
    }

    @Override
    public Object getBody() {
        return this.body;
    }

    /**
     * Builder implementation for creating HttpResponseMessage mocks.
     * Provides a fluent API for test setup.
     */
    static class HttpResponseMessageBuilderMock implements HttpResponseMessage.Builder {

        private Object body;
        private final Map<String, String> headers = new HashMap<>();
        private HttpStatusType httpStatus;

        public Builder status(HttpStatus status) {
            this.httpStatus = status;
            return this;
        }

        @Override
        public Builder status(HttpStatusType httpStatusType) {
            this.httpStatus = httpStatusType;
            return this;
        }

        @Override
        public Builder header(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        @Override
        public Builder body(Object body) {
            this.body = body;
            return this;
        }

        @Override
        public HttpResponseMessage build() {
            return new HttpResponseMessageMock(this.httpStatus, this.headers, this.body);
        }
    }
}
