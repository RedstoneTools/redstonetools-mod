package com.domain.redstonetools.features;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;

public abstract class AbstractFeature {

    /** Cached feature descriptor. */
    private final Feature feature;

    {
        feature = getClass().getAnnotation(Feature.class);

        if (feature == null) {
            throw new IllegalStateException("Feature " + getClass() + " is not annotated with @Feature");
        }
    }

    /* Getters */

    public String getName() {
        return feature.name();
    }

    public String getDescription() {
        return feature.description();
    }

    public String getCommand() {
        return feature.command();
    }

    /**
     * Register this feature.
     */
    public void register() {
        CommandRegistrationCallback.EVENT.register(this::registerCommands);
    }

    protected abstract void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated);

}
