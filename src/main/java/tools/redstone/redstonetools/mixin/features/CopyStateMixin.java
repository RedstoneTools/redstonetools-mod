package tools.redstone.redstonetools.mixin.features;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(BlockItem.class)
public abstract class CopyStateMixin {

    @Shadow protected abstract boolean canPlace(ItemPlacementContext context, BlockState state);
    @Shadow public abstract Block getBlock();

    @Inject(method = "getPlacementState", at = @At("TAIL"), cancellable = true)
    public void getPlacementState(ItemPlacementContext context, CallbackInfoReturnable<BlockState> cir) {
        BlockStateComponent stateComponent = context.getStack().get(DataComponentTypes.BLOCK_STATE);
        Block block = null;
        if (context.getStack().getItem() instanceof BlockItem blockItem) {
            block = blockItem.getBlock();
        }
        BlockState state = null;
        if (stateComponent != null) {
            assert block != null;
            state = stateComponent.applyToState(block.getDefaultState());
        }
        if (state != null && this.canPlace(context, state)) {
            cir.setReturnValue(state);
        }
    }

}
