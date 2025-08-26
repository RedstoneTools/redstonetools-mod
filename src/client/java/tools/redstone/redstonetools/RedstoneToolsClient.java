package tools.redstone.redstonetools;

import fi.dy.masa.malilib.event.InitializationHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.lwjgl.util.tinyfd.TinyFileDialogs;
import tools.redstone.redstonetools.malilib.InitHandler;
import tools.redstone.redstonetools.packets.RedstoneToolsClientPackets;

import static tools.redstone.redstonetools.RedstoneTools.LOGGER;

public class RedstoneToolsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		LOGGER.info("Initializing Redstone Tools");
		InitializationHandler.getInstance().registerInitializationHandler(new InitHandler());

		RedstoneToolsClientPackets.registerPackets();
		ClientCommands.registerCommands();
		Commands.registerCommands();
	}
}
