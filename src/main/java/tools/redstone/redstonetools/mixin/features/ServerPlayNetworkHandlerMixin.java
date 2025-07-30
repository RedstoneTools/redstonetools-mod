package tools.redstone.redstonetools.mixin.features;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tools.redstone.redstonetools.features.commands.ItemBindFeature;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @Inject(method = "executeCommand", at = @At("HEAD"), cancellable = true)
    public void checkItemBind(String command, CallbackInfo ci) {
        ServerPlayerEntity player = ((ServerPlayNetworkHandler)(Object)this).getPlayer();
        if (ItemBindFeature.waitingForCommandForPlayer(player)) {
            ItemBindFeature.addCommand(command, player);
            ItemBindFeature.playersWaitingForCommmand.remove(player);
            ci.cancel();
        }
    }
}
