package com.dominikcebula.spring.http.payload.logger;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SampleControllerForTesting {

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Sample-Header-01", "Sample-Value-01");
        responseHeaders.set("Sample-Header-02", "Sample-Value-02");
        responseHeaders.set("Sample-Header-03", "Sample-Value-03");

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body("pong");
    }

    @PostMapping("/echo")
    public ResponseEntity<String> echo(@RequestBody String body) {
        return ResponseEntity.ok(body);
    }
}
