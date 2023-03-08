package com.domain.redstonetools.mixin;

import com.domain.redstonetools.RedstoneToolsClient;
import com.domain.redstonetools.features.toggleable.AutoDustFeature;
import com.domain.redstonetools.utils.ColoredBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin {
    private AutoDustFeature autoDustFeature;

    private AutoDustFeature getAutoDustFeature() {
        if (autoDustFeature == null) {
            autoDustFeature = RedstoneToolsClient.INJECTOR.getInstance(AutoDustFeature.class);
        }

        return autoDustFeature;
    }

    @Inject(method = "onPlaced", at = @At("TAIL"))
    private void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {
        if (!getAutoDustFeature().isEnabled()) {
            return;
        }

        var dustPos = pos.up();
        var block = world.getBlockState(pos).getBlock();
        var blockAbove = world.getBlockState(dustPos).getBlock();

        if (!blockAbove.equals(Blocks.AIR) || ColoredBlock.fromBlock(block) == null) {
            return;
        }

        placer.getWorld().setBlockState(dustPos, Blocks.REDSTONE_WIRE.getDefaultState());
    }
}
