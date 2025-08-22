package tools.redstone.redstonetools.features.toggleable;

import fi.dy.masa.malilib.config.options.ConfigBoolean;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import tools.redstone.redstonetools.malilib.config.Configs;
import tools.redstone.redstonetools.packets.SetFeatureEnabledPayload;

public class AutoRotateClient {
	public static ConfigBoolean isEnabled = Configs.Toggles.AUTOROTATE;

	public static void registerHandler() {
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			SetFeatureEnabledPayload payload = new SetFeatureEnabledPayload("AutoRotate", AutoRotateClient.isEnabled.getBooleanValue());
			ClientPlayNetworking.send(payload);
		});

		isEnabled.setValueChangeCallback((t) -> {
			System.out.println(t.getBooleanValue());
			SetFeatureEnabledPayload payload = new SetFeatureEnabledPayload("AutoRotate", t.getBooleanValue());
			ClientPlayNetworking.send(payload);
		});
	}
}
