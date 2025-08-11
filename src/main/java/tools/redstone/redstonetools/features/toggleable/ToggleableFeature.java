package tools.redstone.redstonetools.features.toggleable;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.features.AbstractFeature;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;

import java.util.HashSet;
import java.util.Set;

public abstract class ToggleableFeature extends AbstractFeature {
    private volatile Set<ServerPlayerEntity> enabledFor = new HashSet<>(); // volatile for thread safety

    public boolean isEnabled(ServerPlayerEntity player) {
        return enabledFor.contains(player);
    }

    public int toggle(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        return toggle(context.getSource());
    }

    public int toggle(ServerCommandSource source) throws CommandSyntaxException {
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

    public int enable(ServerCommandSource source) throws CommandSyntaxException {
        enable(source.getPlayer());
        source.sendMessage(Text.literal("Enabled %s".formatted(this.getClass().getSimpleName().replace("Feature", ""))));
        return 0;
    }

    public void disable(ServerPlayerEntity player) {
        enabledFor.remove(player);
        onDisable();
    }

    public int disable(ServerCommandSource source) throws CommandSyntaxException {
        disable(source.getPlayer());
        source.sendMessage(Text.literal("Disabled %s".formatted(this.getClass().getSimpleName().replace("Feature", ""))));
        return 0;
    }

    public int disable(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        return disable(context.getSource());
    }

    protected void onEnable() { }
    protected void onDisable() { }
}
