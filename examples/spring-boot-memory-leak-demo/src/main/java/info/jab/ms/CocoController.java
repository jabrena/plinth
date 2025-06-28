package info.jab.ms;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
public class CocoController {

    private static final Logger logger = LoggerFactory.getLogger(CocoController.class);

    private record MyPojo(String message) { }

    private List<MyPojo> objects = new ArrayList<>();

    private static final Supplier<String> largeMessageSupplier =
        () -> "lorem ipsum dolor sit amet ".repeat(50);

    @GetMapping("/objects/create")
    public ResponseEntity<String> getObjectsParty() {
        logger.info("Starting object creation endpoint.");

        String message = largeMessageSupplier.get();
        IntStream.rangeClosed(0, 1000)
            .mapToObj(i -> new MyPojo(message))
            .forEach(objects::add);

        return ResponseEntity.ok("Don't touch me even with a stick!");
    }

    @GetMapping("/threads/create")
    public ResponseEntity<String> getThreadsParty() {
        logger.info("Starting thread creation endpoint");

        ExecutorService service = Executors.newFixedThreadPool(10, new CustomizableThreadFactory("findme-"));

        IntStream.rangeClosed(0, 10)
            .forEach(i -> service.execute(() -> {
                // Empty runnable task - demonstrates the memory leak
            }));
        return ResponseEntity.ok("Don't touch me even with a stick!");
    }
}
