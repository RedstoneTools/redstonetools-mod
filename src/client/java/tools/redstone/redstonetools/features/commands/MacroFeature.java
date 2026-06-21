package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.commands.CommandBuildContext;
import tools.redstone.redstonetools.config.option.ConfigMacro;
import tools.redstone.redstonetools.features.commands.argument.MacroArgumentType;

//? if >=26.1 {
/*import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;
*///? } else {
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
//? }

public class MacroFeature {
	public static final MacroFeature INSTANCE = new MacroFeature();

	protected MacroFeature() {
	}

	public void registerCommand(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext registryAccess) {
			dispatcher.register(literal("macro")
				.then(argument("macro", MacroArgumentType.macro())
					.executes(this::execute)));
			dispatcher.register(literal("m")
				.then(argument("macro", MacroArgumentType.macro())
					.executes(this::execute)));
	}

	protected int execute(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
		ConfigMacro macro = MacroArgumentType.getMacro(context, "macro");
		macro.run();
		return 1;
	}
}
