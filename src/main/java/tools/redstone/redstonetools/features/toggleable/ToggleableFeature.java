package tools.redstone.redstonetools.features.toggleable;

import net.minecraft.text.Text;
import tools.redstone.redstonetools.features.AbstractFeature;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;

public abstract class ToggleableFeature extends AbstractFeature {
    private volatile boolean enabled; // volatile for thread safety

    public boolean isEnabled() {
        return enabled;
    }

    public int toggle(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        return toggle(context.getSource());
    }

    public int toggle(ServerCommandSource source) throws CommandSyntaxException {
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

    public int enable(ServerCommandSource source) throws CommandSyntaxException {
        enable();
        source.sendMessage(Text.literal("Enabled feature %s".formatted(this.getClass().getSimpleName())));
        return 0;
    }

    public int enable(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        return enable(context.getSource());
    }

    public void disable() {
        enabled = false;
        onDisable();
    }

    public int disable(ServerCommandSource source) throws CommandSyntaxException {
        disable();
        source.sendMessage(Text.literal("Disabled feature %s".formatted(this.getClass().getSimpleName())));
        return 0;
    }

    public int disable(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        return disable(context.getSource());
    }

    protected void onEnable() { }
    protected void onDisable() { }
}
