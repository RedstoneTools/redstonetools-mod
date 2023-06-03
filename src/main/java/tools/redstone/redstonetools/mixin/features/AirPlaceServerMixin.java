package tools.redstone.redstonetools.mixin.features;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ServerPlayNetworkHandler.class)
public class AirPlaceServerMixin {

    @ModifyConstant(method = "onPlayerInteractBlock", constant = @Constant(doubleValue = 64.0) )
    private double modifyConstant(double originalValue) {
        return 999;
    }

}
