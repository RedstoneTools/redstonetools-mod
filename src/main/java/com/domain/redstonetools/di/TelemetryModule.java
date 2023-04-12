package com.domain.redstonetools.di;

import com.domain.redstonetools.telemetry.TelemetryClient;
import com.google.inject.AbstractModule;

public class TelemetryModule extends AbstractModule {
    private static TelemetryClient telemetryClient;

    @Override
    protected void configure() {
        bind(TelemetryClient.class).toProvider(() -> {
            if (telemetryClient == null) {
                telemetryClient = new TelemetryClient();
            }

            return telemetryClient;
        });
    }
}
