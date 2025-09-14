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
				String dimensionChange = Configs.ClientData.AUTORUN_DIMENSION_CHANGE.getStringValue();
				if (dimensionChange.startsWith("/")) {
					client.getNetworkHandler().sendChatCommand(dimensionChange.substring(1));
				} else if (!dimensionChange.isEmpty()){
					client.getNetworkHandler().sendChatMessage(dimensionChange);
				}
			} else { // world entry
				String worldEntry = Configs.ClientData.AUTORUN_WORLD_ENTRY.getStringValue();
				if (worldEntry.startsWith("/")) {
					client.send(() -> client.getNetworkHandler().sendChatCommand(worldEntry.substring(1)));
				} else if (!worldEntry.isEmpty()){
					client.send(() -> client.getNetworkHandler().sendChatMessage(worldEntry));
				}
				if (hasRanCommands) return;
				hasRanCommands = true;
				String firstWorldEntry = Configs.ClientData.AUTORUN_FIRST_WORLD_ENTRY.getStringValue();
				if (firstWorldEntry.startsWith("/")) {
					client.send(() -> client.getNetworkHandler().sendChatCommand(firstWorldEntry.substring(1)));
				} else if (!firstWorldEntry.isEmpty()) {
					client.send(() -> client.getNetworkHandler().sendChatMessage(firstWorldEntry));
				}
			}
		});

		RedstoneToolsClientPackets.registerPackets();
		Commands.registerCommands();
	}
}
