package tools.redstone.redstonetools.mixin.features;

import com.mojang.brigadier.ParseResults;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tools.redstone.redstonetools.features.commands.ItemBindFeature;

@Mixin(CommandManager.class)
public class CommandManagerMixin {
	@Inject(method = "execute", at = @At("HEAD"), cancellable = true)
	public void checkItemBind(ParseResults<ServerCommandSource> parseResults, String command, CallbackInfo ci) {
		ServerPlayerEntity player = parseResults.getContext().getSource().getPlayer();
		if (ItemBindFeature.waitingForCommandForPlayer(player)) {
			ItemBindFeature.addCommand(command, player);
			ItemBindFeature.playersWaitingForCommand.remove(player);
			ci.cancel();
		}
	}
}
