package com.domain.redstonetools;

import com.domain.redstonetools.features.AbstractFeature;
import com.domain.redstonetools.features.Feature;
import com.domain.redstonetools.features.commands.quicktp.QuickTpFeature;
import com.domain.redstonetools.features.options.Options;
import com.domain.redstonetools.utils.ReflectionUtils;
import com.mojang.brigadier.arguments.ArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RedstoneToolsClient implements ClientModInitializer {
    public static final String MOD_ID = "redstonetools";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // TODO: Maybe use https://github.com/ronmamo/reflections to get all classes with the
    // Feature annotation, it might also be useful for other reflection related tasks
    public static final List<Class<? extends AbstractFeature<?>>> FEATURE_CLASSES = List.of(
        QuickTpFeature.class
    );

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Redstone Tools");

        CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated) -> {
            for (var featureClass : FEATURE_CLASSES) {
                var feature = ReflectionUtils.getFeatureInstance(featureClass);

                feature.registerCommands(dispatcher, dedicated);
            }
        }));
    }
}
