package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback.EVENT;

public class ClientDataFeature {
	public Map<String, String> variables = new HashMap<>();
	public Map<String, Integer> intVariables = new HashMap<>();
	public Map<String, Double> doubleVariables = new HashMap<>();
	public static final ClientDataFeature INSTANCE = new ClientDataFeature();

	public void registerCommand() {
		EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(literal("clientdata")
				.then(literal("set")
					.then(argument("name", StringArgumentType.word())
						.then(argument("value", StringArgumentType.string())
							.executes(context -> {
								String name = StringArgumentType.getString(context, "name");
								String value = StringArgumentType.getString(context, "value");
								variables.put(name, value);
								context.getSource().sendFeedback(Text.of("Set " + name + " to " + value));
								return 1;
							})
						)))
				.then(literal("setInt")
					.then(argument("name", StringArgumentType.word())
						.then(argument("value", IntegerArgumentType.integer())
							.executes(context -> {
								String name = StringArgumentType.getString(context, "name");
								Integer value = IntegerArgumentType.getInteger(context, "value");
								intVariables.put(name, value);
								return 1;
							})
						)))
				.then(literal("setDouble")
					.then(argument("name", StringArgumentType.word())
						.then(argument("value", DoubleArgumentType.doubleArg())
							.executes(context -> {
								String name = StringArgumentType.getString(context, "name");
								Double value = DoubleArgumentType.getDouble(context, "value");
								doubleVariables.put(name, value);
								return 1;
							})
						)))
			);
		});
	}
}
