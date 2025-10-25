package com.dominikcebula.spring.http.payload.logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

public class HttpLoggingFilterDefaultLoggingLevel implements EnvironmentPostProcessor {
    private static final String LOGGING_LEVEL_PROPERTY = "logging.level.payload.logger.http";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment,
                                       SpringApplication application) {
        if (!environment.containsProperty(LOGGING_LEVEL_PROPERTY)) {
            Map<String, Object> props = new HashMap<>();
            props.put(LOGGING_LEVEL_PROPERTY, "DEBUG");

            MapPropertySource source = new MapPropertySource("spring-web-http-payload-logger-logging-defaults", props);

            environment.getPropertySources().addLast(source);
        }
    }
}
