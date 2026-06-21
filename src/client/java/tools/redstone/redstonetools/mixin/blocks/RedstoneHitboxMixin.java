package tools.redstone.redstonetools.mixin.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tools.redstone.redstonetools.config.General;
import tools.redstone.redstonetools.features.toggleable.BigDustFeature;

@Mixin(RedStoneWireBlock.class)
public class RedstoneHitboxMixin {
	// use array for better performance
	@Unique
	private static final VoxelShape[] SHAPES = new VoxelShape[17];

	@Inject(method = "getShape", at = @At("HEAD"), cancellable = true)
	public void getOutlineShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
		if (BigDustFeature.INSTANCE.isEnabled()) {
			cir.setReturnValue(SHAPES[General.BIGDUST_HEIGHT_IN_PIXELS.getIntegerValue()]);
		}
	}

	static {
		SHAPES[0] = Block.box(0.0, 0.0, 0.0, 16.0, 0.01, 16.0);
		for (int i = 1; i <= 16; i++) {
			SHAPES[i] = Block.box(0.0, 0.0, 0.0, 16.0, i, 16.0);
		}
	}
}
