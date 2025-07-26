package tools.redstone.redstonetools.mixin.features;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tools.redstone.redstonetools.RedstoneToolsClient;
import tools.redstone.redstonetools.features.toggleable.AirPlaceFeature;

@Mixin(PlayerEntity.class)
public class AirPlaceServerMixin {

    @Unique
    private final AirPlaceFeature airPlaceFeature = RedstoneToolsClient.INJECTOR.getInstance(AirPlaceFeature.class);

    @Inject(method = "getBlockInteractionRange", at = @At("TAIL"), cancellable = true)
    private void getBlockInteractionRange(CallbackInfoReturnable<Double> cir) {
        double originalValue = cir.getReturnValueD();

        if (airPlaceFeature.isEnabled()) {
            cir.setReturnValue((double) AirPlaceFeature.reach.getValue() + 1);
        } else {
            cir.setReturnValue(originalValue);
        }
    }

}
