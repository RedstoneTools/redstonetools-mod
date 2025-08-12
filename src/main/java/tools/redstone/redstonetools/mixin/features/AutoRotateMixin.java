package tools.redstone.redstonetools.mixin.features;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
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
    private static final Set<Block> ROTATABLE = Set.of(
            Blocks.REPEATER,
            Blocks.COMPARATOR,
            Blocks.OBSERVER,
            Blocks.PISTON,
            Blocks.STICKY_PISTON
    );

    @Inject(method = "onPlaced", at = @At("TAIL"))
    private void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {
        if (world.isClient) return;
        if (!(placer instanceof ServerPlayerEntity player)) return;
        if (!FeatureUtils.getFeature(AutoRotateFeature.class).isEnabled(player)) return;
        if (!ROTATABLE.contains(state.getBlock())) return;

        EnumProperty<Direction> dirProp = null;
        if (state.contains(Properties.HORIZONTAL_FACING)) {
            dirProp = Properties.HORIZONTAL_FACING;
        } else if (state.contains(Properties.FACING)) {
            dirProp = Properties.FACING;
        }

        if (dirProp == null) return;

        // Rotate the block to its opposite face
        world.setBlockState(pos, state.with(dirProp, state.get(dirProp).getOpposite()));
    }
}
