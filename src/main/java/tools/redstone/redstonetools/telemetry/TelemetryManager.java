package tools.redstone.redstonetools.telemetry;

import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Singleton
public class TelemetryManager {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Path telemetryFilePath;
    private TelemetryConfig config;

    public TelemetryManager() {
        config = TelemetryConfig.DEFAULT;
        try {
            telemetryFilePath = MinecraftClient.getInstance().runDirectory.toPath()
                    .resolve("config")
                    .resolve("redstonetools")
                    .resolve("telemetry.json");

            loadSettingsFromJson();
        } catch (Throwable t) {
            throw new RuntimeException("Coudln't load telemetry config", t);
        }
    }

    private void loadSettingsFromJson() throws IOException {
        Files.createDirectories(telemetryFilePath.getParent());
        if (!Files.exists(telemetryFilePath)) {
            saveChanges();
            return;
        }

        config = TelemetryConfig.from(telemetryFilePath);
        LOGGER.trace("Loaded telemetry config: {}", config);
    }

    public void saveChanges() {
        // Write %appdata%/.minecraft/config/redstonetools/telemetry.json
        try {
            Files.createDirectories(telemetryFilePath.getParent());
        } catch (IOException e) {
            LOGGER.warn("Failed to create RedstoneTools config directory", e);
        }

        try {
            config.write(telemetryFilePath);
        } catch (IOException e) {
            LOGGER.warn("Failed to write telemetry config", e);
        }
    }

    public TelemetryConfig getConfig() {
        return config;
    }
}
