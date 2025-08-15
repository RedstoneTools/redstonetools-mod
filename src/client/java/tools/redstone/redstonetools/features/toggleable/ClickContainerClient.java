package tools.redstone.redstonetools.features.toggleable;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import tools.redstone.redstonetools.packets.SetFeatureEnabledC2SPayload;

public class ClickContainerClient {
	public static boolean isEnabled;
	public static void registerHandler() {
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			SetFeatureEnabledC2SPayload payload = new SetFeatureEnabledC2SPayload("ClickContainer" + (ClickContainerClient.isEnabled ? "0" : "1"));
			ClientPlayNetworking.send(payload);
		});
	}
}
