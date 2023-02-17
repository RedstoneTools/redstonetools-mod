package com.domain.redstonetools;

import com.domain.redstonetools.features.AbstractFeature;
import com.domain.redstonetools.features.commands.copystate.CopyStateFeature;
import com.domain.redstonetools.features.commands.baseconvert.BaseConvertFeature;
import com.domain.redstonetools.features.commands.colorcode.ColorCodeFeature;
import com.domain.redstonetools.features.commands.binaryread.BinaryBlockReadFeature;
import com.domain.redstonetools.features.commands.glass.GlassFeature;
import com.domain.redstonetools.features.commands.quicktp.QuickTpFeature;
import com.domain.redstonetools.features.commands.redstoner.RedstonerFeature;
import com.domain.redstonetools.features.commands.ssbarrel.SsBarrelFeature;
import com.domain.redstonetools.utils.ReflectionUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class RedstoneToolsClient implements ClientModInitializer {
    public static final String MOD_ID = "redstonetools";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // TODO: Maybe use https://github.com/ronmamo/reflections to get all classes
    // with the
    // Feature annotation, it might also be useful for other reflection related
    // tasks
    public static final List<Class<? extends AbstractFeature<?>>> FEATURE_CLASSES = List.of(
            QuickTpFeature.class,
            BaseConvertFeature.class,
            GlassFeature.class,
            BinaryBlockReadFeature.class,
            RedstonerFeature.class,
            SsBarrelFeature.class,

            CopyStateFeature.class,
            ColorCodeFeature.class

    );

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Redstone Tools");
        RedstoneToolsGameRules.register();

        CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated) -> {
            for (var featureClass : FEATURE_CLASSES) {
                var feature = ReflectionUtils.getFeatureInstance(featureClass);

                feature.registerCommands(dispatcher, dedicated);
            }
        }));
    }
}
