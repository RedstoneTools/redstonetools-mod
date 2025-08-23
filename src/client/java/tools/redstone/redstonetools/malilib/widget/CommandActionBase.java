package tools.redstone.redstonetools.malilib.widget;

import net.minecraft.client.MinecraftClient;
import tools.redstone.redstonetools.macros.actions.Action;
import tools.redstone.redstonetools.macros.actions.CommandAction;

public class CommandActionBase extends Action {
	public String command;
	public CommandActionBase(String command) {
		this.command = command;
	}

	@Override
	public void run() {
		var player = MinecraftClient.getInstance().player;
		assert player != null;
		if (command.startsWith("/"))
			player.networkHandler.sendChatCommand(command.substring(1));
		else
			player.networkHandler.sendChatMessage(command);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CommandAction commandAction) {
			return command.equals(commandAction.command);
		}

		return super.equals(obj);
	}
}
