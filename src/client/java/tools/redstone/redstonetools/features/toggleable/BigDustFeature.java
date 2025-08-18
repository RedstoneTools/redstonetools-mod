package tools.redstone.redstonetools.features.toggleable;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import tools.redstone.redstonetools.utils.ClientFeatureUtils;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class BigDustFeature extends ClientToggleableFeature {
	public static void registerCommand() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(literal("bigdust")
				.executes(context -> ClientFeatureUtils.getFeature(BigDustFeature.class).toggle(context))
				.then(argument("heightInPixels", IntegerArgumentType.integer(1, 16))
						.executes(context -> ClientFeatureUtils.getFeature(BigDustFeature.class).toggle(context)))));
	}

	public static int heightInPixels;

	@Override

	public int toggle(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
		boolean hasDifferentArguments = true;
		int o;
		try {
			o = IntegerArgumentType.getInteger(context, "heightInPixels");
			if (o == BigDustFeature.heightInPixels) {
				return super.toggle(context);
			}
		} catch (Exception e) {
			o = 3;
			hasDifferentArguments = false;
		}
		heightInPixels = o;
		if (hasDifferentArguments && isEnabled()) {
			return 1;
		}
		return super.toggle(context);
	}
}
