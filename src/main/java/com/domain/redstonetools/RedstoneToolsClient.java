package com.domain.redstonetools;

import com.domain.redstonetools.features.AbstractFeature;
import com.domain.redstonetools.features.commands.quicktp.QuickTpFeature;
import com.domain.redstonetools.service.CommandProvider;
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
    private static final List<Class<? extends AbstractFeature>> FEATURES = List.of(
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

    private final Map<String, AbstractFeature> featureMap = new HashMap<>();

    public AbstractFeature getFeature(String str) {
        return featureMap.get(str);
    }

    public boolean registerFeature(AbstractFeature feature) {
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

    public AbstractFeature createAndRegisterFeature(Class<? extends AbstractFeature> klass) {
        try {
            Constructor<?> constructor = klass.getDeclaredConstructor();
            AbstractFeature instance = (AbstractFeature) constructor.newInstance();

            initServices(instance);

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

    // initialize the default services
    private void initServices(AbstractFeature feature) {
        feature.withService(CommandProvider.class, new CommandProvider());
    }

    // registers all initial features
    // from the constant list
    private void registerInitialFeatures() {
        for (var feature : FEATURES) {
            createAndRegisterFeature(feature);
        }
    }

}
