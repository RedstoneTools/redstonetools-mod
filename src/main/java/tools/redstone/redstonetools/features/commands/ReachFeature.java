package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.ai.attributes.Attributes;
import tools.redstone.redstonetools.Commands;

public class ReachFeature {
	public static final ReachFeature INSTANCE = new ReachFeature();

	protected ReachFeature() {
	}

	public void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, net.minecraft.commands.Commands.CommandSelection registrationEnvironment) {
			dispatcher.register(net.minecraft.commands.Commands.literal("reach")
				.requires(Commands.PERMISSION_LEVEL_2)
				.then(net.minecraft.commands.Commands.argument("reach", FloatArgumentType.floatArg(0.0f))
					.executes(context -> execute(context, true, true))
				)
				.then(net.minecraft.commands.Commands.literal("reset")
					.executes(context -> reset(context, true, true))
				)
				.then(net.minecraft.commands.Commands.literal("block")
					.then(net.minecraft.commands.Commands.argument("reach", FloatArgumentType.floatArg(0.0f))
						.suggests((context, builder) -> {
							builder.suggest(String.valueOf(Attributes.BLOCK_INTERACTION_RANGE.value().getDefaultValue()));
							return builder.buildFuture();
						})
						.executes(context -> execute(context, true, false))
					)
				)
				.then(net.minecraft.commands.Commands.literal("entity")
					.then(net.minecraft.commands.Commands.argument("reach", FloatArgumentType.floatArg(0.0f))
						.suggests((context, builder) -> {
							builder.suggest(String.valueOf(Attributes.ENTITY_INTERACTION_RANGE.value().getDefaultValue()));
							return builder.buildFuture();
						})
						.executes(context -> execute(context, false, true))
					)
				));
	}

	private int execute(CommandContext<CommandSourceStack> context, boolean block, boolean entity) {
		float reach = FloatArgumentType.getFloat(context, "reach");
		if (block) execute(context, "/attribute @s minecraft:block_interaction_range base set " + reach);
		if (entity) execute(context, "/attribute @s minecraft:entity_interaction_range base set " + reach);
		return 0;
	}

	private int reset(CommandContext<CommandSourceStack> context, boolean block, boolean entity) {
		if (block) execute(context, "/attribute @s minecraft:block_interaction_range base reset");
		if (entity) execute(context, "/attribute @s minecraft:entity_interaction_range base reset");
		return 0;
	}

	private static void execute(CommandContext<CommandSourceStack> context, String command) {
		context.getSource().getServer().getCommands()
				.performPrefixedCommand(context.getSource(), command);
	}
}
