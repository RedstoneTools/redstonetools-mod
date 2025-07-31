package tools.redstone.redstonetools.mixin.gamerules;

import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ItemScatterer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tools.redstone.redstonetools.RedstoneToolsGameRules;

import static tools.redstone.redstonetools.RedstoneToolsGameRules.DO_CONTAINER_DROPS;

@Mixin(ItemScatterer.class)
public class DoContainerDropsMixin {
    @Inject(method = "spawn(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)V", at = @At("HEAD"), cancellable = true)
    private static void preventSpawning(World world, double x, double y, double z, ItemStack stack, CallbackInfo ci) {
        if (world instanceof ServerWorld serverWorld) {
            if (!serverWorld.getGameRules().getBoolean(RedstoneToolsGameRules.DO_CONTAINER_DROPS)) ci.cancel();
        }
    }
}
