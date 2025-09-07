package tools.redstone.redstonetools.mixin.features;

import net.fabricmc.fabric.impl.command.client.ClientCommandInternals;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientCommandInternals.class)
public class ClientCommandInternalsMixin {
	@Inject(method = "executeCommand", at = @At("HEAD"), remap = false, cancellable = true)
	private static void executeCommand(String command, CallbackInfoReturnable<Boolean> cir) {
		// until fabric api fixes this (see the TODO comment in the method we're mixin in to) this is fine
		if (command.startsWith("g ") ||
			command.startsWith("base") ||
			command.startsWith("quicktp") ||
			command.startsWith("reach")) {
			cir.setReturnValue(false);
		}
	}
}
