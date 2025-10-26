package com.dominikcebula.spring.http.payload.logger;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("/api")
public class SampleControllerForTesting {

    @GetMapping("/ping/5xx")
    public ResponseEntity<String> ping() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Sample-Header-01", "Sample-Value-01");
        responseHeaders.set("Sample-Header-02", "Sample-Value-02");
        responseHeaders.set("Sample-Header-03", "Sample-Value-03");

        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .headers(responseHeaders)
                .body("pong");
    }

    @PostMapping("/echo/5xx")
    public ResponseEntity<String> echo(@RequestBody String body) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(body);
    }
}
