package tools.redstone.redstonetools.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;


public class ItemUtils {
    private ItemUtils(){
    }

    public static void addExtraNBTText(ItemStack itemStack, String text) {
        NbtCompound displayNbt = itemStack.getSubNbt("display");
        NbtList loreList = new NbtList();

        if (displayNbt == null) {
            displayNbt = new NbtCompound();
        } else {
            loreList = (NbtList) displayNbt.get("Lore");
        }
        String lore = "\"(+"+text +")\"";

        if (loreList != null && !loreList.contains(NbtString.of(lore))) loreList.add(NbtString.of(lore));
        displayNbt.put("Lore", loreList);
        itemStack.setSubNbt("display", displayNbt);
    }

}
