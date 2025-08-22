package tools.redstone.redstonetools.features.toggleable;

import fi.dy.masa.malilib.config.options.ConfigBoolean;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import tools.redstone.redstonetools.malilib.config.Configs;
import tools.redstone.redstonetools.packets.SetFeatureEnabledPayload;

public class AutoDustClient {
	public static ConfigBoolean isEnabled = Configs.Toggles.AUTODUST;

	public static void registerHandler() {
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			SetFeatureEnabledPayload payload = new SetFeatureEnabledPayload("AutoDust", AutoDustClient.isEnabled.getBooleanValue());
			ClientPlayNetworking.send(payload);
		});

		isEnabled.setValueChangeCallback((t) -> {
			SetFeatureEnabledPayload payload = new SetFeatureEnabledPayload("AutoDust", t.getBooleanValue());
			ClientPlayNetworking.send(payload);
		});
	}
}
