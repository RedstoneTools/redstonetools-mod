package tools.redstone.redstonetools.features.toggleable;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import static net.minecraft.commands.Commands.literal;

public class AutoRotateFeature extends ToggleableFeature {
	public static final AutoRotateFeature INSTANCE = new AutoRotateFeature();

	protected AutoRotateFeature() {
	}

	public void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection registrationEnvironment) {
			dispatcher.register(literal("autorotate").requires(tools.redstone.redstonetools.Commands.getPerm("autorotate")).executes(this::toggle));
//			dispatcher.register(literal("ar").executes(this::toggle));
	}

	@Override
	public String getName() {
		return "AutoRotate";
	}
}
