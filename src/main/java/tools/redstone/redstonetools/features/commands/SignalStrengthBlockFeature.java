package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.features.commands.argument.SignalBlockArgumentType;
import tools.redstone.redstonetools.utils.SignalBlock;

import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SignalStrengthBlockFeature {
	public static final SignalStrengthBlockFeature INSTANCE = new SignalStrengthBlockFeature();

	protected SignalStrengthBlockFeature() {
	}

	public void registerCommand() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("ssb")
				.executes(this::parseArguments)
				.then(argument("signalStrength", IntegerArgumentType.integer())
						.executes(this::parseArguments)
						.then(argument("block", SignalBlockArgumentType.signalblock())
								.executes(this::parseArguments)))));
	}

	protected int parseArguments(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		int signalStrength;
		SignalBlock block;
		try {
			signalStrength = IntegerArgumentType.getInteger(context, "signalStrength");
		} catch (Exception ignored) {
			signalStrength = 15;
		}
		try {
			block = SignalBlockArgumentType.getSignalBlock(context, "block");
		} catch (Exception ignored) {
			block = SignalBlock.AUTO;
		}
		return execute(context, signalStrength, block);
	}

	protected int execute(CommandContext<ServerCommandSource> context, int signalStrength, SignalBlock block) throws CommandSyntaxException {
		try {
			var playerInventory = Objects.requireNonNull(context.getSource().getPlayer()).getInventory();
			ItemStack itemStack = block.getItemStack(signalStrength);
			playerInventory.insertStack(itemStack);
//			playerInventory.swapStackWithHotbar(itemStack);
		} catch (IllegalArgumentException | IllegalStateException | NullPointerException e) {
			throw new SimpleCommandExceptionType(Text.literal(e.getMessage())).create();
		}

		// who intentionally doesnt put a space between the last / and the message??
		//funny
		if (signalStrength == 0) {
			String[] funny = {
					"Why would you want this??", "Wtf are you going to use this for?", "What for?",
					"... Ok, if you're sure.", "I'm 99% sure you could just use any other block.",
					"This seems unnecessary.", "Is that a typo?", "Do you just like the glint?",
					"Wow, what a fancy but otherwise useless " + block.name().toLowerCase(Locale.ROOT).replace("_", " ") + "."
					, "For decoration?"};
			context.getSource().sendMessage(Text.literal(funny[new Random().nextInt(funny.length)]));
			return 1;
		}

		return 1;
	}

}
