package tools.redstone.redstonetools.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.MinecraftClient;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.features.commands.*;
import tools.redstone.redstonetools.features.toggleable.AirPlaceFeature;
import tools.redstone.redstonetools.features.toggleable.BigDustFeature;
import tools.redstone.redstonetools.malilib.config.MacroManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ClientFeatureUtils {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static Set<AbstractFeature> features;

	private ClientFeatureUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static <T> T getFeature(Class<T> clazz) {
		if (features == null) {
			Set<AbstractFeature> FEATURES = new HashSet<>();
			FEATURES.add(new BigDustFeature());
			FEATURES.add(new AirPlaceFeature());
			FEATURES.add(new MacroFeature());
			FEATURES.add(new BaseConvertFeature());
			features = FEATURES;
		}
		Optional<T> found1 = features.stream()
				.filter(clazz::isInstance)
				.map(clazz::cast)
				.findFirst();
		if (clazz == MacroManager.class && found1.isEmpty()) features.add(new MacroManager());
		Optional<T> found = features.stream()
				.filter(clazz::isInstance)
				.map(clazz::cast)
				.findFirst();
		if (found.isEmpty()) {
			System.out.println("Add " + clazz.getSimpleName() + " to ClientFeatureUtils");
			throw new RuntimeException();
		}
		return found.orElseThrow();
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

			AirPlaceFeature airplace = ClientFeatureUtils.getFeature(AirPlaceFeature.class);
			airplace.setEnabled(storedState.airPlace);
			AirPlaceFeature.reach = storedState.airPlaceReach;
			AirPlaceFeature.showOutline = storedState.airPlaceShowOutline;

			BigDustFeature bigdust = ClientFeatureUtils.getFeature(BigDustFeature.class);
			bigdust.setEnabled(storedState.bigDust);
			BigDustFeature.heightInPixels = storedState.bigDustHeightInPixels;

//			AutoDustFeature autodust = FeatureUtils.getFeature(AutoDustFeature.class);
//			autodust.setEnabled(storedState.autoDust);
		} catch (IOException e) {
			System.err.println("Error reading toggles: " + e.getMessage());
		}
	}

	public static void saveToggles() {
		var storedState = new StoredState();
		AirPlaceFeature airplace = ClientFeatureUtils.getFeature(AirPlaceFeature.class);
		storedState.airPlace = airplace.isEnabled();
		storedState.airPlaceReach = AirPlaceFeature.reach;
		storedState.airPlaceShowOutline = AirPlaceFeature.showOutline;

		BigDustFeature bigdust = ClientFeatureUtils.getFeature(BigDustFeature.class);
		storedState.bigDust = bigdust.isEnabled();
		storedState.bigDustHeightInPixels = BigDustFeature.heightInPixels;

//		AutoDustFeature autodust = FeatureUtils.getFeature(AutoDustFeature.class);
//		storedState.autoDust = autodust.isEnabled();

		try {
			Files.write(togglesFilePath, GSON.toJson(storedState).getBytes());
		} catch (IOException e) {
			System.err.println("Error saving toggles: " + e.getMessage());
		}
	}

	private static class StoredState {
		public boolean airPlace;
		public float   airPlaceReach;
		public boolean airPlaceShowOutline;

		public boolean bigDust;
		public int     bigDustHeightInPixels;

		public boolean autoDust;
	}
}
