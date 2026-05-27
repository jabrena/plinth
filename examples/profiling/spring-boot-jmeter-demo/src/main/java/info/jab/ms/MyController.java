package info.jab.ms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class MyController {
    
    // Rule 3.1: Use static final Logger instance per class, obtained via LoggerFactory.getLogger(ClassName.class)
    private static final Logger logger = LoggerFactory.getLogger(MyController.class);
    
    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        logger.info("Processing hello request");
        return ResponseEntity.ok("Hello, World!");
    }
}
