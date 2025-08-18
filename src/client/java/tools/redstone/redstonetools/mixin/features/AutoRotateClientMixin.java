package tools.redstone.redstonetools.mixin.features;


import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import tools.redstone.redstonetools.features.toggleable.AutoRotateClient;

@Mixin(BlockItem.class)
public abstract class AutoRotateClientMixin {
	@Shadow
	protected abstract boolean canPlace(ItemPlacementContext context, BlockState state);

	@ModifyReturnValue(method = "getPlacementState", at = @At("RETURN"))
	private BlockState changeRotation(BlockState original, @Local(argsOnly = true) ItemPlacementContext context) {
		if (!AutoRotateClient.isEnabled
				|| original == null) return original;
		if (original.contains(Properties.FACING))
			original = original.with(Properties.FACING, original.get(Properties.FACING).getOpposite());

		if (original.contains(Properties.HORIZONTAL_FACING))
			original= original.with(Properties.HORIZONTAL_FACING, original.get(Properties.HORIZONTAL_FACING).getOpposite());

		if (original.contains(Properties.HOPPER_FACING))
			if (original.get(Properties.HOPPER_FACING).getOpposite() != Direction.UP)
				original= original.with(Properties.HOPPER_FACING, original.get(Properties.HOPPER_FACING).getOpposite());

		return this.canPlace(context, original) ? original : null;
	}
}