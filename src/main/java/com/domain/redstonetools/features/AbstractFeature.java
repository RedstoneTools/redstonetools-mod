package com.domain.redstonetools.features;

import com.domain.redstonetools.config.ConfigurationManager;
import com.domain.redstonetools.config.FeatureConfiguration;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

public abstract class AbstractFeature {

    // cached descriptor for this
    // feature to avoid regular slow
    // reflection
    protected final Feature featureDesc;

    // the configuration for this feature
    protected final FeatureConfiguration config;

    {
        featureDesc = getClass().getAnnotation(Feature.class);
        if (featureDesc == null)
            throw new IllegalStateException("Supposed feature " + getClass() + " does not" +
                    " have an @Feature descriptor annotation");

        // create configuration
        config = ConfigurationManager.get().newConfiguration(this);
    }

    public String getName() {
        return featureDesc.name();
    }

    public String getIdentifier() {
        return featureDesc.id();
    }

    public String getDescription() {
        return featureDesc.description();
    }

    public String getCommandName() {
        return featureDesc.command();
    }

    /**
     * Register this feature.
     */
    public void register() {
        CommandRegistrationCallback.EVENT.register(this::registerCommands);

        // load and save initial configuration
        // we save to make sure
        config.load();

    }

    protected abstract void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated);

}
