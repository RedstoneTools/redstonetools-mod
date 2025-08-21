package tools.redstone.redstonetools.mixin.features;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import tools.redstone.redstonetools.features.toggleable.AutoRotateClient;
import tools.redstone.redstonetools.utils.BlockUtils;

@Mixin(BlockItem.class)
public abstract class AutoRotateClientMixin {
	@Shadow
	protected abstract boolean canPlace(ItemPlacementContext context, BlockState state);

	@ModifyReturnValue(method = "getPlacementState", at = @At("RETURN"))
	private BlockState changeRotation(BlockState original, @Local(argsOnly = true) ItemPlacementContext context) {
		if (!AutoRotateClient.isEnabled
				|| original == null) return original;
		original = BlockUtils.rotate(original);

		return this.canPlace(context, original) ? original : null;
	}
}