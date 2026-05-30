package tools.redstone.redstonetools.macros.actions;

import net.minecraft.client.Minecraft;

public class CommandAction extends Action {
	public String command;

	public CommandAction(String command) {
		this.command = command;
	}

	@Override
	public void run() {
		var player = Minecraft.getInstance().player;
		assert player != null;
		if (command.startsWith("/"))
			player.connection.sendCommand(command.substring(1));
		else if (!command.isEmpty())
			player.connection.sendChat(command);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CommandAction commandAction) {
			return command.equals(commandAction.command);
		}

		return super.equals(obj);
	}

}
