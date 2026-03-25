package info.jab.ms;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

/**
 * Main application class for the God Analysis API.
 */
@QuarkusMain
public class GodAnalysisApplication {

    public static void main(String... args) {
        Quarkus.run(args);
    }
}