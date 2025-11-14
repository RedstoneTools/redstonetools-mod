package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class ReachFeature {
	public static final ReachFeature INSTANCE = new ReachFeature();

	protected ReachFeature() {
	}

	public void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
			dispatcher.register(CommandManager.literal("reach")
				.requires(source -> source.hasPermissionLevel(2))
				.then(CommandManager.argument("reach", FloatArgumentType.floatArg(0.0f))
					.executes(context -> execute(context, true, true))
				)
				.then(CommandManager.literal("reset")
					.executes(context -> reset(context, true, true))
				)
				.then(CommandManager.literal("block")
					.then(CommandManager.argument("reach", FloatArgumentType.floatArg(0.0f))
						.suggests((context, builder) -> {
							builder.suggest(String.valueOf(EntityAttributes.BLOCK_INTERACTION_RANGE.value().getDefaultValue()));
							return builder.buildFuture();
						})
						.executes(context -> execute(context, true, false))
					)
				)
				.then(CommandManager.literal("entity")
					.then(CommandManager.argument("reach", FloatArgumentType.floatArg(0.0f))
						.suggests((context, builder) -> {
							builder.suggest(String.valueOf(EntityAttributes.ENTITY_INTERACTION_RANGE.value().getDefaultValue()));
							return builder.buildFuture();
						})
						.executes(context -> execute(context, false, true))
					)
				));
	}

	private int execute(CommandContext<ServerCommandSource> context, boolean block, boolean entity) {
		float reach = FloatArgumentType.getFloat(context, "reach");
		if (block) execute(context, "/attribute @s minecraft:block_interaction_range base set " + reach);
		if (entity) execute(context, "/attribute @s minecraft:entity_interaction_range base set " + reach);
		return 0;
	}

	private int reset(CommandContext<ServerCommandSource> context, boolean block, boolean entity) {
		if (block) execute(context, "/attribute @s minecraft:block_interaction_range base reset");
		if (entity) execute(context, "/attribute @s minecraft:entity_interaction_range base reset");
		return 0;
	}

	private static void execute(CommandContext<ServerCommandSource> context, String command) {
		context.getSource().getServer().getCommandManager()
				./*? if <1.21.10 {*//*executeWithPrefix*//*?} else {*/parseAndExecute/*?}*/(context.getSource(), command);
	}
}
