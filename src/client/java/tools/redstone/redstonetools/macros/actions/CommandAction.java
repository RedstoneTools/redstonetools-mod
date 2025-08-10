package tools.redstone.redstonetools.macros.actions;

import net.minecraft.client.MinecraftClient;

public class CommandAction extends Action {
	public String command;

	public CommandAction(String command) {
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
