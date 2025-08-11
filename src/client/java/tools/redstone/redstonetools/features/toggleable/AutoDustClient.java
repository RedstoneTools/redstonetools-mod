package tools.redstone.redstonetools.features.toggleable;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class AutoDustClient {
	public static boolean isEnabled;

	public static void registerHandler() {
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			if (AutoDustClient.isEnabled) {
				var server = MinecraftClient.getInstance().player.getServer();
				server.getCommandManager().executeWithPrefix(server.getCommandSource(), "/autodust");
				MinecraftClient.getInstance().player.sendMessage(Text.of("Enabled autodust"), false);
			}
		});
	}
}
