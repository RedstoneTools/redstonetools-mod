package com.domain.redstonetools.features;

import com.domain.redstonetools.config.ConfigurationManager;
import com.domain.redstonetools.config.FeatureConfiguration;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;

public abstract class AbstractFeature {

    private final FeatureConfiguration configuration;
    private final Feature feature;

    {
        feature = getClass().getAnnotation(Feature.class);

        if (feature == null) {
            throw new IllegalStateException("Feature " + getClass() + " is not annotated with @Feature");
        }

        configuration = ConfigurationManager.get().newConfiguration(this);
    }

    public String getName() {
        return feature.name();
    }

    public String getDescription() {
        return feature.description();
    }

    public String getCommand() {
        return feature.command();
    }

    public String getIdentifier() {
        return feature.id();
    }

    /**
     * Register this feature.
     */
    public void register() {
        CommandRegistrationCallback.EVENT.register(this::registerCommands);

        // load and save initial configuration
        // we save to make sure defaults are saved
        configuration.load();
        configuration.save();
    }

    protected abstract void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated);

}
