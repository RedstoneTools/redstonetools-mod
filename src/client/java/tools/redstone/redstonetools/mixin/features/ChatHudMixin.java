package tools.redstone.redstonetools.mixin.features;

import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tools.redstone.redstonetools.malilib.config.MacroManager;

@Mixin(ChatHud.class)
public class ChatHudMixin {
	@Inject(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("HEAD"), cancellable = true)
	private void injected(Text message, CallbackInfo ci) {
		if (MacroManager.shouldMute) ci.cancel();
	}
}
