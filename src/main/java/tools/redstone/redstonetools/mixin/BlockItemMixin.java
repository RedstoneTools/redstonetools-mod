package tools.redstone.redstonetools.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tools.redstone.redstonetools.utils.BlockStateNbtUtil;


@Mixin(BlockItem.class)
public abstract class BlockItemMixin {

    @Shadow protected abstract boolean canPlace(ItemPlacementContext context, BlockState state);
    @Shadow public abstract Block getBlock();

    @Inject(method = "getPlacementState", at = @At("TAIL"), cancellable = true)
    public void getPlacementState(ItemPlacementContext context, CallbackInfoReturnable<BlockState> cir) {
        BlockState state = BlockStateNbtUtil.getPlacementStateOrNull(context.getStack());
        if (state != null && this.canPlace(context, state)) {
            cir.setReturnValue(state);
        }
    }

}
