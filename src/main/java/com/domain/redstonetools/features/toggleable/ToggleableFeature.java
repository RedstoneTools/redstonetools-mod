package com.domain.redstonetools.features.toggleable;

import com.domain.redstonetools.features.AbstractFeature;
import com.domain.redstonetools.utils.CommandUtils;
import com.domain.redstonetools.utils.ReflectionUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;

import java.util.concurrent.atomic.AtomicBoolean;

import static net.minecraft.server.command.CommandManager.literal;

public abstract class ToggleableFeature extends AbstractFeature {
    private AtomicBoolean enabled = new AtomicBoolean(false); // use atomic for thread safety

    @Override
    protected void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        var info = ReflectionUtils.getFeatureInfo(getClass());

        LiteralArgumentBuilder<ServerCommandSource> enableBuilder = literal("enable").executes(this::enable);
        // add arguments
        enableBuilder.then(CommandUtils.createArgumentChain(ReflectionUtils.getArguments(getClass()),
                this::enable));

        dispatcher.register(literal(info.command())
                .then(enableBuilder)
                .then(literal("disable")
                        .executes(this::disable)));
    }

    public boolean isEnabled() {
        return enabled.get();
    }

    private int enable(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        enabled.set(true);

        return onEnable(context.getSource());
    }

    private int disable(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        enabled.set(false);

        return onDisable(context.getSource());
    }

    protected int onEnable(ServerCommandSource source) throws CommandSyntaxException {
        return 0;
    }

    protected int onDisable(ServerCommandSource source) throws CommandSyntaxException {
        return 0;
    }
}
