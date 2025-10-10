package tools.redstone.redstonetools.packets;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import tools.redstone.redstonetools.features.toggleable.AutoDustClient;
import tools.redstone.redstonetools.features.toggleable.AutoRotateClient;
import tools.redstone.redstonetools.features.toggleable.ClickContainerClient;

public class RedstoneToolsClientPackets {
	public static void registerPackets() {
		AutoDustClient.registerHandler();
		AutoRotateClient.registerHandler();
		ClickContainerClient.registerHandler();

		ClientPlayNetworking.registerGlobalReceiver(SetFeatureEnabledPayload.ID, ((payload, context) -> {
			String feature = payload.feature();
			boolean enabled = payload.enabled();

			switch (feature) {
				case "AutoDust" -> AutoDustClient.isEnabled.setBooleanValue(enabled);
				case "AutoRotate" -> AutoRotateClient.isEnabled.setBooleanValue(enabled);
				case "ClickContainers" -> ClickContainerClient.isEnabled.setBooleanValue(enabled);
			}
		}));
	}
}
