package com.domain.redstonetools.features;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;

public abstract class AbstractFeature {

    // cached descriptor for this
    // feature to avoid regular slow
    // reflection
    final Feature featureDesc;

    {
        featureDesc = getClass().getAnnotation(Feature.class);
        if (featureDesc == null)
            throw new IllegalStateException("Supposed feature " + getClass() + " does not" +
                    " have an @Feature descriptor annotation");
    }

    public Feature getFeatureDescriptor() {
        return featureDesc;
    }

    /**
     * Register this feature.
     */
    public void register() {
        CommandRegistrationCallback.EVENT.register(this::registerCommands);
    }

    protected abstract void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated);

}
