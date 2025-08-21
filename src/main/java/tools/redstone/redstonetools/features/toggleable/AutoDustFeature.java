package tools.redstone.redstonetools.features.toggleable;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.literal;

public class AutoDustFeature extends ToggleableFeature {
	public static final AutoDustFeature INSTANCE = new AutoDustFeature();

	protected AutoDustFeature() {
	}

	public void registerCommand() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
			dispatcher.register(literal("autodust").executes(this::execute)));
	}

	private int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return this.toggle(context);
	}

	@Override
	public String getName() {
		return "AutoDust";
	}
}
