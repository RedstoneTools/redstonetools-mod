package tools.redstone.redstonetools.features.toggleable;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import static net.minecraft.commands.Commands.literal;

public class AutoDustFeature extends ToggleableFeature {
	public static final AutoDustFeature INSTANCE = new AutoDustFeature();

	protected AutoDustFeature() {
	}

	public void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection registrationEnvironment) {
			dispatcher.register(literal("autodust").requires(tools.redstone.redstonetools.Commands.getPerm("autodust")).executes(this::toggle));
//			dispatcher.register(literal("autowire").executes(this::toggle));
//			dispatcher.register(literal("aw").executes(this::toggle));
	}

	@Override
	public String getName() {
		return "AutoDust";
	}
}
