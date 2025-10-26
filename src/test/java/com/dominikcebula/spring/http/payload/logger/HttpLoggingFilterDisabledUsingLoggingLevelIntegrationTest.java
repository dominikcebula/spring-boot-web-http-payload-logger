package com.dominikcebula.spring.http.payload.logger;

import com.dominikcebula.spring.http.payload.logger.utils.InMemoryLogAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(classes = SampleApplicationForTesting.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles({"filter-logger-disabled-using-logging-level"})
public class HttpLoggingFilterDisabledUsingLoggingLevelIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private InMemoryLogAppender appender;

    @BeforeEach
    void setUp() {
        appender = InMemoryLogAppender.setUp();
    }

    @AfterEach
    void tearDown() {
        InMemoryLogAppender.tearDown(appender);
    }

    @Test
    void shouldNotLogRequestOrResponseWhenLoggingFilterIsDisabledUsingLoggingLevel() {
        ResponseEntity<String> response = restTemplate.exchange("/api/ping/5xx", HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo("pong");

        appender.assertDoesNotContain("=== HTTP REQUEST - BEGIN =====================");
        appender.assertDoesNotContain("=== HTTP REQUEST - END =======================");

        appender.assertDoesNotContain("=== HTTP RESPONSE - BEGIN =====================");
        appender.assertDoesNotContain("=== HTTP RESPONSE - END =======================");
    }
}
