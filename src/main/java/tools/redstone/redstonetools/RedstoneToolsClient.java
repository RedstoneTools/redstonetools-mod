package tools.redstone.redstonetools;

import meteordevelopment.starscript.Starscript;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rip.hippo.inject.Doctor;
import rip.hippo.inject.Injector;
import tools.redstone.redstonetools.macros.WorldlessCommandHelper;
import tools.redstone.redstonetools.utils.ReflectionUtils;
import tools.redstone.redstonetools.utils.StarscriptUtils;

public class RedstoneToolsClient implements ClientModInitializer {
    public static final String MOD_ID = "redstonetools";
    public static final String MOD_VERSION = "v" + FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow().getMetadata().getVersion().getFriendlyString();
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Injector INJECTOR = Doctor.createInjector(ReflectionUtils.getModules());
    public static final Starscript STARSCRIPT = new Starscript();

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Redstone Tools");

        StarscriptUtils.initialize(STARSCRIPT);

        // Register game rules
        RedstoneToolsGameRules.register();

        // Register features
        ReflectionUtils.getFeatures().forEach(feature -> {
            LOGGER.trace("Registering feature {}", feature);

            feature.register();
        });

        // should call the "static" block
        WorldlessCommandHelper.dummyNetworkHandler.getCommandDispatcher();
    }
}
