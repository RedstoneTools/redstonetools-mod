package tools.redstone.redstonetools.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractBlock.class)
public interface AbstractBlockMixin {
	@Invoker
	ItemStack callGetPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData);
}
