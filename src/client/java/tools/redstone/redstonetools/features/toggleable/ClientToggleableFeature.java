package tools.redstone.redstonetools.features.toggleable;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;

public abstract class ClientToggleableFeature {
	private final ConfigBoolean enabled; // volatile for thread safety

	protected ClientToggleableFeature(ConfigBoolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled.getBooleanValue();
	}

	public int toggle(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
		return toggle(context.getSource());
	}

	public int toggle(FabricClientCommandSource source) throws CommandSyntaxException {
		return !enabled.getBooleanValue() ? enable(source) : disable(source);
	}

	public void setEnabled(boolean status) {
		if (status == enabled.getBooleanValue())
			return; // no work to do

		if (status) {
			enable();
		} else {
			disable();
		}
	}

	public void enable() {
		enabled.setBooleanValue(true);
		onEnable();
	}

	public int enable(FabricClientCommandSource source) throws CommandSyntaxException {
		enable();
		//? if <26.1 {
		source.getPlayer().displayClientMessage(Component.literal("Enabled %s".formatted(this.getClass().getSimpleName().replace("Feature", ""))), false);
		//? } else
		//source.getPlayer().sendSystemMessage(Component.literal("Enabled %s".formatted(this.getClass().getSimpleName().replace("Feature", ""))));
		return 0;
	}

	public void disable() {
		enabled.setBooleanValue(false);
		onDisable();
	}

	public int disable(FabricClientCommandSource source) throws CommandSyntaxException {
		disable();
		//? if <26.1 {
		source.getPlayer().displayClientMessage(Component.literal("Disabled %s".formatted(this.getClass().getSimpleName().replace("Feature", ""))), false);
        //? } else
		//source.getPlayer().sendSystemMessage(Component.literal("Disabled %s".formatted(this.getClass().getSimpleName().replace("Feature", ""))));
		return 0;
	}

	protected void onEnable() {
	}

	protected void onDisable() {
	}
}
