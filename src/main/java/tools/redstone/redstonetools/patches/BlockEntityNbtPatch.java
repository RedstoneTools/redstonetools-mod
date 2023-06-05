package tools.redstone.redstonetools.fixes;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SkullItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;

public class BlockEntityNbtFix {

    public static ItemStack addBlockEntityNbt(ItemStack stack, BlockEntity blockEntity) {
        NbtCompound nbtCompound = blockEntity.createNbtWithIdentifyingData();
        NbtCompound nbtCompound2;
        if (stack.getItem() instanceof SkullItem && nbtCompound.contains("SkullOwner")) {
            nbtCompound2 = nbtCompound.getCompound("SkullOwner");
            stack.getOrCreateNbt().put("SkullOwner", nbtCompound2);
        } else {
            BlockItem.setBlockEntityNbt(stack, blockEntity.getType(), nbtCompound);
            nbtCompound2 = new NbtCompound();
            NbtList nbtList = new NbtList();
            nbtList.add(NbtString.of("\"(+NBT)\""));
            nbtCompound2.put("Lore", nbtList);
            stack.setSubNbt("display", nbtCompound2);
        }
        return stack;
    }

}
