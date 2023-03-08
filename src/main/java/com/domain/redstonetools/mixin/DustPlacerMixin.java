package com.domain.redstonetools.mixin;

import com.domain.redstonetools.features.commands.DustPlacerFeature;
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

import java.util.Objects;

@Mixin(Block.class)
public abstract class DustPlacerMixin {
    @Inject(method = "onPlaced", at = @At("TAIL"))
    private void injectMethod(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {
        BlockPos redstonePos = pos.add(0,1,0);
        BlockState block = world.getBlockState(pos);
        BlockState blockAbove = world.getBlockState(redstonePos);

        if(
                blockAbove == null ||
                        !DustPlacerFeature.isActive ||
                        !Objects.equals(blockAbove.getBlock().getLootTableId().toString(), "minecraft:empty")
        ) return;
        if (true) {
            Block redstone = Blocks.REDSTONE_WIRE;
            placer.getWorld().setBlockState(redstonePos, redstone.getDefaultState() );
        }
    }
}
