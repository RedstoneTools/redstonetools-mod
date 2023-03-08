package com.domain.redstonetools.features;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

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

    public String getName() {
        return featureDesc.name();
    }

    public String getDescription() {
        return featureDesc.description();
    }

    public String getCommand() {
        return featureDesc.command();
    }

    /**
     * Register this feature.
     */
    public void register() {
        CommandRegistrationCallback.EVENT.register(this::registerCommands);
    }

    protected abstract void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated);

}
