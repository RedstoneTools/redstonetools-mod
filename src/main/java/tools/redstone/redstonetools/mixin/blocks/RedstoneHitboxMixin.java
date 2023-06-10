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
import tools.redstone.redstonetools.RedstoneToolsClient;
import tools.redstone.redstonetools.features.commands.BigDustHeightFeature;
import tools.redstone.redstonetools.features.toggleable.BigDustFeature;

@Pseudo
@Mixin(RedstoneWireBlock.class)
public class RedstoneHitboxMixin {

    @Inject(method="getOutlineShape", at = @At("HEAD"), cancellable = true)
    public void getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        BigDustFeature bigDustFeature = RedstoneToolsClient.INJECTOR.getInstance(BigDustFeature.class);
        BigDustHeightFeature bigDustHeightFeature = RedstoneToolsClient.INJECTOR.getInstance(BigDustHeightFeature.class);

        if (bigDustFeature.isEnabled()) {
            cir.setReturnValue( Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, bigDustHeightFeature.customHeight, 16.0) );
        }
    }

}
