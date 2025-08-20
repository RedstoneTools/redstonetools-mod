package tools.redstone.redstonetools.features.toggleable;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
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
}
