package com.pbpoints.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Pbpoints.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    public String cronFixture;

    public String cronEventStatus;

    public String cronDataExport;

    public String getCronFixture() {
        return cronFixture;
    }

    public void setCronFixture(String cronFixture) {
        this.cronFixture = cronFixture;
    }

    public String getCronEventStatus() {
        return cronEventStatus;
    }

    public void setCronEventStatus(String cronEventStatus) {
        this.cronEventStatus = cronEventStatus;
    }

    public String getCronDataExport() {
        return cronDataExport;
    }

    public void setCronDataExport(String cronDataExport) {
        this.cronDataExport = cronDataExport;
    }

    @Override
    public String toString() {
        return (
            "ApplicationProperties{" +
            "cronFixture='" +
            cronFixture +
            '\'' +
            ", cronEventStatus='" +
            cronEventStatus +
            '\'' +
            ", cronDataExport='" +
            cronDataExport +
            '\'' +
            '}'
        );
    }
}
