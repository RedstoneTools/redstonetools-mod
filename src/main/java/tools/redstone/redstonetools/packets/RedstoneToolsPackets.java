package tools.redstone.redstonetools.packets;

//? if fabric {
/*import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
*///? } else {
import io.netty.buffer.Unpooled;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.plugin.Plugin;
//? }
import net.minecraft.server.level.ServerPlayer;
import tools.redstone.redstonetools.RedstoneTools;
import tools.redstone.redstonetools.features.toggleable.AutoDustFeature;
import tools.redstone.redstonetools.features.toggleable.AutoRotateFeature;
import tools.redstone.redstonetools.features.toggleable.ClickContainerFeature;

public class RedstoneToolsPackets {
	//? if fabric {
	/*public static void registerPackets(Object ignored) {
		//? if <26.1 {
		/^PayloadTypeRegistry.playS2C().register(SetFeatureEnabledPayload.ID, SetFeatureEnabledPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(SetFeatureEnabledPayload.ID, SetFeatureEnabledPayload.CODEC);
		^///? } else {
		PayloadTypeRegistry.serverboundPlay().register(SetFeatureEnabledPayload.ID, SetFeatureEnabledPayload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(SetFeatureEnabledPayload.ID, SetFeatureEnabledPayload.CODEC);
		//? }

		ServerPlayNetworking.registerGlobalReceiver(SetFeatureEnabledPayload.ID,
			(payload, context) -> applyToFeature(payload, context.player()));
	}

	public static void send(ServerPlayer player, SetFeatureEnabledPayload payload) {
		ServerPlayNetworking.send(player, payload);
	}
	*///? } else {
	public static final String CHANNEL = SetFeatureEnabledPayload.SET_ENABLED_PAYLOAD_ID.toString();

	private static Plugin plugin;

	public static void registerPackets(Plugin owner) {
		plugin = owner;

		Bukkit.getMessenger().registerOutgoingPluginChannel(owner, CHANNEL);
		Bukkit.getMessenger().registerIncomingPluginChannel(owner, CHANNEL, (channel, sender, data) -> {
			ServerPlayer player = ((CraftPlayer) sender).getHandle();
			SetFeatureEnabledPayload payload = decode(data, player.registryAccess());
			if (payload != null) {
				applyToFeature(payload, player);
			}
		});
	}

	public static void send(ServerPlayer player, SetFeatureEnabledPayload payload) {
		if (plugin == null) {
			return;
		}
		player.getBukkitEntity().sendPluginMessage(plugin, CHANNEL, encode(payload, player.registryAccess()));
	}

	private static byte[] encode(SetFeatureEnabledPayload payload, RegistryAccess registries) {
		RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), registries);
		try {
			SetFeatureEnabledPayload.CODEC.encode(buf, payload);
			return buf.readByteArray();
		} finally {
			buf.release();
		}
	}

	private static SetFeatureEnabledPayload decode(byte[] data, RegistryAccess registries) {
		RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.wrappedBuffer(data), registries);
		try {
			return SetFeatureEnabledPayload.CODEC.decode(buf);
		} catch (RuntimeException exception) {
			RedstoneTools.LOGGER.warn("Discarding a malformed {} payload", CHANNEL, exception);
			return null;
		} finally {
			buf.release();
		}
	}
	//? }

	/** Shared by both platforms: applies a received toggle to the matching feature. */
	private static void applyToFeature(SetFeatureEnabledPayload payload, ServerPlayer player) {
		switch (payload.feature()) {
			case "AutoDust" -> AutoDustFeature.INSTANCE.setEnabled(payload.enabled(), player);
			case "AutoRotate" -> AutoRotateFeature.INSTANCE.setEnabled(payload.enabled(), player);
			case "ClickContainers" -> ClickContainerFeature.INSTANCE.setEnabled(payload.enabled(), player);
			default -> RedstoneTools.LOGGER.debug("Ignoring unknown feature toggle: {}", payload.feature());
		}
	}
}