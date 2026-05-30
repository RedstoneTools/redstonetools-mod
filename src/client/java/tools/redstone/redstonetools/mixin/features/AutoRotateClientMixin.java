package tools.redstone.redstonetools.mixin.features;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import tools.redstone.redstonetools.features.toggleable.AutoRotateClient;

@Mixin(BlockItem.class)
public abstract class AutoRotateClientMixin {
	@Shadow
	protected abstract boolean canPlace(BlockPlaceContext context, BlockState state);

	@ModifyReturnValue(method = "getPlacementState", at = @At("RETURN"))
	private BlockState changeRotation(BlockState original, @Local(argsOnly = true) BlockPlaceContext context) {
		if (!AutoRotateClient.isEnabled.getBooleanValue()
				|| original == null) return original;

		BlockState backup = original;
		original = original.rotate(Rotation.CLOCKWISE_180);

		if (this.canPlace(context, original))
			return original;
		else if (this.canPlace(context, backup))
			return backup;
		else
			return null;
	}
}