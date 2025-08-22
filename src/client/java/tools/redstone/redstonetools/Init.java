package tools.redstone.redstonetools;

import fi.dy.masa.malilib.event.InitializationHandler;
import tools.redstone.redstonetools.malilib.InitHandler;
import tools.redstone.redstonetools.packets.RedstoneToolsClientPackets;

public class Init {
	public static void init() {
		InitializationHandler.getInstance().registerInitializationHandler(new InitHandler());

		RedstoneToolsClientPackets.registerPackets();
		ClientCommands.registerCommands();
		Commands.registerCommands();
	}
}
