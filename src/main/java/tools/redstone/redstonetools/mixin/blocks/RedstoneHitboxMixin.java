package tools.redstone.redstonetools.mixin.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tools.redstone.redstonetools.features.toggleable.BigDustFeature;
import tools.redstone.redstonetools.utils.FeatureUtils;

@Mixin(RedstoneWireBlock.class)
public class RedstoneHitboxMixin {
    @Unique
    private static BigDustFeature bigDustFeature;

    // use array for better performance
    @Unique
    private static final VoxelShape[] SHAPES = new VoxelShape[16];

    @Inject(method="getOutlineShape", at = @At("HEAD"), cancellable = true)
    public void getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (bigDustFeature == null) {
            bigDustFeature = FeatureUtils.getFeature(BigDustFeature.class);
        }

        if (bigDustFeature.isEnabled()) {
            cir.setReturnValue(SHAPES[BigDustFeature.heightInPixels - 1]);
        }
    }

    static {
        for (int i = 1; i <= 16; i++) {
            SHAPES[i - 1] = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, i, 16.0);
        }
    }

}
