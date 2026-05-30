package tools.redstone.redstonetools.mixin.features;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tools.redstone.redstonetools.features.toggleable.AutoDustFeature;
import tools.redstone.redstonetools.utils.ColoredBlock;
import tools.redstone.redstonetools.utils.PlayerUtils;

@Mixin(Block.class)
public abstract class AutoDustMixin {
	@Inject(method = "setPlacedBy", at = @At("TAIL"))
	private void onPlaced(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {
		if (placer instanceof ServerPlayer player) {
			if (!AutoDustFeature.INSTANCE.isEnabled(player) || world.isClientSide()) {
				return;
			}

			var dustPos = pos.above();
			var block = world.getBlockState(pos).getBlock();
			var blockAbove = world.getBlockState(dustPos).getBlock();

			if (blockAbove != Blocks.AIR || ColoredBlock.fromBlock(block) == null) {
				return;
			}

			BlockPlaceContext context = new BlockPlaceContext(player, InteractionHand.MAIN_HAND, new ItemStack(Items.REDSTONE),
				new BlockHitResult(new Vec3(dustPos.getX(), dustPos.getY(), dustPos.getZ()), Direction.UP, dustPos, false));
			PlayerUtils.getWorld(placer).setBlockAndUpdate(dustPos, Blocks.REDSTONE_WIRE.getStateForPlacement(context));
		}
	}
}
