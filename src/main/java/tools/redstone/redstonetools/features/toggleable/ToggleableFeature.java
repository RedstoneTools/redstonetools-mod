package tools.redstone.redstonetools.features.toggleable;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import tools.redstone.redstonetools.packets.SetFeatureEnabledPayload;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class ToggleableFeature {
	private final Set<UUID> enabledFor = new HashSet<>(); // volatile for thread safety

	public boolean isEnabled(ServerPlayer player) {
		return enabledFor.contains(player.getUUID());
	}

	public int toggle(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		return toggle(context.getSource());
	}

	public int toggle(CommandSourceStack source) throws CommandSyntaxException {
		ServerPlayer player = source.getPlayerOrException();
		return !enabledFor.contains(player.getUUID()) ? enable(source) : disable(source);
	}

	public void setEnabled(boolean status, ServerPlayer player) {
		if (status) {
			enable(player);
		} else {
			disable(player);
		}
	}

	public void enable(ServerPlayer player) {
		enabledFor.add(player.getUUID());
		var payload = new SetFeatureEnabledPayload(this.getName(), true);
		ServerPlayNetworking.send(player, payload);
		onEnable();
	}

	public int enable(CommandSourceStack source) throws CommandSyntaxException {
		enable(source.getPlayerOrException());
		source.sendSystemMessage(Component.literal("Enabled " + this.getName()));
		return 0;
	}

	public void disable(ServerPlayer player) {
		enabledFor.remove(player.getUUID());
		var payload = new SetFeatureEnabledPayload(this.getName(), false);
		ServerPlayNetworking.send(player, payload);
		onDisable();
	}

	public int disable(CommandSourceStack source) throws CommandSyntaxException {
		disable(source.getPlayerOrException());
		source.sendSystemMessage(Component.literal("Disabled " + this.getName()));
		return 0;
	}

	public abstract String getName();

	protected void onEnable() {
	}

	protected void onDisable() {
	}
}
