package tools.redstone.redstonetools.features.toggleable;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.commands.CommandBuildContext;
import tools.redstone.redstonetools.config.Toggles;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class BigDustFeature extends ClientToggleableFeature {
	public static final BigDustFeature INSTANCE = new BigDustFeature();

	protected BigDustFeature() {
		super(Toggles.BIGDUST);
	}

	public void registerCommand(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext registryAccess) {
			dispatcher.register(literal("bigdust")
				.executes(this::toggle)
				.then(argument("heightInPixels", IntegerArgumentType.integer(1, 16))
						.executes(this::toggle)));
	}
}
