package tools.redstone.redstonetools.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.MinecraftClient;
import tools.redstone.redstonetools.features.toggleable.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigUtils {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private ConfigUtils() {
		throw new IllegalStateException("Utility class");
	}

	private static Path togglesFilePath;

	public static void readToggles() {
		togglesFilePath = MinecraftClient.getInstance().runDirectory.toPath()
				.resolve("config")
				.resolve("redstonetools")
				.resolve("toggles.json");
		try {
			byte[] bytes = Files.readAllBytes(togglesFilePath);

			String fileContents = new String(bytes);
			var storedState = GSON.fromJson(fileContents, StoredState.class);

			AirPlaceFeature.INSTANCE.setEnabled(storedState.airPlace);
			BigDustFeature.INSTANCE.setEnabled(storedState.bigDust);
			AutoDustClient.isEnabled.setBooleanValue(storedState.autoDust);
			AutoRotateClient.isEnabled.setBooleanValue(storedState.autoRotate);
			ClickContainerClient.isEnabled.setBooleanValue(storedState.clickContainer);

		} catch (IOException e) {
			System.err.println("Error reading toggles: " + e.getMessage());
		}

		AutoDustClient.registerHandler();
		AutoRotateClient.registerHandler();
		ClickContainerClient.registerHandler();
	}

	public static void saveToggles() {
		var storedState = new StoredState();

		storedState.airPlace = AirPlaceFeature.INSTANCE.isEnabled();
		storedState.bigDust = BigDustFeature.INSTANCE.isEnabled();
		storedState.autoDust = AutoDustClient.isEnabled.getBooleanValue();
		storedState.autoRotate = AutoRotateClient.isEnabled.getBooleanValue();
		storedState.clickContainer = ClickContainerClient.isEnabled.getBooleanValue();

		try {
			Files.write(togglesFilePath, GSON.toJson(storedState).getBytes());
		} catch (IOException e) {
			System.err.println("Error saving toggles: " + e.getMessage());
		}
	}

	private static class StoredState {
		public boolean airPlace;
		public boolean bigDust;
		public boolean autoDust;
		public boolean autoRotate;
		public boolean clickContainer;
	}
}
