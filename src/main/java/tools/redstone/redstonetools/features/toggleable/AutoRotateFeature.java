package tools.redstone.redstonetools.features.toggleable;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import static net.minecraft.server.command.CommandManager.literal;

public class AutoRotateFeature extends ToggleableFeature {
	public static final AutoRotateFeature INSTANCE = new AutoRotateFeature();

	protected AutoRotateFeature() {
	}

	public void registerCommand() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
			dispatcher.register(literal("autorotate").executes(this::toggle)));
	}

	@Override
	public String getName() {
		return "AutoRotate";
	}
}
