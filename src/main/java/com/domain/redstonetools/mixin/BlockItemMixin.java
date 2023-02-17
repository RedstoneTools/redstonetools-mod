package com.domain.redstonetools.mixin;

import com.domain.redstonetools.utils.BlockStateNbtUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;


@Mixin(BlockItem.class)
public abstract class BlockItemMixin {

    @Shadow protected abstract boolean canPlace(ItemPlacementContext context, BlockState state);
    @Shadow public abstract Block getBlock();


    //Checks, if block that is being placed has "blockstate" nbt. If so, replaces original blockstate with the one in nbt.
    @Overwrite
    public BlockState getPlacementState(ItemPlacementContext context) {
        NbtCompound nbt = context.getStack().getNbt();
        String str = "";
        if (nbt != null) str = nbt.getString("blockstate");
        BlockState state = this.getBlock().getPlacementState(context);

        if (!str.equals("")) {
            state = BlockStateNbtUtil.stringToBlockState(str,this.getBlock().getDefaultState());
        }
        return state != null && this.canPlace(context, state) ? state : null;
    }


}
