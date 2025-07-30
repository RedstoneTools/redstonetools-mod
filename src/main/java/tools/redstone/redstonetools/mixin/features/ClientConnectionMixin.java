package tools.redstone.redstonetools.mixin.features;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.CommandExecutionC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tools.redstone.redstonetools.features.commands.ItemBindFeature;
import tools.redstone.redstonetools.utils.FeatureUtils;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    private static ItemBindFeature itemBindFeature = FeatureUtils.getFeature(ItemBindFeature.class);
    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void channelRead0(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci) {
        if (packet instanceof CommandExecutionC2SPacket commandExecutionC2SPacket) {
            if (itemBindFeature.waitingForCommand) {
                itemBindFeature.addCommand(commandExecutionC2SPacket.command());
                ItemBindFeature.waitingForCommand = false;
                ci.cancel();
            } else {
                return;
            }
        }
    }
}
