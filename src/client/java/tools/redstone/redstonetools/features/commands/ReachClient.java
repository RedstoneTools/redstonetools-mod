package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.attribute.EntityAttributes;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback.EVENT;

public class ReachClient {
	public static final ReachClient INSTANCE = new ReachClient();

	protected ReachClient() {
	}

	public void registerCommand() {
		EVENT.register((dispatcher, registryAccess) ->
			dispatcher.register(ClientCommandManager.literal("reach")
				.requires(source -> source.getPlayer().hasPermissionLevel(2))
				.then(ClientCommandManager.argument("reach", FloatArgumentType.floatArg(0.0f))
					.executes(context -> execute(context, true, true))
				)
				.then(ClientCommandManager.literal("reset")
					.executes(context -> reset(context, true, true))
				)
				.then(ClientCommandManager.literal("block")
					.then(ClientCommandManager.argument("reach", FloatArgumentType.floatArg(0.0f))
						.suggests((context, builder) -> {
							builder.suggest(String.valueOf(EntityAttributes.BLOCK_INTERACTION_RANGE.value().getDefaultValue()));
							return builder.buildFuture();
						})
						.executes(context -> execute(context, true, false))
					)
				)
				.then(ClientCommandManager.literal("entity")
					.then(ClientCommandManager.argument("reach", FloatArgumentType.floatArg(0.0f))
						.suggests((context, builder) -> {
							builder.suggest(String.valueOf(EntityAttributes.ENTITY_INTERACTION_RANGE.value().getDefaultValue()));
							return builder.buildFuture();
						})
						.executes(context -> execute(context, false, true))
					)
				)));
	}

	private int execute(CommandContext<FabricClientCommandSource> context, boolean block, boolean entity) {
		float reach = FloatArgumentType.getFloat(context, "reach");
		ClientPlayNetworkHandler networkHandler = context.getSource().getPlayer().networkHandler;
		if (block) networkHandler.sendChatCommand("attribute @s minecraft:block_interaction_range base set " + reach);
		if (entity) networkHandler.sendChatCommand("attribute @s minecraft:entity_interaction_range base set " + reach);
		return 0;
	}

	private int reset(CommandContext<FabricClientCommandSource> context, boolean block, boolean entity) {
		ClientPlayNetworkHandler networkHandler = context.getSource().getPlayer().networkHandler;
		if (block) networkHandler.sendChatCommand("attribute @s minecraft:block_interaction_range base reset");
		if (entity) networkHandler.sendChatCommand("attribute @s minecraft:entity_interaction_range base reset");
		return 0;
	}
}
