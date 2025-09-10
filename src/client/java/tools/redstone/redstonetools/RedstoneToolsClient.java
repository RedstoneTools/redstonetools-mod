package tools.redstone.redstonetools;

import fi.dy.masa.malilib.event.InitializationHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents;
import tools.redstone.redstonetools.malilib.InitHandler;
import tools.redstone.redstonetools.malilib.config.Configs;
import tools.redstone.redstonetools.packets.RedstoneToolsClientPackets;

import static tools.redstone.redstonetools.RedstoneTools.LOGGER;

public class RedstoneToolsClient implements ClientModInitializer {
	private static boolean hasRanCommands = false;

	@Override
	public void onInitializeClient() {
		LOGGER.info("Initializing Redstone Tools");
		InitializationHandler.getInstance().registerInitializationHandler(new InitHandler());

		ClientWorldEvents.AFTER_CLIENT_WORLD_CHANGE.register((client, clientWorld) -> {
			if (client.getNetworkHandler() != null) { // dimension change
				for (String str : Configs.ClientData.AUTORUN_DIMENSION_CHANGE.getStrings()) {
					if (str.startsWith("/")) {
						client.getNetworkHandler().sendChatCommand(str.substring(1));
					}
				}
			} else { // world entry
				for (String str : Configs.ClientData.AUTORUN_WORLD_ENTRY.getStrings()) {
					if (str.startsWith("/")) {
						client.send(() -> client.getNetworkHandler().sendChatCommand(str.substring(1)));
					}
				}
				if (hasRanCommands) return;
				hasRanCommands = true;
				for (String str : Configs.ClientData.AUTORUN_FIRST_WORLD_ENTRY.getStrings()) {
					if (str.startsWith("/")) {
						client.send(() -> client.getNetworkHandler().sendChatCommand(str.substring(1)));
					}
				}
			}
		});

		RedstoneToolsClientPackets.registerPackets();
		ClientCommands.registerCommands();
		Commands.registerCommands();
	}
}
