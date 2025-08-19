package tools.redstone.redstonetools.features.toggleable;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.packets.SetFeatureEnabledS2CPayload;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class ToggleableFeature extends AbstractFeature {
	private final Set<UUID> enabledFor = new HashSet<>(); // volatile for thread safety

	public boolean isEnabled(ServerPlayerEntity player) {
		return enabledFor.contains(player.getUuid());
	}

	public int toggle(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return toggle(context.getSource());
	}

	public int toggle(ServerCommandSource source) throws CommandSyntaxException {
		ServerPlayerEntity player = source.getPlayerOrThrow();
		return !enabledFor.contains(player.getUuid()) ? enable(source) : disable(source);
	}

	public void setEnabled(boolean status, ServerPlayerEntity player) {
		if (status) {
			enable(player);
		} else {
			disable(player);
		}
	}

	public void enable(ServerPlayerEntity player) {
		enabledFor.add(player.getUuid());
		var payload = new SetFeatureEnabledS2CPayload(this.getName() + "1");
		ServerPlayNetworking.send(player, payload);
		onEnable();
	}

	public int enable(ServerCommandSource source) throws CommandSyntaxException {
		enable(source.getPlayerOrThrow());
		source.sendMessage(Text.literal("Enabled " + this.getName()));
		return 0;
	}

	public void disable(ServerPlayerEntity player) {
		enabledFor.remove(player.getUuid());
		var payload = new SetFeatureEnabledS2CPayload(this.getName() + "0");
		ServerPlayNetworking.send(player, payload);
		onDisable();
	}

	public int disable(ServerCommandSource source) throws CommandSyntaxException {
		disable(source.getPlayerOrThrow());
		source.sendMessage(Text.literal("Disabled " + this.getName()));
		return 0;
	}

	public abstract String getName();

	protected void onEnable() {
	}

	protected void onDisable() {
	}
}
