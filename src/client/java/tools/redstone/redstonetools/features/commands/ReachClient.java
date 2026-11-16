package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.world.entity.ai.attributes.Attributes;
import tools.redstone.redstonetools.ClientCommands;

//? if >=26.1 {
import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;
//? } else {
/*import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
*///? }


public class ReachClient {
	public static final ReachClient INSTANCE = new ReachClient();

	protected ReachClient() {
	}

	public void registerCommand(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext registryAccess) {
			dispatcher.register(literal("reach")
				.requires(ClientCommands.PERMISSION_LEVEL_2)
				.then(argument("reach", FloatArgumentType.floatArg(0.0f))
					.executes(context -> execute(context, true, true))
				)
				.then(literal("reset")
					.executes(context -> reset(context, true, true))
				)
				.then(literal("block")
					.then(argument("reach", FloatArgumentType.floatArg(0.0f))
						.suggests((context, builder) -> {
							builder.suggest(String.valueOf(Attributes.BLOCK_INTERACTION_RANGE.value().getDefaultValue()));
							return builder.buildFuture();
						})
						.executes(context -> execute(context, true, false))
					)
				)
				.then(literal("entity")
					.then(argument("reach", FloatArgumentType.floatArg(0.0f))
						.suggests((context, builder) -> {
							builder.suggest(String.valueOf(Attributes.ENTITY_INTERACTION_RANGE.value().getDefaultValue()));
							return builder.buildFuture();
						})
						.executes(context -> execute(context, false, true))
					)
				));
	}

	private int execute(CommandContext<FabricClientCommandSource> context, boolean block, boolean entity) {
		float reach = FloatArgumentType.getFloat(context, "reach");
		ClientPacketListener networkHandler = context.getSource().getPlayer().connection;
		if (block) networkHandler.sendCommand("attribute @s minecraft:block_interaction_range base set " + reach);
		if (entity) networkHandler.sendCommand("attribute @s minecraft:entity_interaction_range base set " + reach);
		return 0;
	}

	private int reset(CommandContext<FabricClientCommandSource> context, boolean block, boolean entity) {
		ClientPacketListener networkHandler = context.getSource().getPlayer().connection;
		if (block) networkHandler.sendCommand("attribute @s minecraft:block_interaction_range base reset");
		if (entity) networkHandler.sendCommand("attribute @s minecraft:entity_interaction_range base reset");
		return 0;
	}
}
