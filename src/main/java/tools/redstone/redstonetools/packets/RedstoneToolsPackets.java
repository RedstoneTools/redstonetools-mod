package tools.redstone.redstonetools.packets;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import tools.redstone.redstonetools.features.toggleable.AutoDustFeature;
import tools.redstone.redstonetools.features.toggleable.AutoRotateFeature;
import tools.redstone.redstonetools.features.toggleable.ClickContainerFeature;
import tools.redstone.redstonetools.utils.FeatureUtils;

public class RedstoneToolsPackets {
	public static void registerPackets() {
		PayloadTypeRegistry.playS2C().register(SetFeatureEnabledS2CPayload.ID, SetFeatureEnabledS2CPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(SetFeatureEnabledC2SPayload.ID, SetFeatureEnabledC2SPayload.CODEC);

		ServerPlayNetworking.registerGlobalReceiver(SetFeatureEnabledC2SPayload.ID, (payload, context) -> {
			String feature = payload.featureAndToggle().substring(0, payload.featureAndToggle().length() - 1);
			boolean enabled = payload.featureAndToggle().endsWith("1");

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
