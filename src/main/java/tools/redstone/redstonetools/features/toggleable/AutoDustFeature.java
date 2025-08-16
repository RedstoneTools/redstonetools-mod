package tools.redstone.redstonetools.features.toggleable;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import tools.redstone.redstonetools.utils.FeatureUtils;

import static net.minecraft.server.command.CommandManager.literal;

public class AutoDustFeature extends ToggleableFeature {
	public static void registerCommand() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("autodust")
				.executes(context -> FeatureUtils.getFeature(AutoDustFeature.class).execute(context))));
	}

	private int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return this.toggle(context);
	}

	@Override
	public String getName() {
		return "AutoDust";
	}
}
