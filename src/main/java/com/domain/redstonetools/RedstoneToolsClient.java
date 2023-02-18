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

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    // the feature instances loaded
    static final Map<String, AbstractFeature<?>> featureMap =
            new HashMap<>();

    public static AbstractFeature<?> getFeature(String str) {
        return featureMap.get(str);
    }

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Redstone Tools");
        RedstoneToolsGameRules.register();

        // create features
        for (Class<? extends AbstractFeature<?>> featureClass : FEATURE_CLASSES) {
            AbstractFeature<?> feature = ReflectionUtils.newFeatureInstance(featureClass);
            featureMap.put(feature.getInfo().name(), feature);
        }

        CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated) -> {
            for (var feature : featureMap.values()) {
                feature.registerCommands(dispatcher, dedicated);
            }
        }));
    }
}
