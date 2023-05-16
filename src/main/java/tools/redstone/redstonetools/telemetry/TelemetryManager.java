package tools.redstone.redstonetools.telemetry;

import net.minecraft.client.MinecraftClient;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TelemetryManager {
    private final Path telemetryFilePath;
    public boolean telemetryEnabled = false;
    public boolean showTelemetryPrompt = true;

    public TelemetryManager() {
        telemetryFilePath = MinecraftClient.getInstance().runDirectory.toPath()
                .resolve("config")
                .resolve("redstonetools")
                .resolve("telemetry.json");

        JsonObject telemetryJson = null;
        try {
            Files.createDirectories(telemetryFilePath.getParent());
            if (Files.exists(telemetryFilePath)) {
                var reader = Json.createReader(new FileReader(telemetryFilePath.toFile()));
                telemetryJson = reader.readObject();
                reader.close();

                loadSettingsFromJson(telemetryJson);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSettingsFromJson(JsonObject json) {
        telemetryEnabled = json.getBoolean("telemetryEnabled");
        showTelemetryPrompt = json.getBoolean("showTelemetryPrompt");

        saveChanges();
    }

    public void saveChanges() {
        // Write %appdata%/.minecraft/config/redstonetools/telemetry.json
        try {
            Files.createDirectories(telemetryFilePath.getParent());
        } catch (IOException e) {
            e.printStackTrace();
        }

        var telemetryJson = Json.createObjectBuilder()
                .add("telemetryEnabled", telemetryEnabled)
                .add("showTelemetryPrompt", showTelemetryPrompt)
                .build();

        try (var writer = Json.createWriter(new FileWriter(telemetryFilePath.toFile()))) {
            writer.writeObject(telemetryJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
