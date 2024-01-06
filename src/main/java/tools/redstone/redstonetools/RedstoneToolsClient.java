package tools.redstone.redstonetools;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.util.Identifier;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mojang.brigadier.arguments.ArgumentType;

import rip.hippo.inject.Doctor;
import rip.hippo.inject.Injector;
import tools.redstone.redstonetools.features.arguments.serializers.IntegerSerializer;
import tools.redstone.redstonetools.features.arguments.serializers.TypeSerializer;
import tools.redstone.redstonetools.utils.DependencyLookup;
import tools.redstone.redstonetools.utils.ReflectionUtils;

import java.nio.file.Path;

public class RedstoneToolsClient implements ClientModInitializer {

    public static final String MOD_ID = "redstonetools";
    public static final String MOD_VERSION = "v" + FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow()
            .getMetadata().getVersion().getFriendlyString();
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir().resolve("redstonetools");
    public static final Injector INJECTOR = Doctor.createInjector(ReflectionUtils.getModules());
    public static final Reflections REFLECTIONS = new Reflections("tools.redstone.redstonetools");

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Redstone Tools");

        // Register game rules
        RedstoneToolsGameRules.register();

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
