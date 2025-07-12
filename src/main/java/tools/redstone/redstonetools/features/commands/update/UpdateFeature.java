package tools.redstone.redstonetools.features.commands.update;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.utils.WorldEditUtils;

import java.util.Objects;

import static net.minecraft.server.command.CommandManager.literal;

public class UpdateFeature {
	public static void registerCommand() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("update")
				.executes(context -> new UpdateFeature().execute(context))));
	}

	protected int execute(CommandContext<ServerCommandSource> context)
			throws com.mojang.brigadier.exceptions.CommandSyntaxException {
		var selectionOrFeedback = WorldEditUtils.getSelection(context.getSource().getPlayer());
		if (selectionOrFeedback.right().isPresent()) {
			throw new SimpleCommandExceptionType(Text.literal("No selection!")).create();
		}

		assert selectionOrFeedback.left().isPresent();
		var selection = selectionOrFeedback.left().get();

		assert Objects.requireNonNull(context.getSource().getPlayer()).getWorld() != null;
		context.getSource().sendMessage(RegionUpdater.updateRegion(context.getSource().getPlayer().getWorld(), selection.getMinimumPoint(), selection.getMaximumPoint()));
		return 1;
    }
}