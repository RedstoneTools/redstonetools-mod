package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.utils.ClientFeatureUtils;

import java.math.BigInteger;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback.EVENT;

public class BaseConvertFeature extends AbstractFeature {
	private static final SimpleCommandExceptionType INVALID_NUMBER =
			new SimpleCommandExceptionType(Text.literal("Invalid number"));

	public static void registerCommand() {
		EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("base")
				.then(ClientCommandManager.argument("inputNum", StringArgumentType.word())
						.then(ClientCommandManager.argument("toBase", IntegerArgumentType.integer(2, 16))
								.executes(context -> ClientFeatureUtils.getFeature(BaseConvertFeature.class).execute(context))))));
	}

	protected int execute(CommandContext<FabricClientCommandSource> context)
			throws com.mojang.brigadier.exceptions.CommandSyntaxException {
		String number = StringArgumentType.getString(context, "inputNum");
		int toBase = IntegerArgumentType.getInteger(context, "toBase");

		int base = 10;
		number = number.toLowerCase();
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
		String output = integer.toString(toBase).toLowerCase();

		String toPrefix = "";
		if (toBase == 16) {
			toPrefix = "0x";
		} else if (toBase == 8) {
			toPrefix = "0o";
		} else if (toBase == 2) {
			toPrefix = "0b";
		}
		if (!toPrefix.isEmpty()) {
			context.getSource().sendFeedback(Text.literal("%s = %s".formatted(prefix + number, toPrefix + output)));
		} else {
			context.getSource().sendFeedback(Text.literal("%s = %s in base %s".formatted(prefix + number, output, toBase)));
		}
		return 1;
	}
}
