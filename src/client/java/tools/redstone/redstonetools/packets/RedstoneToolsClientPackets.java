package tools.redstone.redstonetools.packets;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import tools.redstone.redstonetools.features.toggleable.AutoDustClient;

public class RedstoneToolsClientPackets {
	public static void registerPackets() {
		ClientPlayNetworking.registerGlobalReceiver(SetFeatureEnabledS2CPayload.ID, ((payload, context) -> {
			String feature = payload.featureAndToggle().substring(0, payload.featureAndToggle().length() - 1);
			boolean enabled;
			enabled = payload.featureAndToggle().endsWith("1");

			switch (feature) {
				case "AutoDust":
					AutoDustClient.isEnabled = enabled;
				default:
			}
		}));
	}
}
