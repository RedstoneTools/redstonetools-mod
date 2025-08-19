package tools.redstone.redstonetools.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import tools.redstone.redstonetools.RedstoneTools;

//                                        I cba to make my own codec, this is the next best thing
public record SetFeatureEnabledS2CPayload(String featureAndToggle) implements CustomPayload {
	public static final Identifier SET_ENABLED_PAYLOAD_ID = Identifier.of(RedstoneTools.MOD_ID, "set_enabled");
	public static final CustomPayload.Id<SetFeatureEnabledS2CPayload> ID = new CustomPayload.Id<>(SET_ENABLED_PAYLOAD_ID);
	public static final PacketCodec<RegistryByteBuf, SetFeatureEnabledS2CPayload> CODEC = PacketCodec.tuple(PacketCodecs.STRING, SetFeatureEnabledS2CPayload::featureAndToggle, SetFeatureEnabledS2CPayload::new);

	@Override
	public CustomPayload.Id<? extends CustomPayload> getId() {
		return ID;
	}
}
