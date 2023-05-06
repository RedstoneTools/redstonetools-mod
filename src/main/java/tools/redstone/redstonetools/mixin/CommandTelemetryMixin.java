package tools.redstone.redstonetools.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tools.redstone.redstonetools.utils.TelemetryUtils;

import java.util.Objects;

@Mixin(net.minecraft.server.command.CommandManager.class)
public class CommandTelemetryMixin {
    @Inject(method = "execute", at = @At("HEAD"))
    private void execute(net.minecraft.server.command.ServerCommandSource source, String command, CallbackInfoReturnable<Integer> cir) {
        ServerPlayerEntity player;
        try {
            player = source.getPlayer();
        } catch (CommandSyntaxException ignored) {
            return;
        }

        if (player.getUuid().equals(Objects.requireNonNull(MinecraftClient.getInstance().player).getUuid())) {
            return;
        }

        TelemetryUtils.sendCommand(command);
    }
}
