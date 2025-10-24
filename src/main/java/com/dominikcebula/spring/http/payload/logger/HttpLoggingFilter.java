package com.dominikcebula.spring.http.payload.logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpLoggingFilter extends OncePerRequestFilter {

    private static final Log log = LogFactory.getLog(HttpLoggingFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        if (!log.isDebugEnabled()) {
            filterChain.doFilter(request, response);
            return;
        }

        ContentCachingRequestWrapper wrappedRequest = getContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = getContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();
        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            try {
                long durationMs = System.currentTimeMillis() - startTime;
                String urlWithQuery = getUrlWithQuery(request.getQueryString(), request.getRequestURI());

                logRequest(urlWithQuery, wrappedRequest);
                logResponse(urlWithQuery, wrappedRequest, wrappedResponse, durationMs);

                wrappedResponse.copyBodyToResponse();
            } catch (Exception e) {
                log.warn("Failed to log HTTP request/response", e);
            }
        }
    }

    private ContentCachingRequestWrapper getContentCachingRequestWrapper(HttpServletRequest request) {
        return (request instanceof ContentCachingRequestWrapper)
                ? (ContentCachingRequestWrapper) request
                : new ContentCachingRequestWrapper(request);
    }

    private ContentCachingResponseWrapper getContentCachingResponseWrapper(HttpServletResponse response) {
        return (response instanceof ContentCachingResponseWrapper)
                ? (ContentCachingResponseWrapper) response
                : new ContentCachingResponseWrapper(response);
    }

    private void logRequest(String urlWithQuery, ContentCachingRequestWrapper request) throws IOException {
        Map<String, String> headers = extractHeaders(request);
        String requestBody = getRequestBody(request);

        log.debug("=== HTTP REQUEST - BEGIN =====================");
        log.debug("Method: " + request.getMethod());
        log.debug("URL: " + urlWithQuery);
        log.debug("Headers: " + headers);
        log.debug("Body: " + requestBody);
        log.debug("=== HTTP REQUEST - END =======================");
    }

    private void logResponse(String urlWithQuery, ContentCachingRequestWrapper request,
                             ContentCachingResponseWrapper response,
                             long durationMs) throws IOException {
        Map<String, String> headers = extractHeaders(response);
        String responseBody = getResponseBody(response);

        log.debug("=== HTTP RESPONSE - BEGIN =====================");
        log.debug("Status: " + response.getStatus());
        log.debug("Method: " + request.getMethod());
        log.debug("URL: " + urlWithQuery);
        log.debug("DurationMs: " + durationMs);
        log.debug("Headers: " + headers);
        log.debug("Body: " + responseBody);
        log.debug("=== HTTP RESPONSE - END =======================");
    }

    private String getUrlWithQuery(String query, String uri) {
        return (query == null) ? uri : (uri + "?" + query);
    }

    private Map<String, String> extractHeaders(HttpServletRequest request) {
        Map<String, String> headers = new LinkedHashMap<>();

        Enumeration<String> names = request.getHeaderNames();
        if (names != null) {
            for (String name : Collections.list(names)) {
                headers.put(name, request.getHeader(name));
            }
        }

        return headers;
    }

    private Map<String, String> extractHeaders(HttpServletResponse response) {
        Map<String, String> headers = new LinkedHashMap<>();

        for (String name : response.getHeaderNames()) {
            headers.put(name, response.getHeader(name));
        }

        return headers;
    }

    private String getRequestBody(ContentCachingRequestWrapper request) throws IOException {
        byte[] buf = request.getContentAsByteArray();
        if (buf.length == 0) {
            return "";
        }

        return new String(buf, getCharsetFromContentType(request.getContentType()));
    }

    private String getResponseBody(ContentCachingResponseWrapper response) throws IOException {
        byte[] buf = response.getContentAsByteArray();
        if (buf.length == 0) {
            return "";
        }

        return new String(buf, getCharsetFromContentType(response.getContentType()));
    }

    private Charset getCharsetFromContentType(String contentTypeHeader) {
        final Charset defaultCharset = StandardCharsets.UTF_8;

        if (contentTypeHeader == null) {
            return defaultCharset;
        }

        try {
            String[] parts = contentTypeHeader.split(";");
            for (String part : parts) {
                String lowerCasePart = part.trim().toLowerCase();
                if (lowerCasePart.startsWith("charset=")) {
                    String charsetName = lowerCasePart.substring("charset=".length()).trim();

                    return loadCharsetOrFallBack(charsetName, defaultCharset);
                }
            }
            return defaultCharset;
        } catch (Exception e) {
            log.warn("Failed to parse charset from content type header: " + contentTypeHeader, e);
            return defaultCharset;
        }
    }

    private Charset loadCharsetOrFallBack(String charsetName, Charset defaultCharset) {
        try {
            return Charset.forName(charsetName);
        } catch (Exception e) {
            log.warn("Unsupported charset: " + charsetName + ", falling back to default: " + defaultCharset, e);
            return defaultCharset;
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return false;
    }
}
