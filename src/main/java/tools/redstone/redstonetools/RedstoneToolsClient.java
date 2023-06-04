package tools.redstone.redstonetools;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rip.hippo.inject.Doctor;
import rip.hippo.inject.Injector;
import tools.redstone.redstonetools.macros.WorldlessCommandHelper;
import tools.redstone.redstonetools.utils.ReflectionUtils;

public class RedstoneToolsClient implements ClientModInitializer {
    public static final String MOD_ID = "redstonetools";
    public static final String MOD_VERSION = "v" + FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow().getMetadata().getVersion().getFriendlyString();
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Injector INJECTOR = Doctor.createInjector(ReflectionUtils.getModules());

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Redstone Tools");

        // Register game rules
        RedstoneToolsGameRules.register();

        // Register features
        ReflectionUtils.getFeatures().forEach(feature -> {
            LOGGER.trace("Registering feature {}", feature);

            String[] modDependencies = feature.getModDependencies();
            if (modDependencies.length != 0) {
                LOGGER.trace("Checking mod dependencies for feature {}", feature);

                for (String modDependency : modDependencies) {
                    if (!FabricLoader.getInstance().isModLoaded(modDependency)) {
                        LOGGER.warn("Mod dependency '{}' not found for feature {}, force disabling it", modDependency, feature.getClass().getName());
                        return;
                    }
                }
            }
            feature.register();
        });

        // should call the "static" block
        WorldlessCommandHelper.dummyNetworkHandler.getCommandDispatcher();
    }
}
