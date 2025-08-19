package tools.redstone.redstonetools.packets;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import tools.redstone.redstonetools.features.toggleable.AutoDustFeature;
import tools.redstone.redstonetools.features.toggleable.AutoRotateFeature;
import tools.redstone.redstonetools.features.toggleable.ClickContainerFeature;
import tools.redstone.redstonetools.utils.FeatureUtils;

public class RedstoneToolsPackets {
	public static void registerPackets() {
		PayloadTypeRegistry.playS2C().register(SetFeatureEnabledPayload.ID, SetFeatureEnabledPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(SetFeatureEnabledPayload.ID, SetFeatureEnabledPayload.CODEC);

		ServerPlayNetworking.registerGlobalReceiver(SetFeatureEnabledPayload.ID, (payload, context) -> {
			String feature = payload.feature();
			boolean enabled = payload.enabled();

			switch (feature) {
				case "AutoDust" ->
						FeatureUtils.getFeature(AutoDustFeature.class).setEnabled(enabled, context.player());
				case "AutoRotate" ->
						FeatureUtils.getFeature(AutoRotateFeature.class).setEnabled(enabled, context.player());
				case "ClickContainers" ->
						FeatureUtils.getFeature(ClickContainerFeature.class).setEnabled(enabled, context.player());
				default -> {
				}
			}
		});
	}
}
