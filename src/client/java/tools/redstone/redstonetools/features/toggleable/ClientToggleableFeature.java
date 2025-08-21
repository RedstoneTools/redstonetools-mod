package tools.redstone.redstonetools.features.toggleable;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

public abstract class ClientToggleableFeature {
	private volatile boolean enabled; // volatile for thread safety

	public boolean isEnabled() {
		return enabled;
	}

	public int toggle(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
		return toggle(context.getSource());
	}

	public int toggle(FabricClientCommandSource source) throws CommandSyntaxException {
		return !enabled ? enable(source) : disable(source);
	}

	public void setEnabled(boolean status) {
		if (status == enabled)
			return; // no work to do

		if (status) {
			enable();
		} else {
			disable();
		}
	}

	public void enable() {
		enabled = true;
		onEnable();
	}

	public int enable(FabricClientCommandSource source) throws CommandSyntaxException {
		enable();
		source.getPlayer().sendMessage(Text.literal("Enabled %s".formatted(this.getClass().getSimpleName().replace("Feature", ""))), false);
		return 0;
	}

	public void disable() {
		enabled = false;
		onDisable();
	}

	public int disable(FabricClientCommandSource source) throws CommandSyntaxException {
		disable();
		source.getPlayer().sendMessage(Text.literal("Disabled %s".formatted(this.getClass().getSimpleName().replace("Feature", ""))), false);
		return 0;
	}

	protected void onEnable() {
	}

	protected void onDisable() {
	}
}
