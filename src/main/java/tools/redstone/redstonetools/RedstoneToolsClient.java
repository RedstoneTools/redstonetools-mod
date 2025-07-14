package tools.redstone.redstonetools;

import fi.dy.masa.malilib.config.options.ConfigStringList;
import fi.dy.masa.malilib.event.InitializationHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rip.hippo.inject.Doctor;
import rip.hippo.inject.Injector;
import tools.redstone.redstonetools.macros.Commands;
import tools.redstone.redstonetools.macros.Macro;
import tools.redstone.redstonetools.macros.gui.malilib.InitHandler;
import tools.redstone.redstonetools.utils.FeatureUtils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class RedstoneToolsClient implements ClientModInitializer {

    public static final String MOD_ID = "redstonetools";
    public static final String MOD_VERSION = "v" + FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow().getMetadata().getVersion().getFriendlyString();
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir().resolve("redstonetools");
    public static final List<Macro> MACRO_CONFIGS = new ArrayList<Macro>();

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Redstone Tools");

        // Register game rules
        RedstoneToolsGameRules.register();

        // Register features
//        FeatureUtils.getFeatures().forEach(feature -> {
//            LOGGER.trace("Registering feature {}", feature.getClass().getName());
//
//            if (feature.requiresWorldEdit() && !DependencyLookup.WORLDEDIT_PRESENT) {
//                LOGGER.warn("Feature {} requires WorldEdit, but WorldEdit is not loaded. Skipping registration.", feature.getName());
//                return;
//            }
//            feature.register();
//        });

        // Register commands
        InitializationHandler.getInstance().registerInitializationHandler(new InitHandler());
        Commands.registerCommands();
    }
}
