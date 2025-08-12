package tools.redstone.redstonetools.features.toggleable;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.features.AbstractFeature;

import java.util.HashSet;
import java.util.Set;

public abstract class ToggleableFeature extends AbstractFeature {
	private final Set<ServerPlayerEntity> enabledFor = new HashSet<>(); // volatile for thread safety

	public boolean isEnabled(ServerPlayerEntity player) {
		return enabledFor.contains(player);
	}

	public int toggle(CommandContext<ServerCommandSource> context) {
		return toggle(context.getSource());
	}

	public int toggle(ServerCommandSource source) {
		ServerPlayerEntity player = source.getPlayer();
		return !enabledFor.contains(player) ? enable(source) : disable(source);
	}

	public void setEnabled(boolean status, ServerPlayerEntity player) {
		if (status) {
			enable(player);
		} else {
			disable(player);
		}
	}

	public void enable(ServerPlayerEntity player) {
		enabledFor.add(player);
		onEnable();
	}

	public int enable(ServerCommandSource source) {
		enable(source.getPlayer());
		source.sendMessage(Text.literal("Enabled %s".formatted(this.getClass().getSimpleName().replace("Feature", ""))));
		return 0;
	}

	public void disable(ServerPlayerEntity player) {
		enabledFor.remove(player);
		onDisable();
	}

	public int disable(ServerCommandSource source) {
		disable(source.getPlayer());
		source.sendMessage(Text.literal("Disabled %s".formatted(this.getClass().getSimpleName().replace("Feature", ""))));
		return 0;
	}

	protected void onEnable() {
	}

	protected void onDisable() {
	}
}
