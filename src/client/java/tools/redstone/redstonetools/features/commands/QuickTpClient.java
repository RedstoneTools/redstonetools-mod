package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.commands.CommandBuildContext;
import tools.redstone.redstonetools.ClientCommands;

//? if >=26.1 {
import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;
//? } else {
/*import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
*///? }

public class QuickTpClient {
	public static final QuickTpClient INSTANCE = new QuickTpClient();

	protected QuickTpClient() {
	}

	public void registerCommand(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext registryAccess) {
			dispatcher.register(
			literal("quicktp")
				.requires(ClientCommands.PERMISSION_LEVEL_2)
				.executes(context -> this.execute(
					context,
					50.0f))
				.then(argument("distance", FloatArgumentType.floatArg())
					.executes(context -> this.execute(
						context,
						context.getArgument("distance", float.class)
					))));
	}

	private int execute(CommandContext<FabricClientCommandSource> context, float distance) {
		context.getSource().getPlayer().connection.sendCommand(
			"tp @s ^ ^ ^" + distance
		);
		return 0;
	}
}
