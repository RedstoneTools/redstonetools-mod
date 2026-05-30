package tools.redstone.redstonetools.mixin.features;

import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tools.redstone.redstonetools.config.MacroManager;

@Mixin(ChatComponent.class)
public class ChatHudMixin {
	@Inject(method = "addMessage(Lnet/minecraft/network/chat/Component;)V", at = @At("HEAD"), cancellable = true)
	private void injected(Component message, CallbackInfo ci) {
		if (MacroManager.shouldMute) ci.cancel();
	}
}
