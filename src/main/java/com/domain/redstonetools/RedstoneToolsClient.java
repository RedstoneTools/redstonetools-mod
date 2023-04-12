package com.domain.redstonetools;


import com.domain.redstonetools.macros.WorldlessCommandHelper;
import com.domain.redstonetools.utils.ReflectionUtils;
import com.google.inject.Guice;
import com.google.inject.Injector;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RedstoneToolsClient implements ClientModInitializer {
    public static final String MOD_ID = "redstonetools";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Injector INJECTOR = Guice.createInjector(ReflectionUtils.getModules());

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Redstone Tools");

        // Register game rules
        RedstoneToolsGameRules.register();

        // Register features
        for (var featureClass : ReflectionUtils.getFeatureClasses()) {
            var feature = INJECTOR.getInstance(featureClass);

            feature.register();
        }
        
        WorldlessCommandHelper.dummyNetworkHandler.getCommandDispatcher();//should call the "static" method
    }
}
