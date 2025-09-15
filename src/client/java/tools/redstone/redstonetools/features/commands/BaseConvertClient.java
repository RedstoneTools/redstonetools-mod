package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;


public class BaseConvertClient {
	public static final BaseConvertClient INSTANCE = new BaseConvertClient();

	protected BaseConvertClient() {
	}
	public void registerCommand(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
			dispatcher.register(
			ClientCommandManager.literal("base")
				.then(ClientCommandManager.argument("inputNum", StringArgumentType.word())
						.then(ClientCommandManager.argument("toBase", IntegerArgumentType.integer(2, 16))
								.executes(context -> BaseConvertFeature.INSTANCE.execute(
									StringArgumentType.getString(context, "inputNum"),
									IntegerArgumentType.getInteger(context, "toBase"),
									(t) -> context.getSource().sendFeedback(t)
									)))));
	}
}
