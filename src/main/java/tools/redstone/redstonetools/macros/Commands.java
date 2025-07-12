package tools.redstone.redstonetools.macros;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import fi.dy.masa.malilib.gui.GuiBase;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.features.commands.BaseConvertFeature;
import tools.redstone.redstonetools.features.commands.update.RegionUpdater;
import tools.redstone.redstonetools.macros.gui.malilib.GuiMacroManager;
import tools.redstone.redstonetools.utils.WorldEditUtils;

import java.math.BigInteger;
import java.util.Objects;

import static net.minecraft.server.command.CommandManager.*;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback.*;

public class Commands {
	// errors we can throw
	private static final SimpleCommandExceptionType INVALID_NUMBER =
			new SimpleCommandExceptionType(Text.literal("Invalid number"));

	public static void registerCommands() {
		// client side commands
		EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("edit-macros")
				.executes(commandContext -> {
					GuiBase.openGui(new GuiMacroManager());
					return 1;
				})));

		BaseConvertFeature.registerCommand();

		EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("colored")
				.executes(commandContext -> {
					GuiBase.openGui(new GuiMacroManager());
					return 1;
				})));
		EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("base")
			.then(ClientCommandManager.argument("inputNum", IntegerArgumentType.integer()))
			.then(ClientCommandManager.argument("toBase", IntegerArgumentType.integer()))
			.executes(context -> {
				int inputNum = IntegerArgumentType.getInteger(context, "inputNum");
				int toBase = IntegerArgumentType.getInteger(context, "toBase");

				BigInteger value;
				try {
					value = new BigInteger(Integer.toString(inputNum));
				} catch (NumberFormatException e) {
					throw INVALID_NUMBER.create();
				}
				String output = value.toString(toBase).toUpperCase(); // uppercase for A–Z
				context.getSource().sendFeedback(Text.literal("%s = %s in base %s".formatted(inputNum, output, toBase)));
				return 1;
			})));



		// server side commands
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("update")
			.executes(context -> {
				var selectionOrFeedback = WorldEditUtils.getSelection(context.getSource().getPlayer());
				if (selectionOrFeedback.right().isPresent()) {
					throw new SimpleCommandExceptionType(Text.literal("No selection!")).create();
				}

				assert selectionOrFeedback.left().isPresent();
				var selection = selectionOrFeedback.left().get();

				assert Objects.requireNonNull(context.getSource().getPlayer()).getWorld() != null;
				context.getSource().sendMessage(RegionUpdater.updateRegion(context.getSource().getPlayer().getWorld(), selection.getMinimumPoint(), selection.getMaximumPoint()));
				return 1;
			})));
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("colorcode")
				.executes(context -> {
					return 1;
				})));
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("test")
				.executes(context -> {
					return 1;
				})));
	}
}
