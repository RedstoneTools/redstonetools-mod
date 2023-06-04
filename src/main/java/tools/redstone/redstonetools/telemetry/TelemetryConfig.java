package tools.redstone.redstonetools.telemetry;

import tools.redstone.redstonetools.RedstoneToolsClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TelemetryConfig {
    public static final TelemetryConfig DEFAULT =
            new TelemetryConfig(false, true);

    public boolean telemetryEnabled;
    public boolean showTelemetryPrompt;

    public TelemetryConfig(boolean telemetryEnabled, boolean showTelemetryPrompt) {
        this.telemetryEnabled = telemetryEnabled;
        this.showTelemetryPrompt = showTelemetryPrompt;
    }

    static TelemetryConfig from(Path path) throws IOException {
        TelemetryConfig config = RedstoneToolsClient.GSON.fromJson(
                Files.newBufferedReader(path),
                TelemetryConfig.class
        );
        return config == null ? DEFAULT : config;
    }

    public void write(Path telemetryFilePath) throws IOException {
        System.out.println("Writing config");
        Files.writeString(telemetryFilePath, RedstoneToolsClient.GSON.toJson(this));
    }

    @Override
    public String toString() {
        return "TelemetryConfig{" +
                "telemetryEnabled=" + telemetryEnabled +
                ", showTelemetryPrompt=" + showTelemetryPrompt +
                '}';
    }
}
