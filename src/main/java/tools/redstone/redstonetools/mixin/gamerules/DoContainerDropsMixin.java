package tools.redstone.redstonetools.mixin.gamerules;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tools.redstone.redstonetools.RedstoneToolsGameRules;

@Mixin(Containers.class)
public abstract class DoContainerDropsMixin {
	@Inject(method = "dropItemStack(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/item/ItemStack;)V", at = @At("HEAD"), cancellable = true)
	private static void preventSpawning(Level world, double x, double y, double z, ItemStack stack, CallbackInfo ci) {
		if (world instanceof ServerLevel serverWorld) {
			//? if <=1.21.10 {
			if (!serverWorld.getGameRules().getBoolean(RedstoneToolsGameRules.DO_CONTAINER_DROPS)) ci.cancel();
			//?} else {
			/*if (!serverWorld.getGameRules().get(RedstoneToolsGameRules.DO_CONTAINER_DROPS)) ci.cancel();
			*///?}
		}
	}
}
