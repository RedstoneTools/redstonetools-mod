package com.domain.redstonetools.di;

import com.domain.redstonetools.telemetry.TelemetryClient;
import com.google.inject.AbstractModule;

public class TelemetryModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(TelemetryClient.class).asEagerSingleton();
    }
}
