package tools.redstone.redstonetools.mixin.features;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tools.redstone.redstonetools.features.toggleable.AutoRotateFeature;
import tools.redstone.redstonetools.utils.FeatureUtils;

import java.util.Set;

@Mixin(Block.class)
public abstract class AutoRotateMixin {

	@Unique
	private static Set<Block> ROTATABLE;

	@Unique
	private static Set<Block> getRotatable() {
		if (ROTATABLE == null) {
			ROTATABLE = Set.of(
					Blocks.REPEATER,
					Blocks.COMPARATOR,
					Blocks.OBSERVER,
					Blocks.PISTON,
					Blocks.STICKY_PISTON
			);
		}
		return ROTATABLE;
	}

	@Inject(method = "onPlaced", at = @At("TAIL"))
	private void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {
		if (world == null) return;
		if (world.isClient) return;
		if (!(placer instanceof ServerPlayerEntity player)) return;
		if (!FeatureUtils.getFeature(AutoRotateFeature.class).isEnabled(player)) return;
		if (!getRotatable().contains(state.getBlock())) return;

		Property<Direction> dirProp = null;
		if (state.contains(Properties.HORIZONTAL_FACING)) {
			dirProp = Properties.HORIZONTAL_FACING;
		} else if (state.contains(Properties.FACING)) {
			dirProp = Properties.FACING;
		}

		if (dirProp == null) return;

		Direction current;
		try {
			current = state.get(dirProp);
		} catch (Exception e) {
			System.err.println("[AutoRotateMixin] could not read direction property for block " + state.getBlock() + " at " + pos + ": " + e);
			return;
		}

		if (current == null) return;

		world.setBlockState(pos, state.with(dirProp, current.getOpposite()));
	}
}