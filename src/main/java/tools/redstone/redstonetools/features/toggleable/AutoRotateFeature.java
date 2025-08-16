package tools.redstone.redstonetools.features.toggleable;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import tools.redstone.redstonetools.utils.FeatureUtils;

import static net.minecraft.server.command.CommandManager.literal;

public class AutoRotateFeature extends ToggleableFeature {

	public static void registerCommand() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
				dispatcher.register(literal("autorotate")
						.executes(context -> FeatureUtils.getFeature(AutoRotateFeature.class).toggle(context))
				)
		);
	}

	@Override
	public String getName() {
		return "AutoRotate";
	}
}
