package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback.EVENT;

public class ClientDataFeature {
	public Map<String, String> variables = new HashMap<>();
	public static final ClientDataFeature INSTANCE = new ClientDataFeature();

	public void registerCommand() {
		EVENT.register((dispatcher, registryAccess) ->
			dispatcher.register(literal("clientdata")
				.then(literal("set")
					.then(argument("name", StringArgumentType.word())
						.then(argument("value", StringArgumentType.greedyString())
							.executes(context -> {
								String name = StringArgumentType.getString(context, "name");
								String value = StringArgumentType.getString(context, "value");
								variables.put(name, value);
								context.getSource().sendFeedback(Text.of("Set " + name + " to \"" + value + "\""));
								return 1;
							})
						)
					)
				)
				.then(literal("delete")
					.then(argument("name", StringArgumentType.word())
						.executes(context -> {
								variables.remove(StringArgumentType.getString(context, "name"));
								return 1;
							}
						)
					)
				)
			)
		);
	}
}
