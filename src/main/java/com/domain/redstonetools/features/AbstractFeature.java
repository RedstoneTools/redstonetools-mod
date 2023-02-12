package com.domain.redstonetools.features;

import com.domain.redstonetools.features.options.Options;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;

public abstract class AbstractFeature<O extends Options> {
    abstract public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated);
}
