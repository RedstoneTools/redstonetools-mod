package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback.EVENT;

public class PrintFeature {
	public static final PrintFeature INSTANCE = new PrintFeature();

	public void registerCommand() {
		EVENT.register((dispatcher, registryAccess) ->
			dispatcher.register(literal("print")
				.then(argument("text", StringArgumentType.greedyString())
					.executes((context -> {
						MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of(
							StringArgumentType.getString(context, "text")
						));
						return 1;
						})
					)
				)
			)
		);
	}
}
