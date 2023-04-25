package tools.redstone.redstonetools.di;

import tools.redstone.redstonetools.telemetry.TelemetryClient;
import com.google.inject.AbstractModule;
import tools.redstone.redstonetools.telemetry.TelemetryManager;

public class TelemetryModule extends AbstractModule {
    private static TelemetryClient telemetryClient;
    private static TelemetryManager telemetryManager;

    @Override
    protected void configure() {
        bind(TelemetryClient.class).toProvider(() -> {
            if (telemetryClient == null) {
                telemetryClient = new TelemetryClient();
            }

            return telemetryClient;
        });

        bind(TelemetryManager.class).toProvider(() -> {
            if (telemetryManager == null) {
                telemetryManager = new TelemetryManager();
            }

            return telemetryManager;
        });
    }
}
