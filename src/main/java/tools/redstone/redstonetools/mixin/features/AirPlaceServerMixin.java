package tools.redstone.redstonetools.mixin.features;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import tools.redstone.redstonetools.RedstoneToolsClient;
import tools.redstone.redstonetools.features.toggleable.AirPlaceFeature;

@Mixin(ServerPlayNetworkHandler.class)
public class AirPlaceServerMixin {

    @Unique
    private final AirPlaceFeature airPlaceFeature = RedstoneToolsClient.INJECTOR.getInstance(AirPlaceFeature.class);

    @ModifyConstant(method = "onPlayerInteractBlock", constant = @Constant(doubleValue = 1.0))
    private double modifyConstant(double originalValue) {

        if (airPlaceFeature.isEnabled()) {
            return AirPlaceFeature.reach.getValue() + 1;
        } else {
            return originalValue;
        }

    }

}
