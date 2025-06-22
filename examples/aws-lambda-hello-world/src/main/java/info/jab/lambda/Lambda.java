package info.jab.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * AWS Lambda Hello World Handler
 */
public class Lambda implements RequestHandler<Map<String, Object>, Map<String, Object>> {

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Function name: " + context.getFunctionName());
        logger.log("Function version: " + context.getFunctionVersion());
        logger.log("Remaining time: " + context.getRemainingTimeInMillis());

        // Get the name from input, default to "World"
        Object nameObj = input.getOrDefault("name", "World");
        String name = Objects.isNull(nameObj) ? "World" : String.valueOf(nameObj);

        // Create response
        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", 200);
        response.put("message", "Hello, " + name + "!");
        response.put("input", input);
        response.put("timestamp", System.currentTimeMillis());

        logger.log("Response: " + response.toString());

        return response;
    }

    // Keep the main method for local testing
    public static void main(String[] args) {
        Lambda app = new Lambda();

        // Create a simple test input
        Map<String, Object> testInput = new HashMap<>();
        testInput.put("name", "Local Test");

        // Create a mock context for local testing
        Context mockContext = new Context() {
            @Override
            public String getAwsRequestId() {
                return "local-test-request-id";
            }

            @Override
            public String getLogGroupName() {
                return "local-test-log-group";
            }

            @Override
            public String getLogStreamName() {
                return "local-test-log-stream";
            }

            @Override
            public String getFunctionName() {
                return "HelloWorldFunction";
            }

            @Override
            public String getFunctionVersion() {
                return "1.0";
            }

            @Override
            public String getInvokedFunctionArn() {
                return "arn:aws:lambda:us-east-1:123456789012:function:HelloWorldFunction";
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
                        System.out.println("LOG: " + message);
                    }

                    @Override
                    public void log(byte[] message) {
                        System.out.println("LOG: " + new String(message));
                    }
                };
            }
        };

        Map<String, Object> result = app.handleRequest(testInput, mockContext);
        System.out.println("Result: " + result);
    }
}
