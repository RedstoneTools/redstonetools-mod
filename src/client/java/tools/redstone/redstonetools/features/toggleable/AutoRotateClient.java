package tools.redstone.redstonetools.features.toggleable;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import tools.redstone.redstonetools.packets.SetFeatureEnabledPayload;

public class AutoRotateClient {
	public static boolean isEnabled;

	public static void registerHandler() {
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			SetFeatureEnabledPayload payload = new SetFeatureEnabledPayload("AutoRotate", AutoRotateClient.isEnabled);
			ClientPlayNetworking.send(payload);
		});
	}
}
