package com.domain.redstonetools;

import com.domain.redstonetools.features.Feature;
import com.domain.redstonetools.features.commands.quicktp.QuickTpFeature;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RedstoneToolsClient implements ClientModInitializer {

    public static final String MOD_ID = "redstonetools";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    ////////////////////////////////////////////

    /**
     * Initial features to be initialized.
     *
     * The classes are stored instead of the
     * objects to avoid instantiating them before
     * any other services provided by the mod are
     * initialized.
     */
    private static final List<Class<? extends Feature>> FEATURES = List.of(
            QuickTpFeature.class
    );

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Redstone Tools");

        registerInitialFeatures();
    }

    /*
        Feature Registry
     */

    private final Map<String, Feature> featureMap = new HashMap<>();

    public Feature getFeature(String str) {
        return featureMap.get(str);
    }

    public boolean registerFeature(Feature feature) {
        try {
            feature.register();
            featureMap.put(feature.getName(), feature);
            return true;
        } catch (Exception e) {
            LOGGER.error("Error while initializing feature '" + feature.getName() + "'");
            e.printStackTrace();

            return false;
        }
    }

    public Feature createAndRegisterFeature(Class<? extends Feature> klass) {
        try {
            Constructor<?> constructor = klass.getDeclaredConstructor();
            Feature instance = (Feature) constructor.newInstance();

            if (!registerFeature(instance)) {
                return null;
            }

            return instance;
        } catch (Exception e) {
            LOGGER.error("Failed to instantiate feature " + klass.getName());
            e.printStackTrace();

            return null;
        }
    }

    // registers all initial features
    // from the constant list
    private void registerInitialFeatures() {
        for (var feature : FEATURES) {
            createAndRegisterFeature(feature);
        }
    }

}
