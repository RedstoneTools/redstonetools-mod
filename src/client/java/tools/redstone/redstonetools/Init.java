package tools.redstone.redstonetools;

import fi.dy.masa.malilib.event.InitializationHandler;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import tools.redstone.redstonetools.malilib.InitHandler;
import tools.redstone.redstonetools.packets.RedstoneToolsClientPackets;
import tools.redstone.redstonetools.utils.ClientFeatureUtils;

public class Init {
	public static void init() {
		ClientLifecycleEvents.CLIENT_STOPPING.register(t -> ClientFeatureUtils.saveToggles());
		ClientLifecycleEvents.CLIENT_STARTED.register(t -> ClientFeatureUtils.readToggles());

		RedstoneToolsClientPackets.registerPackets();
		ClientCommands.registerCommands();
		Commands.registerCommands();

		InitializationHandler.getInstance().registerInitializationHandler(new InitHandler());
	}
}
