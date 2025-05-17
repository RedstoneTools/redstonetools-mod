package tools.redstone.redstonetools.mixin.gamerules;

import net.minecraft.inventory.Inventory;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static tools.redstone.redstonetools.RedstoneToolsGameRules.DO_CONTAINER_DROPS;

@Mixin(ItemScatterer.class)
public class DoContainerDropsMixin {
    @Inject(method = "spawn(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/inventory/Inventory;)V", at = @At("HEAD"), cancellable = true)
    private static void spawn(World world, BlockPos pos, Inventory inventory, CallbackInfo ci) {
        if (!world.getServer().getGameRules().getBoolean(DO_CONTAINER_DROPS)) ci.cancel();
    }
}
