package tools.redstone.redstonetools;

import fi.dy.masa.malilib.event.InitializationHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.redstone.redstonetools.macros.ClientCommands;
import tools.redstone.redstonetools.macros.gui.malilib.InitHandler;

import java.nio.file.Path;

public class RedstoneToolsClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(RedstoneTools.MOD_ID);
    public static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir().resolve("redstonetools");
    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Redstone Tools");

        // Register game rules

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
        ClientCommands.registerCommands();
        Commands.registerCommands();
        InitializationHandler.getInstance().registerInitializationHandler(new InitHandler());
//        Registry.register(Registries.COMMAND_ARGUMENT_TYPE, Identifier.of("blockcolor"), new BlockColorArgumentSerializer());
    }
}
