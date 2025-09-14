package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.math.BigInteger;
import java.util.Locale;
import java.util.function.Consumer;


public class BaseConvertFeature {
	public static final BaseConvertFeature INSTANCE = new BaseConvertFeature();

	protected BaseConvertFeature() {
	}

	public void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
			dispatcher.register(CommandManager.literal("base")
			.then(CommandManager.argument("inputNum", StringArgumentType.word())
				.then(CommandManager.argument("toBase", IntegerArgumentType.integer(2, 16))
					.executes(context -> BaseConvertFeature.INSTANCE.execute(
						StringArgumentType.getString(context, "inputNum"),
						IntegerArgumentType.getInteger(context, "toBase"),
						(t) -> {
							try {
								context.getSource().getPlayerOrThrow().sendMessage(t);
							} catch (CommandSyntaxException ignored) {
							}
						}
					)))));
	}

	private static final SimpleCommandExceptionType INVALID_NUMBER =
		new SimpleCommandExceptionType(Text.literal("Invalid number"));

	protected int execute(String number, int toBase, Consumer<Text> printToChat)
		throws com.mojang.brigadier.exceptions.CommandSyntaxException {

		int base = 10;
		number = number.toLowerCase(Locale.ROOT);
		String prefix = "";
		if (number.startsWith("0x")) {
			prefix = "0x";
			base = 16;
		} else if (number.startsWith("0o")) {
			prefix = "0o";
			base = 8;
		} else if (number.startsWith("0b")) {
			prefix = "0b";
			base = 2;
		}
		if (base != 10) number = number.substring(2);
		BigInteger integer;
		try {
			integer = new BigInteger(number, base);
		} catch (NumberFormatException e) {
			throw INVALID_NUMBER.create();
		}
		String output = integer.toString(toBase).toLowerCase(Locale.ROOT);

		String toPrefix = "";
		if (toBase == 16) {
			toPrefix = "0x";
		} else if (toBase == 8) {
			toPrefix = "0o";
		} else if (toBase == 2) {
			toPrefix = "0b";
		}
		if (!toPrefix.isEmpty()) {
			printToChat.accept(Text.literal("%s = %s".formatted(prefix + number, toPrefix + output)));
		} else {
			printToChat.accept(Text.literal("%s = %s in base %s".formatted(prefix + number, output, toBase)));
		}
		return 1;
	}
}
