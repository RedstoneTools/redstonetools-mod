package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback.EVENT;

public class ReachFeature {
	public static final ReachFeature INSTANCE = new ReachFeature();

	protected ReachFeature() {
	}

	public void registerCommand() {
		EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(ClientCommandManager.literal("reach")
					.then(ClientCommandManager.argument("reach", FloatArgumentType.floatArg(0.0f))
							.executes(this::execute)));

			dispatcher.register(ClientCommandManager.literal("reach-block")
					.then(ClientCommandManager.argument("reach", FloatArgumentType.floatArg(0.0f))
							.executes(context -> {
								float reach = FloatArgumentType.getFloat(context, "reach");

								context.getSource().getPlayer().networkHandler.sendChatCommand("attribute @s minecraft:block_interaction_range base set " + reach);
								return 1;
							})));

			dispatcher.register(ClientCommandManager.literal("reach-entity")
					.then(ClientCommandManager.argument("reach", FloatArgumentType.floatArg(0.0f))
							.executes(context -> {
								float reach = FloatArgumentType.getFloat(context, "reach");
								context.getSource().getPlayer().networkHandler.sendChatCommand("attribute @s minecraft:entity_interaction_range base set " + reach);
								return 1;
							})));

		});
	}

	private int execute(CommandContext<FabricClientCommandSource> context) {
		float reach = FloatArgumentType.getFloat(context, "reach");
		context.getSource().getPlayer().networkHandler.sendChatCommand("attribute @s minecraft:entity_interaction_range base set " + reach);
		context.getSource().getPlayer().networkHandler.sendChatCommand("attribute @s minecraft:block_interaction_range base set " + reach);
		return 0;
	}
}
