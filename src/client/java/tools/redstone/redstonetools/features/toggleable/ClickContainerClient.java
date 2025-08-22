package tools.redstone.redstonetools.features.toggleable;

import fi.dy.masa.malilib.config.options.ConfigBoolean;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import tools.redstone.redstonetools.malilib.config.Configs;
import tools.redstone.redstonetools.packets.SetFeatureEnabledPayload;

public class ClickContainerClient {
	public static ConfigBoolean isEnabled = Configs.Toggles.CLICKCONTAINERS;

	public static void registerHandler() {
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			SetFeatureEnabledPayload payload = new SetFeatureEnabledPayload("ClickContainers", ClickContainerClient.isEnabled.getBooleanValue());
			ClientPlayNetworking.send(payload);
		});

		isEnabled.setValueChangeCallback((t) -> {
			System.out.println(t.getBooleanValue());
			SetFeatureEnabledPayload payload = new SetFeatureEnabledPayload("ClickContainers", t.getBooleanValue());
			ClientPlayNetworking.send(payload);
		});
	}
}
