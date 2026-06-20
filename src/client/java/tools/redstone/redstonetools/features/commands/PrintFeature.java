package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.network.chat.Component;

//? if >=26.1 {
/*import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;
*///? } else {
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
//? }

public class PrintFeature {
	public static final PrintFeature INSTANCE = new PrintFeature();

	public void registerCommand(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext registryAccess) {
			dispatcher.register(literal("print")
				.then(argument("text", StringArgumentType.greedyString())
					.executes((context -> {
							context.getSource().sendFeedback(Component.literal(StringArgumentType.getString(context, "text")));
							return 1;
						})
					)
				)
		);
	}
}
