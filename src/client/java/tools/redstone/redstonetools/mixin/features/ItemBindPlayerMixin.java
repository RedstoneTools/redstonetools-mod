package tools.redstone.redstonetools.mixin.features;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tools.redstone.redstonetools.features.commands.ItemBindFeature;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ItemBindPlayerMixin {
    @Inject(method = "sendChatCommand", at = @At("HEAD"), cancellable = true)
    public void injectCommand(String message, CallbackInfo ci) {
        if (!ItemBindFeature.waitingForCommand) return;
        try {
            ItemBindFeature.addCommand(message);
        } catch (CommandSyntaxException e) {
            assert MinecraftClient.getInstance().player != null;
            MinecraftClient.getInstance().player.sendMessage(Text.literal(e.getMessage()), false);
        }
        ItemBindFeature.waitingForCommand = false;
        ci.cancel();
    }
}