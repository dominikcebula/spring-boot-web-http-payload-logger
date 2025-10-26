package com.dominikcebula.spring.http.payload.logger;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;

@ConfigurationProperties(prefix = "logging.payload.logger.http.config")
public class HttpLoggingFilterProperties {
    private boolean log1xxInformationalStatus = false;
    private boolean log2xxSuccessfulStatus = false;
    private boolean log3xxRedirectionStatus = false;
    private boolean log4xxClientErrorStatus = false;
    private boolean log5xxServerErrorStatus = true;

    public boolean isLoggingEnabledForStatus(HttpStatus status) {
        return status != null && (
                (status.is1xxInformational() && log1xxInformationalStatus)
                        || (status.is2xxSuccessful() && log2xxSuccessfulStatus)
                        || (status.is3xxRedirection() && log3xxRedirectionStatus)
                        || (status.is4xxClientError() && log4xxClientErrorStatus)
                        || (status.is5xxServerError() && log5xxServerErrorStatus));
    }

    public void setLog1xxInformationalStatus(boolean log1xxInformationalStatus) {
        this.log1xxInformationalStatus = log1xxInformationalStatus;
    }

    public void setLog2xxSuccessfulStatus(boolean log2xxSuccessfulStatus) {
        this.log2xxSuccessfulStatus = log2xxSuccessfulStatus;
    }

    public void setLog3xxRedirectionStatus(boolean log3xxRedirectionStatus) {
        this.log3xxRedirectionStatus = log3xxRedirectionStatus;
    }

    public void setLog4xxClientErrorStatus(boolean log4xxClientErrorStatus) {
        this.log4xxClientErrorStatus = log4xxClientErrorStatus;
    }

    public void setLog5xxServerErrorStatus(boolean log5xxServerErrorStatus) {
        this.log5xxServerErrorStatus = log5xxServerErrorStatus;
    }
}
