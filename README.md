[![CI Pipeline](https://github.com/dominikcebula/spring-web-http-payload-logger/actions/workflows/maven.yml/badge.svg)](https://github.com/dominikcebula/spring-web-http-payload-logger/actions/workflows/maven.yml)

# 📦 Spring Web HTTP Payload Logger

## ℹ️ Introduction

Spring Web HTTP Payload Logger is a reusable library that enables logging of HTTP request and response payloads in
Spring Web applications. It provides an easy way to monitor and debug HTTP communications by capturing and logging the
payloads of incoming requests and outgoing responses.

After integrating this library into your Spring Web application, you will be able to see the HTTP request and response
in the following format:

```text
c.d.s.h.p.logger.HttpLoggingFilter       : === HTTP REQUEST - BEGIN =====================
c.d.s.h.p.logger.HttpLoggingFilter       : Method: GET
c.d.s.h.p.logger.HttpLoggingFilter       : URL: /api/ping?param1=value1&param2=value2&param3=value3
c.d.s.h.p.logger.HttpLoggingFilter       : Headers: {accept=text/plain, application/json, application/*+json, */*, x-test=test-header-value, user-agent=Java/17.0.15, host=localhost:34491, connection=keep-alive}
c.d.s.h.p.logger.HttpLoggingFilter       : Body: hello world
c.d.s.h.p.logger.HttpLoggingFilter       : === HTTP REQUEST - END =======================

c.d.s.h.p.logger.HttpLoggingFilter       : === HTTP RESPONSE - BEGIN =====================
c.d.s.h.p.logger.HttpLoggingFilter       : Status: 200
c.d.s.h.p.logger.HttpLoggingFilter       : Method: GET
c.d.s.h.p.logger.HttpLoggingFilter       : URL: /api/ping?param1=value1&param2=value2&param3=value3
c.d.s.h.p.logger.HttpLoggingFilter       : DurationMs: 15
c.d.s.h.p.logger.HttpLoggingFilter       : Headers: {Sample-Header-01=Sample-Value-01, Sample-Header-02=Sample-Value-02, Sample-Header-03=Sample-Value-03}
c.d.s.h.p.logger.HttpLoggingFilter       : Body: hello world
c.d.s.h.p.logger.HttpLoggingFilter       : === HTTP RESPONSE - END =======================
```

## 🚀 Usage

To use this library in your Spring Web application, add the dependency to your `pom.xml`:

```xml

<dependency>
    <groupId>com.dominikcebula.spring</groupId>
    <artifactId>spring-web-http-payload-logger</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

You do not need any additional configuration. The library will automatically self-configure to log HTTP request and
response payloads.

## ✍️ Author

Dominik Cebula

- https://dominikcebula.com/
- https://blog.dominikcebula.com/
- https://www.udemy.com/user/dominik-cebula/
