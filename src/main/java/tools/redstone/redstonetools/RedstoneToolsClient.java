package tools.redstone.redstonetools;

import com.mojang.brigadier.arguments.ArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rip.hippo.inject.Doctor;
import rip.hippo.inject.Injector;
import tools.redstone.redstonetools.utils.DependencyLookup;
import tools.redstone.redstonetools.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.nio.file.Path;

public class RedstoneToolsClient implements ClientModInitializer {

    public static final String MOD_ID = "redstonetools";
    public static final String MOD_VERSION = "v" + FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow()
            .getMetadata().getVersion().getFriendlyString();
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir().resolve("redstonetools");
    public static final Injector INJECTOR = Doctor.createInjector(ReflectionUtils.getModules());

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Redstone Tools");

        // Register game rules
        RedstoneToolsGameRules.register();

        // Register arguments
        ReflectionUtils.getAllArguments().forEach(argument -> {
            Identifier id = Identifier.of(MOD_ID, argument.getSimpleName().toLowerCase());

            try {
                Field instanceField = argument.getDeclaredField("INSTANCE");
                instanceField.setAccessible(true);
                ArgumentType<?> instance = (ArgumentType<?>) instanceField.get(argument);
                ArgumentTypeRegistry.registerArgumentType(
                        id,
                        argument,
                        ConstantArgumentSerializer.of(v -> instance));
            } catch (IllegalAccessException | IllegalArgumentException | SecurityException | NoSuchFieldException |
                     ClassCastException e) {
                e.printStackTrace();
                LOGGER.error("Failed to register argument type {}. Skipping registration.",
                        argument.getName());
            }
        });

        // Register features
        ReflectionUtils.getFeatures().forEach(feature ->

        {
            LOGGER.trace("Registering feature {}", feature.getClass().getName());

            if (feature.requiresWorldEdit() && !DependencyLookup.WORLDEDIT_PRESENT) {
                LOGGER.warn("Feature {} requires WorldEdit, but WorldEdit is not loaded. Skipping registration.",
                        feature.getName());
                return;
            }
            feature.register();
        });
    }
}
