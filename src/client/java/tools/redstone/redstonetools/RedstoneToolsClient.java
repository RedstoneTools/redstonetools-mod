package tools.redstone.redstonetools;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.lwjgl.util.tinyfd.TinyFileDialogs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.redstone.redstonetools.macros.ClientCommands;

public class RedstoneToolsClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(RedstoneTools.MOD_ID);
    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Redstone Tools");
        // check if malilib present, if not open an error window and throw
        if (!FabricLoader.getInstance().isModLoaded("malilib")) {
            TinyFileDialogs.tinyfd_messageBox(
                    "Error",
                    "MaLiLib not present!\nPlease install MaLiLib if you want to use redstonetools",
                    "ok",
                    "error",
                    false
            );
            throw new IllegalStateException("MaLiLib not present");
        }

        // Register commands
        ClientCommands.registerCommands();
        Commands.registerCommands();
//        Registry.register(Registries.COMMAND_ARGUMENT_TYPE, Identifier.of("blockcolor"), new BlockColorArgumentSerializer());
    }
}
