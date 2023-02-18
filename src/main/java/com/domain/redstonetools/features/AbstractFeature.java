package com.domain.redstonetools.features;

import com.domain.redstonetools.features.options.Options;
import com.domain.redstonetools.utils.ReflectionUtils;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;

public abstract class AbstractFeature<O extends Options> {

    // the feature info
    protected final Feature featureInfo =
            ReflectionUtils.getFeatureInfo(this);

    public Feature getInfo() {
        return featureInfo;
    }

    abstract public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated);

}
