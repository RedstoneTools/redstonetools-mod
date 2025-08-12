package tools.redstone.redstonetools.features.toggleable;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import tools.redstone.redstonetools.packets.SetFeatureEnabledS2CPayload;
import tools.redstone.redstonetools.utils.FeatureUtils;

import static net.minecraft.server.command.CommandManager.literal;

public class AutoRotateFeature extends ToggleableFeature {

	public static void registerCommand() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
				dispatcher.register(literal("autorotate")
						.executes(context -> FeatureUtils.getFeature(AutoRotateFeature.class).execute(context))
				)
		);
	}

	private int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
		int result = this.toggle(context);
		boolean enabled = this.isEnabled(player);
		var payload = new SetFeatureEnabledS2CPayload("AutoRotate" + (enabled ? "1" : "0"));
		ServerPlayNetworking.send(player, payload);
		return result;
	}
}
