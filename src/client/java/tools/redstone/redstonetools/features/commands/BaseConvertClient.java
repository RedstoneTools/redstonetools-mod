package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.commands.CommandBuildContext;

//? if >=26.1 {
/*import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;
*///? } else {
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
//? }

public class BaseConvertClient {
	public static final BaseConvertClient INSTANCE = new BaseConvertClient();

	protected BaseConvertClient() {
	}
	public void registerCommand(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext registryAccess) {
			dispatcher.register(
			literal("base")
				.then(argument("inputNum", StringArgumentType.word())
						.then(argument("toBase", IntegerArgumentType.integer(2, 16))
								.executes(context -> BaseConvertFeature.INSTANCE.execute(
									StringArgumentType.getString(context, "inputNum"),
									IntegerArgumentType.getInteger(context, "toBase"),
									(t) -> context.getSource().sendFeedback(t)
									)))));
	}
}
