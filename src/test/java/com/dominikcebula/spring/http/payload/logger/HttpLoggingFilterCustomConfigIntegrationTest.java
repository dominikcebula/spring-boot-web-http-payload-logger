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
@ActiveProfiles({"custom-config"})
public class HttpLoggingFilterCustomConfigIntegrationTest {
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
    void shouldLogRequestAndResponseForGetWhenErrorResponseProduced() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Test", "test-header-value");

        ResponseEntity<String> response = restTemplate.exchange("/api/ping/5xx", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo("pong");

        appender.assertContains("=== HTTP REQUEST - BEGIN =====================");
        appender.assertContains("Request Method: GET");
        appender.assertContains("Request URL: /api/ping/5xx");
        appender.assertContains("Request Headers:");
        appender.assertContains("x-test=test-header-value");
        appender.assertContains("Request Body: ");
        appender.assertContains("=== HTTP REQUEST - END =======================");

        appender.assertContains("=== HTTP RESPONSE - BEGIN =====================");
        appender.assertContains("Response Status: 500");
        appender.assertContains("Response Method: GET");
        appender.assertContains("Response URL: /api/ping/5xx");
        appender.assertContains("Response DurationMs:");
        appender.assertContains("Response Headers:");
        appender.assertContains("Sample-Header-01=Sample-Value-01, Sample-Header-02=Sample-Value-02, Sample-Header-03=Sample-Value-03");
        appender.assertContains("Response Body: pong");
        appender.assertContains("=== HTTP RESPONSE - END =======================");
    }

    @Test
    void shouldLogRequestAndResponseForGetWhenSuccessResponseProduced() {
        ResponseEntity<String> response = restTemplate.exchange("/api/ping/2xx", HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("pong");

        appender.assertContains("=== HTTP REQUEST - BEGIN =====================");
        appender.assertContains("Request Method: GET");
        appender.assertContains("Request URL: /api/ping/2xx");
        appender.assertContains("Request Headers:");
        appender.assertContains("Request Body: ");
        appender.assertContains("=== HTTP REQUEST - END =======================");

        appender.assertContains("=== HTTP RESPONSE - BEGIN =====================");
        appender.assertContains("Response Status: 200");
        appender.assertContains("Response Method: GET");
        appender.assertContains("Response URL: /api/ping/2xx");
        appender.assertContains("Response DurationMs:");
        appender.assertContains("Response Body: pong");
        appender.assertContains("=== HTTP RESPONSE - END =======================");
    }

    @Test
    void shouldLogRequestAndResponseForPostWithBodyWhenErrorResponseProduced() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        final String body = "hello world";

        ResponseEntity<String> response = restTemplate.postForEntity("/api/echo/5xx", new HttpEntity<>(body, headers), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo(body);

        appender.assertContains("=== HTTP REQUEST - BEGIN =====================");
        appender.assertContains("Request Method: POST");
        appender.assertContains("Request URL: /api/echo/5xx");
        appender.assertContains("Request Headers:");
        appender.assertContains("accept=text/plain, application/json");
        appender.assertContains("Request Body: " + body);
        appender.assertContains("=== HTTP REQUEST - END =======================");

        appender.assertContains("=== HTTP RESPONSE - BEGIN =====================");
        appender.assertContains("Response Status: 500");
        appender.assertContains("Response Method: POST");
        appender.assertContains("Response URL: /api/echo/5xx");
        appender.assertContains("Response DurationMs:");
        appender.assertContains("Response Headers:");
        appender.assertContains("Response Body: " + body);
        appender.assertContains("=== HTTP RESPONSE - END =======================");
    }

    @Test
    void shouldLogRequestAndResponseForPostWithBodyWhenSuccessResponseProduced() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        final String body = "hello world";

        ResponseEntity<String> response = restTemplate.postForEntity("/api/echo/2xx", new HttpEntity<>(body, headers), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(body);

        appender.assertContains("=== HTTP REQUEST - BEGIN =====================");
        appender.assertContains("Request Method: POST");
        appender.assertContains("Request URL: /api/echo/2xx");
        appender.assertContains("Request Headers:");
        appender.assertContains("Request Body: " + body);
        appender.assertContains("=== HTTP REQUEST - END =======================");

        appender.assertContains("=== HTTP RESPONSE - BEGIN =====================");
        appender.assertContains("Response Status: 200");
        appender.assertContains("Response Method: POST");
        appender.assertContains("Response URL: /api/echo/2xx");
        appender.assertContains("Response DurationMs:");
        appender.assertContains("Response Headers:");
        appender.assertContains("Response Body: " + body);
        appender.assertContains("=== HTTP RESPONSE - END =======================");
    }

    @Test
    void shouldLogRequestAndResponseForGetWithUrlParametersWhenErrorResponseProduced() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Test", "test-header-value");

        ResponseEntity<String> response = restTemplate.exchange("/api/ping/5xx?param1=value1&param2=value2&param3=value3", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo("pong");

        appender.assertContains("=== HTTP REQUEST - BEGIN =====================");
        appender.assertContains("Request Method: GET");
        appender.assertContains("Request URL: /api/ping/5xx?param1=value1&param2=value2&param3=value3");
        appender.assertContains("Request Headers:");
        appender.assertContains("x-test=test-header-value");
        appender.assertContains("Request Body: ");
        appender.assertContains("=== HTTP REQUEST - END =======================");

        appender.assertContains("=== HTTP RESPONSE - BEGIN =====================");
        appender.assertContains("Response Status: 500");
        appender.assertContains("Response Method: GET");
        appender.assertContains("Response URL: /api/ping/5xx?param1=value1&param2=value2&param3=value3");
        appender.assertContains("Response DurationMs:");
        appender.assertContains("Response Headers:");
        appender.assertContains("Sample-Header-01=Sample-Value-01, Sample-Header-02=Sample-Value-02, Sample-Header-03=Sample-Value-03");
        appender.assertContains("Response Body: pong");
        appender.assertContains("=== HTTP RESPONSE - END =======================");
    }
}
