package com.dominikcebula.spring.http.payload.logger.utils;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryLogAppender extends AppenderBase<ILoggingEvent> {

    private final List<ILoggingEvent> events = new LinkedList<>();

    public static InMemoryLogAppender setUp() {
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        InMemoryLogAppender appender = new InMemoryLogAppender();
        appender.setContext(root.getLoggerContext());
        appender.start();
        root.addAppender(appender);
        return appender;
    }

    public static void tearDown(InMemoryLogAppender appender) {
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.detachAppender(appender);
        appender.stop();
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        events.add(eventObject);
    }

    public void assertContains(String substring) {
        assertThat(getMessages())
                .anyMatch(m -> m.contains(substring));
    }

    public void assertDoesNotContain(String substring) {
        assertThat(getMessages())
                .noneMatch(m -> m.contains(substring));
    }

    private List<String> getMessages() {
        return events.stream()
                .map(ILoggingEvent::getFormattedMessage)
                .toList();
    }
}

