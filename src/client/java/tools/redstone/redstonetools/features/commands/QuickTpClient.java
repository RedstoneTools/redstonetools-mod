package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback.EVENT;

public class QuickTpClient {
	public static final QuickTpClient INSTANCE = new QuickTpClient();

	protected QuickTpClient() {
	}

	public void registerCommand() {
		EVENT.register((dispatcher, registryAccess) -> dispatcher.register(
			literal("quicktp")
				.requires(source -> source.getPlayer().hasPermissionLevel(2))
				.executes(context -> this.execute(
					context,
					50.0f))
				.then(argument("distance", FloatArgumentType.floatArg())
					.executes(context -> this.execute(
						context,
						context.getArgument("distance", float.class)
					)))));
	}

	private int execute(CommandContext<FabricClientCommandSource> context, float distance) {
		context.getSource().getPlayer().networkHandler.sendChatCommand(
			"tp @s ^ ^ ^" + distance
		);
		return 0;
	}
}
