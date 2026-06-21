package tools.redstone.redstonetools.packets;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//? if >=1.21.11 {
import net.minecraft.resources.Identifier;
//? } else
//import net.minecraft.resources.ResourceLocation;
import tools.redstone.redstonetools.RedstoneTools;

public record SetFeatureEnabledPayload(String feature, boolean enabled) implements CustomPacketPayload {
	//? if >=1.21.11 {
	public static final Identifier SET_ENABLED_PAYLOAD_ID = Identifier.fromNamespaceAndPath(RedstoneTools.MOD_ID, "set_enabled");
	//? } else
	//public static final ResourceLocation SET_ENABLED_PAYLOAD_ID = ResourceLocation.fromNamespaceAndPath(RedstoneTools.MOD_ID, "set_enabled");

	public static final CustomPacketPayload.Type<SetFeatureEnabledPayload> ID = new CustomPacketPayload.Type<>(SET_ENABLED_PAYLOAD_ID);
	public static final StreamCodec<RegistryFriendlyByteBuf, SetFeatureEnabledPayload> CODEC = StreamCodec.composite(
		ByteBufCodecs.STRING_UTF8, SetFeatureEnabledPayload::feature,
		ByteBufCodecs.BOOL, SetFeatureEnabledPayload::enabled,
		SetFeatureEnabledPayload::new
	);

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
