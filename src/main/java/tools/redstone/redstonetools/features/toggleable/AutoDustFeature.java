package tools.redstone.redstonetools.features.toggleable;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.literal;

public class AutoDustFeature extends ToggleableFeature {
	public static final AutoDustFeature INSTANCE = new AutoDustFeature();

	protected AutoDustFeature() {
	}

	public void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
			dispatcher.register(literal("autodust").executes(this::execute));
	}

	private int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return this.toggle(context);
	}

	@Override
	public String getName() {
		return "AutoDust";
	}
}
