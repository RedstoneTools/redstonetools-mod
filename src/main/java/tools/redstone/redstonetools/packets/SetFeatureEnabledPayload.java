package tools.redstone.redstonetools.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import tools.redstone.redstonetools.RedstoneTools;

public record SetFeatureEnabledPayload(String feature, boolean enabled) implements CustomPayload {
	public static final Identifier SET_ENABLED_PAYLOAD_ID = Identifier.of(RedstoneTools.MOD_ID, "set_enabled");
	public static final CustomPayload.Id<SetFeatureEnabledPayload> ID = new CustomPayload.Id<>(SET_ENABLED_PAYLOAD_ID);
	public static final PacketCodec<RegistryByteBuf, SetFeatureEnabledPayload> CODEC = PacketCodec.tuple(
		PacketCodecs.STRING, SetFeatureEnabledPayload::feature,
		PacketCodecs.BOOLEAN, SetFeatureEnabledPayload::enabled,
		SetFeatureEnabledPayload::new
	);

	@Override
	public CustomPayload.Id<? extends CustomPayload> getId() {
		return ID;
	}
}
