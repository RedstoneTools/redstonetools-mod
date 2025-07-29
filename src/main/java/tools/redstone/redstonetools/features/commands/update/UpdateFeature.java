package tools.redstone.redstonetools.features.commands.update;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.utils.FeatureUtils;
import tools.redstone.redstonetools.utils.WorldEditUtils;

import java.util.Objects;

import static net.minecraft.server.command.CommandManager.literal;

public class UpdateFeature extends AbstractFeature {
	public static void registerCommand() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("/update")
				.executes(context -> FeatureUtils.getFeature(UpdateFeature.class).execute(context))));
	}

	protected int execute(CommandContext<ServerCommandSource> context)
			throws com.mojang.brigadier.exceptions.CommandSyntaxException {
		var selection = WorldEditUtils.getSelection(context.getSource().getPlayer());

		assert Objects.requireNonNull(context.getSource().getPlayer()).getWorld() != null;
		context.getSource().sendMessage(RegionUpdater.updateRegion(context.getSource().getPlayer().getWorld(), selection.getMinimumPoint(), selection.getMaximumPoint()));
		return 1;
    }
}