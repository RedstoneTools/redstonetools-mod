package com.domain.redstonetools.mixin;

import com.domain.redstonetools.utils.TelemetryUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.server.command.CommandManager.class)
public class CommandTelemetryMixin {
    @Inject(method = "execute", at = @At("HEAD"))
    private void execute(net.minecraft.server.command.ServerCommandSource source, String command, CallbackInfoReturnable<Integer> cir) {
        TelemetryUtils.sendCommand(command);
    }
}
