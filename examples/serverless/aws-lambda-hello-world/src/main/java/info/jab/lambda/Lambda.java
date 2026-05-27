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
}
