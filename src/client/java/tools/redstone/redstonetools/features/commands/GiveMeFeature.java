package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.command.argument.ItemStackArgumentType;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.utils.ClientFeatureUtils;
import tools.redstone.redstonetools.utils.FeatureUtils;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback.EVENT;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;

public class GiveMeFeature extends AbstractFeature {
	public static void registerCommand() {
		GiveMeFeature giveMeFeature = ClientFeatureUtils.getFeature(GiveMeFeature.class);
		EVENT.register((dispatcher, registryAccess) -> dispatcher.register(
			literal("g")
				.then(argument("item", ItemStackArgumentType.itemStack(registryAccess))
					.executes(context -> giveMeFeature.execute(
						context,
						registryAccess,
						ItemStackArgumentType.getItemStackArgument(context, "item"),
						1))
					.then(argument("count", IntegerArgumentType.integer(1))
						.executes(context -> giveMeFeature.execute(
							context,
							registryAccess,
							ItemStackArgumentType.getItemStackArgument(context, "item"),
							IntegerArgumentType.getInteger(context, "count")))))));
	}

	private int execute(CommandContext<FabricClientCommandSource> context, CommandRegistryAccess registryAccess, ItemStackArgument itemArgument, int count) {
		context.getSource().getPlayer().networkHandler.sendChatCommand(
			"give @s " + itemArgument.asString(registryAccess) + " " + count
		);
		return 0;
	}
}
