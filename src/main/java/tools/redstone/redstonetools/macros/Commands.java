package tools.redstone.redstonetools.macros;

import fi.dy.masa.malilib.gui.GuiBase;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import tools.redstone.redstonetools.macros.gui.malilib.GuiMacroManager;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback.*;

public class Commands {
	public static void registerCommands() {
		EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("edit-macros")
			.executes(commandContext -> {
				GuiBase.openGui(new GuiMacroManager());
				return 1;
			})));
	}
}
