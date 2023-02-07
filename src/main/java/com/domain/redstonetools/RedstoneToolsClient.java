package com.domain.redstonetools;

import com.domain.redstonetools.features.commands.CommandFeature;
import com.domain.redstonetools.features.commands.quicktp.QuickTpFeature;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RedstoneToolsClient implements ClientModInitializer {
    public static final String MOD_ID = "redstonetools";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static final List<CommandFeature> FEATURES = List.of(
        new QuickTpFeature()
    );

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Redstone Tools");

        registerFeatures();
    }

    private void registerFeatures() {
        for (var feature : FEATURES) {
            registerFeature(feature);
        }
    }

    private void registerFeature(CommandFeature feature) {
        CommandRegistrationCallback.EVENT.register(feature::registerCommand);
    }
}
