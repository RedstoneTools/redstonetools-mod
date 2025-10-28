package tools.redstone.redstonetools.mixin.features;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import tools.redstone.redstonetools.features.toggleable.AutoRotateFeature;
import tools.redstone.redstonetools.utils.BlockUtils;

@Mixin(BlockItem.class)
public abstract class AutoRotateMixin {
	@Shadow
	protected abstract boolean canPlace(ItemPlacementContext context, BlockState state);

	@ModifyReturnValue(method = "getPlacementState", at = @At("RETURN"))
	private BlockState changeRotation(BlockState original, @Local(argsOnly = true) ItemPlacementContext context) {
		if (!(context.getPlayer() instanceof ServerPlayerEntity player))         return original;
		//? if <1.21.10 {
		MinecraftServer server = player.getServer();
		if (server == null)                                                      return original;
		//?} else {
		/*MinecraftServer server = player.getEntityWorld().getServer();
		*///?}
		if (!server.isDedicated())                                               return original;
		if (!AutoRotateFeature.INSTANCE.isEnabled(player)) return original;
		if (original == null)                                                    return null;

		BlockState backup = original;
		original = BlockUtils.rotate(original);

		if (this.canPlace(context, original))
			return original;
		else if (this.canPlace(context, backup))
			return backup;
		else
			return null;
	}
}