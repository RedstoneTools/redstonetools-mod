package tools.redstone.redstonetools;

import fi.dy.masa.malilib.event.InitializationHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import org.lwjgl.util.tinyfd.TinyFileDialogs;
import tools.redstone.redstonetools.malilib.InitHandler;
import tools.redstone.redstonetools.packets.RedstoneToolsClientPackets;
import tools.redstone.redstonetools.utils.ClientFeatureUtils;

import static tools.redstone.redstonetools.RedstoneTools.LOGGER;

public class RedstoneToolsClient implements ClientModInitializer {

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

		ClientLifecycleEvents.CLIENT_STOPPING.register(t -> ClientFeatureUtils.saveToggles());
		ClientLifecycleEvents.CLIENT_STARTED.register(t -> ClientFeatureUtils.readToggles());

		RedstoneToolsClientPackets.registerPackets();
		ClientCommands.registerCommands();
		Commands.registerCommands();

		InitializationHandler.getInstance().registerInitializationHandler(new InitHandler());
	}
}
