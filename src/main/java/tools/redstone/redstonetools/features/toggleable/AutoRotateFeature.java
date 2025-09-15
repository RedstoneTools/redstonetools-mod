package tools.redstone.redstonetools.features.toggleable;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.literal;

public class AutoRotateFeature extends ToggleableFeature {
	public static final AutoRotateFeature INSTANCE = new AutoRotateFeature();

	protected AutoRotateFeature() {
	}

	public void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
			dispatcher.register(literal("autorotate").executes(this::toggle));
	}

	@Override
	public String getName() {
		return "AutoRotate";
	}
}
