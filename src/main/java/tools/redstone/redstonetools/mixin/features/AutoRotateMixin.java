package tools.redstone.redstonetools.mixin.features;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import tools.redstone.redstonetools.features.toggleable.AutoRotateFeature;

@Mixin(BlockItem.class)
public abstract class AutoRotateMixin {
	@Shadow
	protected abstract boolean canPlace(BlockPlaceContext context, BlockState state);

	@ModifyReturnValue(method = "getPlacementState", at = @At("RETURN"))
	private BlockState changeRotation(BlockState original, @Local(argsOnly = true) BlockPlaceContext context) {
		if (!(context.getPlayer() instanceof ServerPlayer player))         return original;
		//? if <1.21.10 {
		/*MinecraftServer server = player.getServer();
		if (server == null)                                                      return original;
		*///?} else {
		MinecraftServer server = player.level().getServer();
		//?}
		if (!server.isDedicatedServer())                                               return original;
		if (!AutoRotateFeature.INSTANCE.isEnabled(player)) return original;
		if (original == null)                                                    return null;

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