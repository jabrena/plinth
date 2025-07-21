package info.jab.ms;

import jakarta.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;

//@RestController
//@RequestMapping("/api/v1")
public class WithoutCocoController {

    private static final Logger logger = LoggerFactory.getLogger(WithoutCocoController.class);

    private record MyPojo(String message) { }

    // Bounded Collections
    private static final int MAX_OBJECTS = 10000;
    private final List<MyPojo> objects = Collections.synchronizedList(new ArrayList<>());

    // Reuse thread pool instead of creating new ones
    private final ExecutorService sharedExecutorService =
        Executors.newFixedThreadPool(10, new CustomizableThreadFactory("shared-pool-"));

    @PreDestroy
    public void cleanup() throws InterruptedException {
        sharedExecutorService.shutdown();
        if (!sharedExecutorService.awaitTermination(30, TimeUnit.SECONDS)) {
            sharedExecutorService.shutdownNow();
        }
    }

    private static final Supplier<String> largeMessageSupplier =
        () -> "lorem ipsum dolor sit amet ".repeat(50);

    @GetMapping("/objects/create")
    public ResponseEntity<String> getObjectsParty() {
        logger.info("Starting object creation endpoint.");

        // Auto-reset when reaching limit to avoid errors
        if (objects.size() >= MAX_OBJECTS) {
            logger.info("Objects limit reached ({}), clearing collection for continued operation", MAX_OBJECTS);
            objects.clear();
        }

        String message = largeMessageSupplier.get();
        IntStream.rangeClosed(0, 1000)
            .mapToObj(i -> new MyPojo(message))
            .forEach(objects::add);

        return ResponseEntity.ok("Don't touch me even with a stick!");
    }

    @GetMapping("/threads/create")
    public ResponseEntity<String> getThreadsParty() {
        logger.info("Starting thread creation endpoint");

        IntStream.rangeClosed(0, 10)
            .forEach(i -> sharedExecutorService.execute(() -> {
                // Empty runnable task - demonstrates the memory leak
            }));
        return ResponseEntity.ok("Don't touch me even with a stick!");
    }
}
