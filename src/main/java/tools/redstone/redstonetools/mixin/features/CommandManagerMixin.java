package tools.redstone.redstonetools.mixin.features;

import com.mojang.brigadier.ParseResults;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tools.redstone.redstonetools.features.commands.ItemBindFeature;

@Mixin(Commands.class)
public class CommandManagerMixin {
	@Inject(method = "performCommand", at = @At("HEAD"), cancellable = true)
	public void checkItemBind(ParseResults<CommandSourceStack> parseResults, String command, CallbackInfo ci) {
		ServerPlayer player = parseResults.getContext().getSource().getPlayer();
		if (ItemBindFeature.waitingForCommandForPlayer(player)) {
			ItemBindFeature.addCommand(command, player);
			ItemBindFeature.playersWaitingForCommand.remove(player);
			ci.cancel();
		}
	}
}
