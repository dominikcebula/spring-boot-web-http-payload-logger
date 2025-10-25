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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(classes = SampleApplicationForTesting.class, webEnvironment = RANDOM_PORT)
public class HttpLoggingFilterEnabledIntegrationTest {
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
    void shouldLogRequestAndResponseForGet() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Test", "test-header-value");

        ResponseEntity<String> response = restTemplate.exchange("/api/ping", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("pong");

        appender.assertContains("=== HTTP REQUEST - BEGIN =====================");
        appender.assertContains("Method: GET");
        appender.assertContains("URL: /api/ping");
        appender.assertContains("Headers: {accept=text/plain, application/json, application/*+json, */*, x-test=test-header-value");
        appender.assertContains("Body: ");
        appender.assertContains("=== HTTP REQUEST - END =======================");

        appender.assertContains("=== HTTP RESPONSE - BEGIN =====================");
        appender.assertContains("Status: 200");
        appender.assertContains("Method: GET");
        appender.assertContains("URL: /api/ping");
        appender.assertContains("DurationMs:");
        appender.assertContains("Headers: {Sample-Header-01=Sample-Value-01, Sample-Header-02=Sample-Value-02, Sample-Header-03=Sample-Value-03}");
        appender.assertContains("Body: pong");
        appender.assertContains("=== HTTP RESPONSE - END =======================");
    }

    @Test
    void shouldLogRequestAndResponseForPostWithBody() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        final String body = "hello world";

        ResponseEntity<String> response = restTemplate.postForEntity("/api/echo", new HttpEntity<>(body, headers), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(body);

        appender.assertContains("=== HTTP REQUEST - BEGIN =====================");
        appender.assertContains("Method: POST");
        appender.assertContains("URL: /api/echo");
        appender.assertContains("Headers: {accept=text/plain, application/json");
        appender.assertContains("Body: " + body);
        appender.assertContains("=== HTTP REQUEST - END =======================");

        appender.assertContains("=== HTTP RESPONSE - BEGIN =====================");
        appender.assertContains("Status: 200");
        appender.assertContains("Method: POST");
        appender.assertContains("URL: /api/echo");
        appender.assertContains("DurationMs:");
        appender.assertContains("Headers: {}");
        appender.assertContains("Body: " + body);
        appender.assertContains("=== HTTP RESPONSE - END =======================");
    }

    @Test
    void shouldLogRequestAndResponseForGetWithUrlParameters() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Test", "test-header-value");

        ResponseEntity<String> response = restTemplate.exchange("/api/ping?param1=value1&param2=value2&param3=value3", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("pong");

        appender.assertContains("=== HTTP REQUEST - BEGIN =====================");
        appender.assertContains("Method: GET");
        appender.assertContains("URL: /api/ping?param1=value1&param2=value2&param3=value3");
        appender.assertContains("Headers: {accept=text/plain, application/json, application/*+json, */*, x-test=test-header-value");
        appender.assertContains("Body: ");
        appender.assertContains("=== HTTP REQUEST - END =======================");

        appender.assertContains("=== HTTP RESPONSE - BEGIN =====================");
        appender.assertContains("Status: 200");
        appender.assertContains("Method: GET");
        appender.assertContains("URL: /api/ping?param1=value1&param2=value2&param3=value3");
        appender.assertContains("DurationMs:");
        appender.assertContains("Headers: {Sample-Header-01=Sample-Value-01, Sample-Header-02=Sample-Value-02, Sample-Header-03=Sample-Value-03}");
        appender.assertContains("Body: pong");
        appender.assertContains("=== HTTP RESPONSE - END =======================");
    }
}
