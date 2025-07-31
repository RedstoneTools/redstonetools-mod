package tools.redstone.redstonetools.features.toggleable;

import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.features.AbstractFeature;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public abstract class ClientToggleableFeature extends AbstractFeature {
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
        source.getPlayer().sendMessage(Text.literal("Enabled feature %s".formatted(this.getClass().getSimpleName())), false);
        return 0;
    }

    public int enable(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        return enable(context.getSource());
    }

    public void disable() {
        enabled = false;
        onDisable();
    }

    public int disable(FabricClientCommandSource source) throws CommandSyntaxException {
        disable();
        source.getPlayer().sendMessage(Text.literal("Disabled feature %s".formatted(this.getClass().getSimpleName())), false);
        return 0;
    }

    public int disable(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        return disable(context.getSource());
    }

    protected void onEnable() { }
    protected void onDisable() { }
}
