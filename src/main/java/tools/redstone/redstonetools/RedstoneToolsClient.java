package tools.redstone.redstonetools;

import com.mojang.serialization.Codec;
import fi.dy.masa.malilib.event.InitializationHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.redstone.redstonetools.features.commands.argument.BlockColorArgumentType;
import tools.redstone.redstonetools.features.commands.serializer.BlockColorArgumentSerializer;
import tools.redstone.redstonetools.macros.Commands;
import tools.redstone.redstonetools.macros.gui.malilib.InitHandler;

import java.nio.file.Path;

public class RedstoneToolsClient implements ClientModInitializer {
    public static final ComponentType<String> COMMAND_COMPONENT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(RedstoneToolsClient.MOD_ID, "command"),
            ComponentType.<String>builder().codec(Codec.STRING).build()
    );
    public static final String MOD_ID = "redstonetools";
    public static final String MOD_VERSION = "v" + FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow().getMetadata().getVersion().getFriendlyString();
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir().resolve("redstonetools");
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
        ArgumentTypeRegistry.registerArgumentType(Identifier.of("blockcolor"), BlockColorArgumentType.class, ConstantArgumentSerializer.of(BlockColorArgumentType::blockcolor));
//        Registry.register(Registries.COMMAND_ARGUMENT_TYPE, Identifier.of("blockcolor"), new BlockColorArgumentSerializer());
    }
}
