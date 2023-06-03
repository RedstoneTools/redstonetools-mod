package tools.redstone.redstonetools.mixin.gamerules;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ItemScatterer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tools.redstone.redstonetools.RedstoneToolsGameRules;

@Mixin(ItemScatterer.class)
public class DoContainerDropsMixin {
    @Inject(method = "spawn(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)V", at = @At("HEAD"), cancellable = true)
    private static void spawn(World world, double x, double y, double z, ItemStack stack, CallbackInfo ci) {
        if (RedstoneToolsGameRules.DO_CONTAINER_DROPS != null && !world.getGameRules().getBoolean(RedstoneToolsGameRules.DO_CONTAINER_DROPS)) {
            ci.cancel();
        }
    }
}
