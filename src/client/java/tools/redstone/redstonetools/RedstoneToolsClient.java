package tools.redstone.redstonetools;

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

        // check if malilib present, if not throw
        // TODO: open a window telling the user to install malilib
        //       can't use swing or something because minecraft runs in headless mode
        if (!FabricLoader.getInstance().isModLoaded("malilib")) {
            throw new IllegalStateException("MaLiLib not present");
        }

        // Register commands
        ClientCommands.registerCommands();
        Commands.registerCommands();
        InitHandler.initHandlerExecuter();
//        Registry.register(Registries.COMMAND_ARGUMENT_TYPE, Identifier.of("blockcolor"), new BlockColorArgumentSerializer());
    }
}
