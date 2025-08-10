package tools.redstone.redstonetools.mixin.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tools.redstone.redstonetools.features.toggleable.AirPlaceFeature;
import tools.redstone.redstonetools.utils.ClientFeatureUtils;

@Mixin(MinecraftClient.class)
public class AirPlaceClientMixin {
	@Unique
	private final AirPlaceFeature airPlaceFeature = ClientFeatureUtils.getFeature(AirPlaceFeature.class);
	@Shadow
	public HitResult crosshairTarget;

	@Inject(method = "doItemUse", at = @At(value = "HEAD"))
	public void doItemUse(CallbackInfo callbackInfo) {
		if (!isAirPlaceAllowed()) {
			return;
		}

		crosshairTarget = AirPlaceFeature.findAirPlaceBlockHit(getPlayer());
	}

	@Inject(method = "doAttack", at = @At(value = "HEAD"))
	public void doAttack(CallbackInfoReturnable<Boolean> cir) {
		if (!isAirPlaceAllowed()) {
			return;
		}

		// Call interactionManager directly because the block is air, with which the player cannot interact
		var hit = AirPlaceFeature.findAirPlaceBlockHit(getPlayer());
		getInteractionManager().attackBlock(hit.getBlockPos(), hit.getSide());
	}

	@Unique
	private boolean isAirPlaceAllowed() {
		// If air place is disabled
		if (!airPlaceFeature.isEnabled()) {
			return false;
		}

		// If the hit result is already set
		if (crosshairTarget != null && crosshairTarget.getType() != HitResult.Type.MISS) {
			return false;
		}

		// If the player or interactionManager not initialized
		if (getPlayer() == null || getInteractionManager() == null) {
			return false;
		}

		// If air place isn't possible with the current
		// player equipment and state
		return AirPlaceFeature.canAirPlace(getPlayer());
	}

	@Unique
	private MinecraftClient getMinecraftClient() {
		return (MinecraftClient) (Object) this;
	}

	@Unique
	private ClientPlayerEntity getPlayer() {
		return getMinecraftClient().player;
	}

	@Unique
	private ClientPlayerInteractionManager getInteractionManager() {
		return getMinecraftClient().interactionManager;
	}

}
