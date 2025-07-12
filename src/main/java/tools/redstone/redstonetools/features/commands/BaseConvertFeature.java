package tools.redstone.redstonetools.features.commands;

import com.google.auto.service.AutoService;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

import java.math.BigInteger;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback.EVENT;

public class BaseConvertFeature {

	private static final SimpleCommandExceptionType INVALID_NUMBER =
			new SimpleCommandExceptionType(Text.literal("Invalid number"));

	public static void registerCommand() {
		EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("base")
				.then(ClientCommandManager.argument("inputNum", IntegerArgumentType.integer())
				.then(ClientCommandManager.argument("toBase",   IntegerArgumentType.integer())
				.executes(BaseConvertFeature::execute)))));
	}

    protected static int execute(CommandContext<FabricClientCommandSource> context)
        throws com.mojang.brigadier.exceptions.CommandSyntaxException {
	    int inputNum = IntegerArgumentType.getInteger(context, "inputNum");
	    int toBase = IntegerArgumentType.getInteger(context, "toBase");

	    BigInteger value;
	    try {
		    value = new BigInteger(Integer.toString(inputNum));
	    } catch (NumberFormatException e) {
		    throw INVALID_NUMBER.create();
	    }
	    String output = value.toString(toBase).toUpperCase();
	    context.getSource().sendFeedback(Text.literal("%s = %s in base %s".formatted(inputNum, output, toBase)));
	    return 1;
    }
}
