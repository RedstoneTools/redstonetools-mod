package tools.redstone.redstonetools.features.toggleable;

import fi.dy.masa.malilib.config.options.ConfigBoolean;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import tools.redstone.redstonetools.config.Toggles;
import tools.redstone.redstonetools.packets.SetFeatureEnabledPayload;

public class ClickContainerClient {
	public static ConfigBoolean isEnabled = Toggles.CLICKCONTAINERS;

	public static void registerHandler() {
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			SetFeatureEnabledPayload payload = new SetFeatureEnabledPayload("ClickContainers", ClickContainerClient.isEnabled.getBooleanValue());
			ClientPlayNetworking.send(payload);
		});

		isEnabled.setValueChangeCallback((t) -> {
			if (Minecraft.getInstance().getConnection() != null) {
				SetFeatureEnabledPayload payload = new SetFeatureEnabledPayload("ClickContainers", t.getBooleanValue());
				ClientPlayNetworking.send(payload);
			}
		});
	}
}
