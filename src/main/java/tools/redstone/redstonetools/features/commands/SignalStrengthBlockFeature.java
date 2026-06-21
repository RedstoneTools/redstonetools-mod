package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import tools.redstone.redstonetools.Commands;
import tools.redstone.redstonetools.utils.ArgumentUtils;
import tools.redstone.redstonetools.utils.SignalBlock;

import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class SignalStrengthBlockFeature {
	public static final SignalStrengthBlockFeature INSTANCE = new SignalStrengthBlockFeature();

	protected SignalStrengthBlockFeature() {
	}

	public void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, net.minecraft.commands.Commands.CommandSelection registrationEnvironment) {
			dispatcher.register(literal("ssb")
				.requires(Commands.PERMISSION_LEVEL_2)
				.executes(this::parseArguments)
				.then(argument("signalStrength", IntegerArgumentType.integer())
						.executes(this::parseArguments)
						.then(argument("block", StringArgumentType.string()).suggests(ArgumentUtils.SIGNAL_BLOCK_SUGGESTION_PROVIDER)
								.executes(this::parseArguments))));
	}

	protected int parseArguments(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		int signalStrength;
		SignalBlock block;
		try {
			signalStrength = IntegerArgumentType.getInteger(context, "signalStrength");
		} catch (Exception ignored) {
			signalStrength = 15;
		}
		try {
			block = ArgumentUtils.parseSignalBlock(context, "block");
		} catch (Exception ignored) {
			block = SignalBlock.AUTO;
		}
		return execute(context, signalStrength, block);
	}

	protected int execute(CommandContext<CommandSourceStack> context, int signalStrength, SignalBlock block) throws CommandSyntaxException {
		try {
			var playerInventory = Objects.requireNonNull(context.getSource().getPlayer()).getInventory();
			ItemStack itemStack = block.getItemStack(signalStrength);
			playerInventory.add(itemStack);
//			playerInventory.swapStackWithHotbar(itemStack);
		} catch (IllegalArgumentException | IllegalStateException | NullPointerException e) {
			throw new SimpleCommandExceptionType(Component.literal(e.getMessage())).create();
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
			context.getSource().sendSystemMessage(Component.literal(funny[new Random().nextInt(funny.length)]));
			return 1;
		}

		return 1;
	}

}
