package tools.redstone.redstonetools.mixin.features;

import net.fabricmc.fabric.impl.command.client.ClientCommandInternals;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tools.redstone.redstonetools.utils.StringUtils;

@Mixin(value = ClientCommandInternals.class, remap = false)
public class ClientCommandInternalsMixin {
	@ModifyVariable(method = "executeCommand", at = @At("HEAD"), argsOnly = true)
	private static String maawrwww(String command) {
		System.out.println("command = " + command);
		command = StringUtils.insertVariablesAndMath(command);
		System.out.println("new command = " + command);
		return command;
	}

	@Inject(method = "executeCommand", at = @At("HEAD"), cancellable = true)
	private static void executeCommand(String command, CallbackInfoReturnable<Boolean> cir) {
		command = StringUtils.insertVariablesAndMath(command);
		// until fabric api fixes this (see the TODO comment in the method we're mixin in to) this is fine
		// TODO: this is wrong. if rst isnt on the server, /g will fail even though theres a client sided replacement
		if (command.startsWith("g ") ||
			command.startsWith("base") ||
			command.startsWith("quicktp") ||
			command.startsWith("reach")) {
			cir.setReturnValue(false);
		}
	}
}
