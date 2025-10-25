package com.dominikcebula.spring.http.payload.logger;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AutoConfiguration
public class HttpLoggingFilterAutoConfiguration {
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(name = "logging.payload.logger.http.enabled", havingValue = "true", matchIfMissing = true)
    public static class HttpLoggingFilterConfiguration {
        @Bean
        public FilterRegistrationBean<HttpLoggingFilter> httpLoggingFilterRegistration() {
            FilterRegistrationBean<HttpLoggingFilter> registrationBean = new FilterRegistrationBean<>();
            registrationBean.setFilter(new HttpLoggingFilter());
            registrationBean.setOrder(Integer.MIN_VALUE);
            return registrationBean;
        }
    }
}
