package tools.redstone.redstonetools.mixin.features;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import tools.redstone.redstonetools.utils.StringUtils;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
	@ModifyVariable(method = "sendChatCommand", at = @At("HEAD"), argsOnly = true)
	public String sendChatCommand(String command) {
		System.out.println(command);
		command = StringUtils.insertVariables(command);
		System.out.println(command);
		return command;
	}
}
