package info.jab.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for AWS Lambda Hello World Handler.
 */
public class LambdaTest {

    private Lambda app;
    private Context mockContext;

    @BeforeEach
    public void setUp() {
        app = new Lambda();

        // Create a mock context for testing
        mockContext = new Context() {
            @Override
            public String getAwsRequestId() {
                return "test-request-id";
            }

            @Override
            public String getLogGroupName() {
                return "test-log-group";
            }

            @Override
            public String getLogStreamName() {
                return "test-log-stream";
            }

            @Override
            public String getFunctionName() {
                return "TestFunction";
            }

            @Override
            public String getFunctionVersion() {
                return "1.0";
            }

            @Override
            public String getInvokedFunctionArn() {
                return "arn:aws:lambda:us-east-1:123456789012:function:TestFunction";
            }

            @Override
            public com.amazonaws.services.lambda.runtime.CognitoIdentity getIdentity() {
                return null;
            }

            @Override
            public com.amazonaws.services.lambda.runtime.ClientContext getClientContext() {
                return null;
            }

            @Override
            public int getRemainingTimeInMillis() {
                return 30000;
            }

            @Override
            public int getMemoryLimitInMB() {
                return 512;
            }

            @Override
            public LambdaLogger getLogger() {
                return new LambdaLogger() {
                    @Override
                    public void log(String message) {
                        System.out.println("TEST LOG: " + message);
                    }

                    @Override
                    public void log(byte[] message) {
                        System.out.println("TEST LOG: " + new String(message));
                    }
                };
            }
        };
    }

    /**
     * Test Lambda handler with custom name
     */
    @Test
    public void testHandleRequestWithCustomName() {
        Map<String, Object> input = new HashMap<>();
        input.put("name", "AWS Lambda");

        Map<String, Object> result = app.handleRequest(input, mockContext);

        assertNotNull(result);
        assertEquals(200, result.get("statusCode"));
        assertEquals("Hello, AWS Lambda!", result.get("message"));
        assertEquals(input, result.get("input"));
        assertNotNull(result.get("timestamp"));
    }

    /**
     * Test Lambda handler with default name
     */
    @Test
    public void testHandleRequestWithDefaultName() {
        Map<String, Object> input = new HashMap<>();

        Map<String, Object> result = app.handleRequest(input, mockContext);

        assertNotNull(result);
        assertEquals(200, result.get("statusCode"));
        assertEquals("Hello, World!", result.get("message"));
        assertEquals(input, result.get("input"));
        assertNotNull(result.get("timestamp"));
    }

    /**
     * Test Lambda handler with empty input
     */
    @Test
    public void testHandleRequestWithEmptyInput() {
        Map<String, Object> input = new HashMap<>();

        Map<String, Object> result = app.handleRequest(input, mockContext);

        assertNotNull(result);
        assertEquals(200, result.get("statusCode"));
        assertTrue(result.get("message").toString().contains("Hello, World!"));
    }

    /**
     * Test Lambda handler with null name
     */
    @Test
    public void testHandleRequestWithNullName() {
        Map<String, Object> input = new HashMap<>();
        input.put("name", null);

        Map<String, Object> result = app.handleRequest(input, mockContext);

        assertNotNull(result);
        assertEquals(200, result.get("statusCode"));
        assertEquals("Hello, World!", result.get("message"));
    }

    /**
     * Test timestamp is recent
     */
    @Test
    public void testTimestampIsRecent() {
        Map<String, Object> input = new HashMap<>();
        long beforeCall = System.currentTimeMillis();

        Map<String, Object> result = app.handleRequest(input, mockContext);

        long afterCall = System.currentTimeMillis();
        long timestamp = (Long) result.get("timestamp");

        assertTrue(timestamp >= beforeCall);
        assertTrue(timestamp <= afterCall);
    }
}
