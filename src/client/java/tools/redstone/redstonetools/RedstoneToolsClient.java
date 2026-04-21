package tools.redstone.redstonetools;

import kr1v.malilibApi.MalilibApi;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents;
import tools.redstone.redstonetools.config.ClientData;
import tools.redstone.redstonetools.packets.RedstoneToolsClientPackets;

import static tools.redstone.redstonetools.RedstoneTools.*;

public class RedstoneToolsClient implements ClientModInitializer {
	private static boolean hasRanCommands = false;

	@Override
	public void onInitializeClient() {
		LOGGER.info("Initializing Redstone Tools");
		MalilibApi.registerMod(MOD_ID, MOD_NAME);

		ClientWorldEvents.AFTER_CLIENT_WORLD_CHANGE.register((client, clientWorld) -> {
			if (client.getNetworkHandler() != null) { // dimension change
				String dimensionChange = ClientData.AUTORUN_DIMENSION_CHANGE.getStringValue();
				if (dimensionChange.startsWith("/")) {
					client.getNetworkHandler().sendChatCommand(dimensionChange.substring(1));
				} else if (!dimensionChange.isEmpty()){
					client.getNetworkHandler().sendChatMessage(dimensionChange);
				}
			} else { // world entry
				String worldEntry = ClientData.AUTORUN_WORLD_ENTRY.getStringValue();
				if (worldEntry.startsWith("/")) {
					client.send(() -> client.getNetworkHandler().sendChatCommand(worldEntry.substring(1)));
				} else if (!worldEntry.isEmpty()){
					client.send(() -> client.getNetworkHandler().sendChatMessage(worldEntry));
				}
				if (hasRanCommands) return;
				hasRanCommands = true;
				String firstWorldEntry = ClientData.AUTORUN_FIRST_WORLD_ENTRY.getStringValue();
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
