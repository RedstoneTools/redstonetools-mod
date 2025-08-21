package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import tools.redstone.redstonetools.features.commands.argument.MacroArgumentType;
import tools.redstone.redstonetools.malilib.widget.MacroBase;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback.EVENT;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;

public class MacroFeature {
	public static final MacroFeature INSTANCE = new MacroFeature();

	protected MacroFeature() {
	}

	public void registerCommand() {
		EVENT.register((dispatcher, registryAccess) -> dispatcher.register(literal("macro")
				.then(argument("macro", MacroArgumentType.macro())
						.executes(this::execute))));
	}

	protected int execute(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
		MacroBase macro = MacroArgumentType.getMacro(context, "macro");
		macro.run();
		return 1;
	}
}
