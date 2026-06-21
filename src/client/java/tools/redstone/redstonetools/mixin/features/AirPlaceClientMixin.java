package tools.redstone.redstonetools.mixin.features;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tools.redstone.redstonetools.features.toggleable.AirPlaceFeature;

@Mixin(Minecraft.class)
public class AirPlaceClientMixin {
	@Shadow
	public HitResult hitResult;

	@Inject(method = "startUseItem", at = @At(value = "HEAD"))
	public void doItemUse(CallbackInfo callbackInfo) {
		if (!isAirPlaceAllowed()) {
			return;
		}

		hitResult = AirPlaceFeature.findAirPlaceBlockHit(getPlayer());
	}

	@Inject(method = "startAttack", at = @At(value = "HEAD"))
	public void doAttack(CallbackInfoReturnable<Boolean> cir) {
		if (!isAirPlaceAllowed()) {
			return;
		}

		// Call interactionManager directly because the block is air, with which the player cannot interact
		var hit = AirPlaceFeature.findAirPlaceBlockHit(getPlayer());
		getInteractionManager().startDestroyBlock(hit.getBlockPos(), hit.getDirection());
	}

	@Unique
	private boolean isAirPlaceAllowed() {
		// If air place is disabled
		if (!AirPlaceFeature.INSTANCE.isEnabled()) {
			return false;
		}

		// If the hit result is already set
		if (hitResult != null && hitResult.getType() != HitResult.Type.MISS) {
			return false;
		}

		// If blockpos isn't air
		if (getMinecraftClient().level != null && !getMinecraftClient().level.getBlockState(AirPlaceFeature.findAirPlaceBlockHit(getPlayer()).getBlockPos()).isAir()) {
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
	private Minecraft getMinecraftClient() {
		return (Minecraft) (Object) this;
	}

	@Unique
	private LocalPlayer getPlayer() {
		return getMinecraftClient().player;
	}

	@Unique
	private MultiPlayerGameMode getInteractionManager() {
		return getMinecraftClient().gameMode;
	}

}
