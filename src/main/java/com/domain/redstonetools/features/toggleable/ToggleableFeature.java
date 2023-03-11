package com.domain.redstonetools.features.toggleable;

import com.domain.redstonetools.features.AbstractFeature;
import com.domain.redstonetools.utils.ReflectionUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.literal;

public abstract class ToggleableFeature extends AbstractFeature {
    private boolean enabled;

    @Override
    protected void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        var info = ReflectionUtils.getFeatureInfo(getClass());

        dispatcher.register(literal(info.command())
                .executes(this::run));
    }

    public boolean isEnabled() {
        return enabled;
    }

    private int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        enabled = !enabled;

        return enabled ? onEnable(context.getSource()) : onDisable(context.getSource());
    }

    protected int onEnable(ServerCommandSource source) throws CommandSyntaxException {
        return 0;
    }

    protected int onDisable(ServerCommandSource source) throws CommandSyntaxException {
        return 0;
    }
}
